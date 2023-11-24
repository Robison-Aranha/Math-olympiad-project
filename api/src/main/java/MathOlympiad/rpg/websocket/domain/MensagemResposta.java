package MathOlympiad.rpg.websocket.domain;


import lombok.Data;

@Data
public class MensagemResposta {

    private String nome;
    private String resposta;
    private Integer contagem;
    private Integer rodada;

}
