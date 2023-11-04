package MathOlympiad.rpg.controller.response;


import MathOlympiad.rpg.domain.Item;
import lombok.Data;

import java.util.HashMap;

@Data
public class ListarItensIventarioResponse {

    private HashMap<Integer, Item> itens;

}
