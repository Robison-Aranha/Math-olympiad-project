package MathOlympiad.rpg.domain;

import MathOlympiad.rpg.enumerator.TemaPergunta;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id") @ToString(of = "id")
public class Tema {


    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "tema_pergunta")
    @Type(PostgreSQLEnumType.class)
    private TemaPergunta tema;
}
