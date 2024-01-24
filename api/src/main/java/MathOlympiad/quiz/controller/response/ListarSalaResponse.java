package MathOlympiad.quiz.controller.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListarSalaResponse {

    private Long id;

    private String nome;

    private Integer numeroTotalDeJogadores;

    private Integer numeroAtualDeJogadores;

    private boolean salaEPrivada;
}
