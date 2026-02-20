package andi.gestione_eventi.services;

import andi.gestione_eventi.entities.Utente;
import andi.gestione_eventi.exceptions.UnauthorizedException;
import andi.gestione_eventi.DTOs.LoginDTO;
import andi.gestione_eventi.security.JWTToolsUtente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceUtente {

    private final UtenteService utenteService;
    private final JWTToolsUtente jwtTools;
    private final PasswordEncoder bcrypt;


    @Autowired
    public AuthServiceUtente(UtenteService utenteService, JWTToolsUtente jwtTools, PasswordEncoder bcrypt) {

        this.utenteService = utenteService;
        this.jwtTools = jwtTools;
        this.bcrypt = bcrypt;
    }

    public String checkCredentialsAndGenerateToken(LoginDTO body) {
        // 1. Controllo credenziali
        // 1.1 Controllo se esiste un utente con quell'email
        Utente found = this.utenteService.findByEmail(body.email());

        // 1.2 Se esiste controllo se la sua password (quella nel DB) è uguale a quella nel body

        if (bcrypt.matches(body.password(), found.getPassword())) {
            // 2. Se credenziali OK
            // 2.1 Genero token
            String accessToken = jwtTools.generateToken(found);

            // 2.2 Ritorno token
            return accessToken;

        } else {
            // 3. Se credenziali non ok --> 401 Unauthorized
            throw new UnauthorizedException("Credenziali errate!");
        }


    }
}
