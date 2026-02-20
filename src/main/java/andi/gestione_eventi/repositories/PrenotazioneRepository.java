package andi.gestione_eventi.repositories;

import andi.gestione_eventi.entities.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.Optional;
import java.util.UUID;

@Repository
public interface PrenotazioneRepository extends JpaRepository<Prenotazione, UUID> {
    @Query("SELECT p FROM Prenotazione p WHERE p.evento.id_evento = :id_evento AND p.utente.id_utente = :id_utente")
    Optional<Prenotazione> findPrenotazioneByIdEventoAndIdUtente(
            @Param("id_evento") UUID id_evento,
            @Param("id_utente") UUID id_utente
    );

}
