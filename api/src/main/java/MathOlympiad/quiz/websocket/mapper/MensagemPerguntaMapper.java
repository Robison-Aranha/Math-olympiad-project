package MathOlympiad.quiz.websocket.mapper;

import MathOlympiad.quiz.domain.Pergunta;
import MathOlympiad.quiz.websocket.domain.MensagemPergunta;

public class MensagemPerguntaMapper {

    public static MensagemPergunta toResponse(Pergunta entity) {
        return MensagemPergunta.builder()
                .link(entity.getLink())
                .questao(entity.getQuestao())
                .build();
    }


}
