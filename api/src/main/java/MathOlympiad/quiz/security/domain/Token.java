package MathOlympiad.quiz.security.domain;

import MathOlympiad.quiz.domain.Usuario;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(of = "id") @ToString(of = "id")
public class Token {

    public Token(String token, Usuario usuarioToken) {
        this.token = token;
        this.usuarioToken = usuarioToken;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String token;

    private Boolean IsExpired;

    @OneToOne
    @JoinColumn(name = "id_usuario")
    Usuario usuarioToken;

}