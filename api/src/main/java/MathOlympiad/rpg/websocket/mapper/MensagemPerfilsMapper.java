package MathOlympiad.rpg.websocket.mapper;

import MathOlympiad.rpg.domain.Usuario;
import MathOlympiad.rpg.websocket.domain.MensagemPerfil;
import MathOlympiad.rpg.websocket.enumerator.TipoMensagemPerfil;

public class MensagemPerfilsMapper {

    public static MensagemPerfil toResponseJoin(Usuario entity) {

        return MensagemPerfil.builder()
                .tipo(TipoMensagemPerfil.JOIN.name())
                .nome(entity.getNome())
                .imagemPerfil(entity.getImagem())
                .build();

    }

}
