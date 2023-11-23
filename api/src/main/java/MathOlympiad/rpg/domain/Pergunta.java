package MathOlympiad.rpg.domain;


import MathOlympiad.rpg.enumerator.RespostaPergunta;
import MathOlympiad.rpg.enumerator.TemaPergunta;
import MathOlympiad.rpg.enumerator.TipoPergunta;
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
    @Column(columnDefinition = "tipo")
    @Type(PostgreSQLEnumType.class)
    private TipoPergunta tipo;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "tema")
    @Type(PostgreSQLEnumType.class)
    private TemaPergunta tema;
}
