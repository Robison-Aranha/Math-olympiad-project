package MathOlympiad.rpg.controller.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BatalhaResponse {

    private String nome;

    private Double dano;

    private Double PorcentagemdanoDesviado;

    private Double critico;

    private Double danoTotal;

}
