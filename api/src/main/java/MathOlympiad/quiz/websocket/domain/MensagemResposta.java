package MathOlympiad.quiz.websocket.domain;


import lombok.Data;

@Data
public class MensagemResposta {

    private String nome;
    private String resposta;
    private Double contagem;

}
