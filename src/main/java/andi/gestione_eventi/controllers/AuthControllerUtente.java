package andi.gestione_eventi.controllers;

import andi.gestione_eventi.services.AuthServiceUtente;
import andi.gestione_eventi.entities.Utente;
import andi.gestione_eventi.exceptions.ValidationException;
import andi.gestione_eventi.DTOs.UtenteDTO;
import andi.gestione_eventi.DTOs.LoginDTO;
import andi.gestione_eventi.DTOs.LoginResponseDTO;
import andi.gestione_eventi.services.AuthServiceUtente;
import andi.gestione_eventi.services.UtenteService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthControllerUtente {
    private final AuthServiceUtente authService;
    private final UtenteService utenteService;

    public AuthControllerUtente(AuthServiceUtente authService, UtenteService utenteService) {
        this.authService = authService;
        this.utenteService = utenteService;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginDTO body) {

        return new LoginResponseDTO(this.authService.checkCredentialsAndGenerateToken(body));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Utente createUtente(@RequestBody @Validated UtenteDTO payload, BindingResult validationResult) {
        // @Validated serve per attivare la validazione, se non lo usiamo è come non farla

        if (validationResult.hasErrors()) {

//			String errors = validationResult.getFieldErrors().stream()
//					.map(fieldError -> fieldError.getDefaultMessage())
//					.collect(Collectors.joining(". "));
//
//			throw new ValidationException(errors);
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();

            throw new ValidationException(errorsList);
        } else {
            return this.utenteService.save(payload);
        }

    }
}
