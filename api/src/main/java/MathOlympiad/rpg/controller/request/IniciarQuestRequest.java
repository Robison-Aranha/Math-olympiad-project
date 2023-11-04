package MathOlympiad.rpg.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class IniciarQuestRequest {


    @NotBlank
    private Long personagemId;

}
