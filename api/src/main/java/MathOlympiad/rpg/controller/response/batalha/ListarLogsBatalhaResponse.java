package MathOlympiad.rpg.controller.response.batalha;


import MathOlympiad.rpg.controller.response.batalha.BatalhaResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListarLogsBatalhaResponse {

    private ArrayList<BatalhaResponse> listaLogsBatalha;

    private String vencedor;

}
