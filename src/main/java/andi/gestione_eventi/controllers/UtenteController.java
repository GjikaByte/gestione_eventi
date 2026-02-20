package andi.gestione_eventi.controllers;

import andi.gestione_eventi.entities.Utente;
import andi.gestione_eventi.DTOs.UtenteDTO;
import andi.gestione_eventi.services.UtenteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("utenti")
public class UtenteController {
    private final UtenteService utenteService;

    @Autowired
    public UtenteController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    // 1. POST http://localhost:3001/utenti (+ Payload)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Utente createUser(@RequestBody @Valid UtenteDTO payload) {
        return this.utenteService.save(payload);
    }


}
