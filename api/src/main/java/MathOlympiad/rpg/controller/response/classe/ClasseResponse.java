package MathOlympiad.rpg.controller.response.classe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClasseResponse {

    private Long id;

    private String nome;

    private String imagem;

    private Map<String, Double> status;

}
