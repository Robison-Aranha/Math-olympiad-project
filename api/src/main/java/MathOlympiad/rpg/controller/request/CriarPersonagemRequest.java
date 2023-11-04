package MathOlympiad.rpg.controller.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CriarPersonagemRequest {

    @NotBlank
    private String classe;

    @NotBlank
    private String nome;

}
