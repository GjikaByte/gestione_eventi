package andi.gestione_eventi.services;

import andi.gestione_eventi.entities.Utente;
import andi.gestione_eventi.entities.Prenotazione;
import andi.gestione_eventi.entities.Evento;
import andi.gestione_eventi.exceptions.BadRequestException;
import andi.gestione_eventi.exceptions.NotFoundException;
import andi.gestione_eventi.DTOs.PrenotazioneDTO;
import andi.gestione_eventi.repositories.UtenteRepository;
import andi.gestione_eventi.repositories.PrenotazioneRepository;
import andi.gestione_eventi.repositories.EventoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class PrenotazioneService {

    private final PrenotazioneRepository prenotazioneRepository;
    private final UtenteRepository utenteRepository;
    private final EventoRepository eventoRepository;

    @Autowired
    public PrenotazioneService(PrenotazioneRepository prenotazioneRepository, UtenteRepository utenteRepository, EventoRepository eventoRepository) {
        this.prenotazioneRepository = prenotazioneRepository;
        this.utenteRepository = utenteRepository;
        this.eventoRepository = eventoRepository;
    }

    public Prenotazione save(PrenotazioneDTO payload) {

        this.prenotazioneRepository
                .findPrenotazioneByIdEventoAndIdUtente(payload.getId_evento(),payload.getId_utente())
                .ifPresent(prenotazione -> {
                    throw new BadRequestException(
                            "La prenotazione per per l'evento " + prenotazione.getEvento() +
                                    " per il dipendente " + prenotazione.getUtente() + " esiste già!"
                    );
                });

        Evento eventoFound = eventoRepository
                .findById(payload.getId_evento())
                .orElseThrow(() -> new RuntimeException("Viaggio non trovato con id: " + payload.getId_evento()));


        Utente utenteFound = utenteRepository.
                findById(payload.getId_utente()).
                orElseThrow(() -> new RuntimeException("Dipendente non trovato con id: " + payload.getId_utente()));

        Prenotazione newPrenotazione = new Prenotazione(eventoFound,utenteFound,payload.getPreferenze());
        Prenotazione savedPrenotazione = this.prenotazioneRepository.save(newPrenotazione);
        log.info("la prenotazione per l'evento " + savedPrenotazione.getEvento() + " e utente "  +savedPrenotazione.getUtente() + " è stata salvata correttamente!");
        return savedPrenotazione;
    }

    public Page<Prenotazione> findAll(int page, int size, String orderBy, String sortCriteria) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(page, size,
                sortCriteria.equals("desc") ? Sort.by(orderBy).descending() : Sort.by(orderBy));
        return this.prenotazioneRepository.findAll(pageable);
    }

    public Prenotazione findById(UUID prenotazioneId) {
        return this.prenotazioneRepository.findById(prenotazioneId)
                .orElseThrow(() -> new NotFoundException(prenotazioneId));
    }

    public void deletePrenotazioneByUtente(UUID prenotazioneId, UUID utenteId) {

        Prenotazione prenotazione = this.prenotazioneRepository.findById(prenotazioneId)
                .orElseThrow(() -> new NotFoundException(prenotazioneId));

        if (!prenotazione.getUtente().getId_utente().equals(utenteId)) {
            throw new BadRequestException("Non puoi eliminare una prenotazione che non hai fatto tu");
        }

        this.prenotazioneRepository.delete(prenotazione);
        log.info("La prenotazione con id " + prenotazioneId + " dell'utente " + utenteId + " è stata eliminata correttamente");
    }
}
