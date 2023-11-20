package MathOlympiad.rpg.controller.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PersonagemRequest {

    @NotBlank
    private Long personagemId;

}
