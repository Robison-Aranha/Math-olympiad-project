package MathOlympiad.rpg.mapper;

import MathOlympiad.rpg.controller.response.ListarSalaResponse;
import MathOlympiad.rpg.domain.Sala;

public class ListarSalaMapper {

    public static ListarSalaResponse toResponse(Sala entity) {
        return ListarSalaResponse.builder()
                .nome(entity.getNome())
                .numeroTotalDeJogadores(entity.getNumeroJogadores())
                .numeroAtualDeJogadores(entity.getParticipantes().size())
                .id(entity.getId())
                .salaEPrivada(entity.isPrivado())
                .build();
    }


}
