package MathOlympiad.rpg.domain;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    private Integer tempo;

    @ManyToMany
    @JoinTable(
            name = "quest_item",
            joinColumns = @JoinColumn(name = "id_quest"),
            inverseJoinColumns = @JoinColumn(name = "id_item")
    )
    private List<Item> itens = new ArrayList<>();


    @OneToMany(mappedBy = "quest")
    private List<Personagem> participantes = new ArrayList<>();

}
