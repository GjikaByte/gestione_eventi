package andi.gestione_eventi.services;

import andi.gestione_eventi.entities.Utente;
import andi.gestione_eventi.exceptions.BadRequestException;
import andi.gestione_eventi.exceptions.NotFoundEmailException;
import andi.gestione_eventi.exceptions.NotFoundException;
import andi.gestione_eventi.DTOs.UtenteDTO;
import andi.gestione_eventi.repositories.UtenteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class UtenteService {

    private final UtenteRepository utenteRepository;
    private final PasswordEncoder bcrypt;


    @Autowired
    public UtenteService(UtenteRepository utenteRepository,PasswordEncoder bcrypt) {
        this.utenteRepository = utenteRepository;
        this.bcrypt=bcrypt;
    }

    public Utente save(UtenteDTO payload) {

        this.utenteRepository.findByEmail(payload.getEmail()).ifPresent(utente -> {
            throw new BadRequestException("L'email " + utente.getEmail() + " è già in uso!");
        });
        Utente newUtente = new Utente(payload.getUsername(), payload.getNome(), payload.getCognome(), payload.getEmail(), bcrypt.encode(payload.getPassword()));
        Utente savedUtente = this.utenteRepository.save(newUtente);
        log.info("L'utente con Cognome " + savedUtente.getCognome() + " è stato salvato correttamente!");
        return savedUtente;
    }

    public Page<Utente> findAll(int page, int size, String orderBy, String sortCriteria) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(page, size,
                sortCriteria.equals("desc") ? Sort.by(orderBy).descending() : Sort.by(orderBy));
        return this.utenteRepository.findAll(pageable);
    }

    public Utente findById(UUID utenteId) {
        return this.utenteRepository.findById(utenteId)
                .orElseThrow(() -> new NotFoundException(utenteId));
    }

    public void findByIdAndDelete(UUID utenteId) {
        Utente found = this.findById(utenteId);
        this.utenteRepository.delete(found);
        log.info("L'utente con id " + utenteId + " è stato eliminato correttamente");

    }

    public Utente findByEmail(String email) {
        return this.utenteRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundEmailException(email));
    }
}

