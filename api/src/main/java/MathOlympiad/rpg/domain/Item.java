package MathOlympiad.rpg.domain;

import MathOlympiad.rpg.enumerator.ItemTipos;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;

import static jakarta.persistence.GenerationType.IDENTITY;


@Entity
@Data
@EqualsAndHashCode(of = "id") @ToString(of = "id")
public class Item {


    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String imagem;

    private BigDecimal preco;

    private String nome;

    private Double valor;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ITEM_TIPOS")
    @Type(PostgreSQLEnumType.class)

    private ItemTipos tipo;

    private String descricao;

}
