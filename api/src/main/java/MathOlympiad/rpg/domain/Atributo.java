package MathOlympiad.rpg.domain;


import MathOlympiad.rpg.enumerator.Atributos;
import MathOlympiad.rpg.enumerator.ItemTipos;
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
public class Atributo {


    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ATRIBUTOS")
    @Type(PostgreSQLEnumType.class)
    private Atributos atributo;

    private Double valor;

}
