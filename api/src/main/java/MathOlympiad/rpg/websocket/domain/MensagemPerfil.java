package MathOlympiad.rpg.websocket.domain;


import MathOlympiad.rpg.websocket.enumerator.TipoMensagemPerfil;
import lombok.Data;

@Data
public class MensagemPerfil {

    private TipoMensagemPerfil tipo;

    private String nome;

    private String imagemPerfil;


}
