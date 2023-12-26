package MathOlympiad.rpg.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EntrarSalaPrivadaRequest {


    @NotBlank
    private String senha;

}
