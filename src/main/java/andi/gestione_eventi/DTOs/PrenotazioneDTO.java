package andi.gestione_eventi.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class PrenotazioneDTO {
    @NotBlank(message = "L'Id dell'evento e' un campo obbligatorio")
    @Size(min = 20, max = 50, message = "L'Id dell'evento deve essere tra i 20 e i 50 caratteri")
    private UUID id_evento;
    @NotBlank(message = "L'Id dell'utente e' un campo obbligatorio")
    @Size(min = 20, max = 50, message = "L'Id dell'utente deve essere tra i 20 e i 50 caratteri")
    private UUID id_utente;
    private String preferenze;
}
