package MathOlympiad.quiz.websocket.mapper;

import MathOlympiad.quiz.domain.Usuario;
import MathOlympiad.quiz.websocket.domain.MensagemPerfil;
import MathOlympiad.quiz.websocket.enumerator.TipoMensagemPerfil;

public class MensagemPerfilsMapper {

    public static MensagemPerfil toResponseJoin(Usuario entity) {

        return MensagemPerfil.builder()
                .tipo(TipoMensagemPerfil.JOIN.name())
                .nome(entity.getNome())
                .imagemPerfil(entity.getImagem())
                .build();

    }

}
