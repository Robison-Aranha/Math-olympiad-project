package MathOlympiad.rpg.websocket.domain;


import MathOlympiad.rpg.websocket.enumerator.TipoMensagemPerfil;
import lombok.Data;

@Data
public class MensagemPerfil {

    private String tipo;

    private String nome;

    private String imagemPerfil;

    private Integer pontos;

}
