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
@Table(name = "eventi")
public class Evento {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id_evento;
    private String descrizione;
    @Column(name="data_evento", nullable = false)
    private LocalDate dataEvento;
    private String luogo;
    private String posti_disponibili;

    public Evento(String descrizione, LocalDate dataEvento, String luogo, String posti_disponibili) {
        this.descrizione = descrizione;
        this.dataEvento = dataEvento;
        this.luogo = luogo;
        this.posti_disponibili = posti_disponibili;
    }
}
