package MathOlympiad.rpg.controller.response.personagem;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListarPersonagensResponse {

    private Long id;

    private String nome;

    private String Imagem;

}
