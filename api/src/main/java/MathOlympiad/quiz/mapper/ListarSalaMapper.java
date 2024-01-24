package MathOlympiad.quiz.mapper;

import MathOlympiad.quiz.controller.response.ListarSalaResponse;
import MathOlympiad.quiz.domain.Sala;

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
