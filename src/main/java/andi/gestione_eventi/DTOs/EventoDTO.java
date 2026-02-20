package andi.gestione_eventi.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class EventoDTO {
    @NotBlank(message = "La destinazione dell'evento e' un campo obbligatorio")
    @Size(min = 2, max = 30, message = "La descrizione dell'evento deve essere tra i 2 e i 30 caratteri")
    private String descrizione;
    @NotBlank(message = "La data dell'evento e' un campo obbligatorio")
    private LocalDate dataEvento;
    @NotBlank(message = "Il luogo dell'evento e' un campo obbligatorio")
    @Size(min = 2, max = 30, message = "Il luogo dell'evento deve essere tra i 2 e i 30 caratteri")
    private String luogo;
    @NotBlank(message = "I posti disponibili dell'evento e' un campo obbligatorio")
    private long posti_disponibili;

}
