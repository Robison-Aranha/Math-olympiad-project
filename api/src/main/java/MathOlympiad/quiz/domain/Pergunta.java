package MathOlympiad.quiz.domain;


import MathOlympiad.quiz.enumerator.RespostaPergunta;
import MathOlympiad.quiz.enumerator.TipoPergunta;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Type;



import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Data
@EqualsAndHashCode(of = "id") @ToString(of = "id")
public class Pergunta {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String link;

    private String questao;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "resposta")
    @Type(PostgreSQLEnumType.class)
    private RespostaPergunta resposta;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "tipo_pergunta")
    @Type(PostgreSQLEnumType.class)
    private TipoPergunta tipo;

    @ManyToOne
    @JoinColumn(name = "id_tema")
    private Tema tema;
}
