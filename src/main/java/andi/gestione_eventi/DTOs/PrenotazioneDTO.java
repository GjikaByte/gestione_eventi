package andi.gestione_eventi.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class PrenotazioneDTO {
    @NotNull(message = "L'Id dell'evento e' un campo obbligatorio")
    private UUID id_evento;
    @NotNull(message = "L'Id dell'utente e' un campo obbligatorio")
    private UUID id_utente;
    private String preferenze;
}
