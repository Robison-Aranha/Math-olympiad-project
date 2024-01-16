package MathOlympiad.rpg.websocket.domain;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MensagemPerfil {

    private String tipo;

    private String nome;

    private String imagemPerfil;

    private Integer pontos;
}
