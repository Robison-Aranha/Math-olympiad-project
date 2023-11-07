package MathOlympiad.rpg.controller.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListarLogsBatalhaResponse {

    private ArrayList<BatalhaResponse> listaLogsBatalha;

    private String vencedor;

}
