package MathOlympiad.rpg.controller.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CriarSalaRequest {

    @NotBlank
    private String senha;

    @NotBlank
    private String nome;

    @NotNull
    private Integer tempoPerguntas;

    @NotNull
    private boolean privado;

    @NotNull
    List<String> temas;

    @NotNull
    private Integer numeroRodadas;

    @NotNull
    private Integer numeroJogadores;

}
