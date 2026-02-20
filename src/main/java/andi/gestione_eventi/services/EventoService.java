package andi.gestione_eventi.services;

import andi.gestione_eventi.entities.Evento;
import andi.gestione_eventi.entities.Utente;
import andi.gestione_eventi.exceptions.BadRequestException;
import andi.gestione_eventi.exceptions.NotFoundException;
import andi.gestione_eventi.DTOs.EventoDTO;
import andi.gestione_eventi.exceptions.UnauthorizedException;
import andi.gestione_eventi.repositories.EventoRepository;
import andi.gestione_eventi.repositories.UtenteRepository;
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
public class EventoService {

    private final EventoRepository eventoRepository;
    private final UtenteRepository utenteRepository;

    @Autowired
    public EventoService(EventoRepository eventoRepository,UtenteRepository utenteRepository) {
        this.eventoRepository = eventoRepository;
        this.utenteRepository= utenteRepository;
    }

    public Evento save(EventoDTO payload) {

        this.eventoRepository
                .findEventoByLuogoAndDataEvento(payload.getLuogo(), payload.getDataEvento())
                .ifPresent(evento -> {
                    throw new BadRequestException(
                            "L'evento nel luogo " + evento.getLuogo() +
                                    " nella data " + evento.getDataEvento() + " esiste già!"
                    );
                });

        Utente organizer = utenteRepository.findById(payload.getOrganizerId())
                .orElseThrow(() -> new NotFoundException(payload.getOrganizerId()));


        Evento newEvento = new Evento(payload.getTitolo(),payload.getDescrizione(),payload.getDataEvento(), payload.getLuogo(), payload.getPosti_disponibili(),organizer);
        Evento savedEvento = this.eventoRepository.save(newEvento);
        log.info("L'evento con luogo " + newEvento.getLuogo() + " del " + newEvento.getDataEvento() + " è stato salvato correttamente con id:" + newEvento.getId_evento());
        return savedEvento;
    }

    public Page<Evento> findAll(int page, int size, String orderBy, String sortCriteria) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(page, size,
                sortCriteria.equals("desc") ? Sort.by(orderBy).descending() : Sort.by(orderBy));
        return this.eventoRepository.findAll(pageable);
    }

    public Evento findById(UUID eventoId) {
        return this.eventoRepository.findById(eventoId)
                .orElseThrow(() -> new NotFoundException(eventoId));
    }


    public void deleteEventByOrganizer(UUID eventoId, UUID organizerId) {

        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new NotFoundException(eventoId));

        if (!evento.getOrganizer().getId_utente().equals(organizerId)) {
            throw new UnauthorizedException("Non puoi eliminare l'evento dato che non ne sei il creatore");
        }

        eventoRepository.delete(evento);
    }
}
