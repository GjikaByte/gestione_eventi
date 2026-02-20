package andi.gestione_eventi.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "prenotazioni")
public class Prenotazione {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id_prenotazione;
    @ManyToOne
    @JoinColumn(name = "id_evento")
    private Evento evento;
    @ManyToOne
    @JoinColumn(name = "id_dipendente")
    private Utente utente;
    private String preferenze;

    public Prenotazione(Evento evento, Utente utente, String preferenze) {
        this.evento = evento;
        this.utente = utente;
        this.preferenze = preferenze;
    }
}
