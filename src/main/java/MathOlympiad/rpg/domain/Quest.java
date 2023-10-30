package MathOlympiad.rpg.domain;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


import java.util.HashMap;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Data
@EqualsAndHashCode(of = "id") @ToString(of = "id")
public class Quest {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String imagem;

    private String descricao;

    @ManyToMany
    @JoinTable(
            name = "quest_itens",
            joinColumns = @JoinColumn(name = "id_quest"),
            inverseJoinColumns = @JoinColumn(name = "id_item")
    )
    private HashMap<Integer, Item> itens = new HashMap<>();

}
