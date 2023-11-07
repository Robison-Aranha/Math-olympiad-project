package MathOlympiad.rpg.mapper;

import MathOlympiad.rpg.controller.response.ItemResponse;
import MathOlympiad.rpg.domain.Item;

public class ItemMapper {

    public static ItemResponse toResponse(Item entity) {
        return ItemResponse.builder()
                .id(entity.getId())
                .image(entity.getImagem())
                .build();
    }

}
