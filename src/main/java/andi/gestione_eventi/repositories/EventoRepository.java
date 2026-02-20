package andi.gestione_eventi.repositories;

import andi.gestione_eventi.entities.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventoRepository extends JpaRepository<Evento, UUID> {
    @Query("SELECT e FROM Evento e WHERE e.luogo = :luogo AND e.dataEvento = :dataEvento")
    Optional<Evento> findEventoByLuogoAndDataEvento(
            @Param("luogo") String luogo,
            @Param("dataEvento") LocalDate dataEvento
    );

}
