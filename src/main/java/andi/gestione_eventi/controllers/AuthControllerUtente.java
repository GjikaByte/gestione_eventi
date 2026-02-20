package andi.gestione_eventi.controllers;

import andi.gestione_eventi.DTOs.EventoDTO;
import andi.gestione_eventi.entities.Evento;
import andi.gestione_eventi.services.AuthServiceUtente;
import andi.gestione_eventi.entities.Utente;
import andi.gestione_eventi.exceptions.ValidationException;
import andi.gestione_eventi.DTOs.UtenteDTO;
import andi.gestione_eventi.DTOs.LoginDTO;
import andi.gestione_eventi.DTOs.LoginResponseDTO;
import andi.gestione_eventi.services.AuthServiceUtente;
import andi.gestione_eventi.services.EventoService;
import andi.gestione_eventi.services.UtenteService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthControllerUtente {
    private final AuthServiceUtente authService;
    private final UtenteService utenteService;
    private final EventoService eventoService;


    public AuthControllerUtente(AuthServiceUtente authService, UtenteService utenteService,EventoService eventoService) {
        this.authService = authService;
        this.utenteService = utenteService;
        this.eventoService = eventoService;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginDTO body) {

        return new LoginResponseDTO(this.authService.checkCredentialsAndGenerateToken(body));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Utente createUtente(@RequestBody @Validated UtenteDTO payload, BindingResult validationResult) {

        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();

            throw new ValidationException(errorsList);
        } else {
            return this.utenteService.save(payload);
        }

    }
    @PostMapping("/registerOrganizer")
    @ResponseStatus(HttpStatus.CREATED)
    public Utente createUtenteOrganizzatore(@RequestBody @Validated UtenteDTO payload, BindingResult validationResult) {

        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();

            throw new ValidationException(errorsList);
        } else {
            return this.utenteService.saveOrganizer(payload);
        }
    }

    // GET http://localhost:3001/auth/organizer/utenti
    @GetMapping("/organizer/utenti")
    public Page<Utente> findAll(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                @RequestParam(defaultValue = "cognome") String orderBy,
                                @RequestParam(defaultValue = "asc") String sortCriteria) {

        return this.utenteService.findAll(page, size, orderBy, sortCriteria);
    }

    // GET http://localhost:3001/auth/organizer/{utenteId}
    @GetMapping("/organizer/{utenteId}")
    public Utente findById(@PathVariable UUID utenteId) {
        return this.utenteService.findById(utenteId);
    }


    // POST http://localhost:3001/auth/registerEvent
    @PostMapping("/registerEvent")
    @ResponseStatus(HttpStatus.CREATED)
    public Evento createEvent(@RequestBody @Validated EventoDTO payload, BindingResult validationResult) {

        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();

            throw new ValidationException(errorsList);
        } else {
            return this.eventoService.save(payload);
        }
    }
    // GET http://localhost:3001/auth/organizer/eventi
    @GetMapping("/organizer/eventi")
    public Page<Evento> findAllEventi(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                @RequestParam(defaultValue = "titolo") String orderBy,
                                @RequestParam(defaultValue = "asc") String sortCriteria) {

        return this.eventoService.findAll(page, size, orderBy, sortCriteria);
    }
    // DELETE http://localhost:3001/auth/organizer/{organizerId}/eventi/{eventoId}
    @DeleteMapping("/organizer/{organizerId}/eventi/{eventoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable UUID organizerId,
                            @PathVariable UUID eventoId) {

        this.eventoService.deleteEventByOrganizer(eventoId, organizerId);
    }
}
