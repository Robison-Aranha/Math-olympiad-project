package MathOlympiad.rpg.controller.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CriarSalaRequest {

    
    private String senha;

    @NotNull
    private String nome;

    @NotNull
    private Integer tempoRodada;

    @NotNull
    private boolean privado;

    @NotNull
    private Integer numeroRodadas;

    @NotNull
    private Integer numeroJogadores;

    @NotNull
    private List<String> temas;

}
