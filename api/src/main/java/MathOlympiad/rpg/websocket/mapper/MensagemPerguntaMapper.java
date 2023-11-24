package MathOlympiad.rpg.websocket.mapper;

import MathOlympiad.rpg.domain.Pergunta;
import MathOlympiad.rpg.websocket.domain.MensagemPergunta;

public class MensagemPerguntaMapper {

    public static MensagemPergunta toResponse(Pergunta entity) {
        return MensagemPergunta.builder()
                .link(entity.getLink())
                .questao(entity.getQuestao())
                .build();
    }


}
