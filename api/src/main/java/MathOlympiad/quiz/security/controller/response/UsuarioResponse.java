package MathOlympiad.quiz.security.controller.response;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UsuarioResponse {

    private Long id;
    private String nome;
    private String token;
    private String imagemPerfil;

}
