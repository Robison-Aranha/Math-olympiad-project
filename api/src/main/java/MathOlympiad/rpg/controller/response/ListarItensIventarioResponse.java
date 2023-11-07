package MathOlympiad.rpg.controller.response;


import MathOlympiad.rpg.domain.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListarItensIventarioResponse {

    private HashMap<Integer, ItemResponse> itens;

}
