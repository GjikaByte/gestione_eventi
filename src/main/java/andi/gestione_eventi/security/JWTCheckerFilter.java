package andi.gestione_eventi.security;


import andi.gestione_eventi.exceptions.UnauthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Per poter esssere inserito nella filter chain deve essere un component e deve estendere OncePerRequestFilter
public class JWTCheckerFilter extends OncePerRequestFilter {

    private final JWTToolsUtente jwtTools;

    @Autowired
    public JWTCheckerFilter(JWTToolsUtente jwtTools) {
        this.jwtTools = jwtTools;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Questo è il metodo che viene eseguito ad ogni richiesta
        // Sarà questo metodo quindi che dovrà fare il controllo dei token

        // PIANO DI BATTAGLIA

        // 1. Verifichiamo se la richiesta contiene l'header Authorization e che in caso sia nel formato "Bearer oi1j3oj21o3j213jo12j3"
        // Se l'header non c'è oppure se è malformato --> lanciamo eccezione
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnauthorizedException("Inserire il token nell'Authorization header nel formato corretto");

        // 2. Estraiamo il token dall'header
        //  authHeader -> "Bearer oi1j3oj21o3j213jo12j3"
        String accessToken = authHeader.replace("Bearer ", "");

        // 3. Verifichiamo se il token è valido (controllare la firma e verificare data di scadenza)
        jwtTools.verifyToken(accessToken);

        // 4. Se tutto è OK --> andiamo avanti, trasmettiamo la richiesta al prossimo (può essere o un altro elemento della catena oppure il controller)
        filterChain.doFilter(request, response);

        // 5. Se c'è qualche problema con il token -> eccezione
    }

    // Tramite l'Override del metodo sottostante posso specificare quando il filtro non debba essere chiamato in causa
    // Ad esempio posso dirgli di non filtrare tutte le richieste dirette al controller "/auth"
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
        // return request.getServletPath().equals("/auth/login") || request.getServletPath().equals("/auth/register");
    }
}
