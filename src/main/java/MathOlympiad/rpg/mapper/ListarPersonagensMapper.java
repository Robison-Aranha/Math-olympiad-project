package MathOlympiad.rpg.mapper;

import MathOlympiad.rpg.controller.response.ListarPersonagensResponse;
import MathOlympiad.rpg.domain.Personagem;

public class ListarPersonagensMapper {

    public static ListarPersonagensResponse toResponse(Personagem entity) {

        return ListarPersonagensResponse.builder()
                .nome(entity.getNome())
                .Imagem(entity.getClasse().getImagem())
                .id(entity.getId())
                .build();

    }


}
