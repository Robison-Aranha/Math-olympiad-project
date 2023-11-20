package MathOlympiad.rpg.domain;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Data
@EqualsAndHashCode(of = "id") @ToString(of = "id")
public class Personagem {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String nome;

    @ManyToOne
    @JoinColumn(name = "id_classe")
    private Classe classe;

    private BigDecimal dinheiro;

    @ManyToOne
    @JoinColumn(name = "id_quest")
    private Quest quest;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;


    @JoinColumn(name = "termino_ultima_quest")
    private LocalDateTime  terminoUltimaQuest;

    @ManyToMany
    @JoinTable(
            name = "personagem_item",
            joinColumns = @JoinColumn(name = "id_personagem"),
            inverseJoinColumns = @JoinColumn(name = "id_item")
    )
    private List<Item> itens = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "personagem_atributo",
            joinColumns = @JoinColumn(name = "id_personagem"),
            inverseJoinColumns = @JoinColumn(name = "id_atributo")
    )
    @MapKey(name = "atributo")
    private Map<String, Atributo> atributos = new HashMap<>();

    private Integer vitorias;

    private Integer derrotas;

}
