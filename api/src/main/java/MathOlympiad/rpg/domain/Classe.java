package MathOlympiad.rpg.domain;

import MathOlympiad.rpg.enumerator.Cla;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Type;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jakarta.persistence.GenerationType.IDENTITY;


@Entity
@Data
@EqualsAndHashCode(of = "id") @ToString(of = "id")
public class Classe {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String imagem;
    private String descricao;
    private String nome;

    @ManyToMany
    @JoinTable(
            name = "classe_atributo",
            joinColumns = @JoinColumn(name = "id_classe"),
            inverseJoinColumns = @JoinColumn(name = "id_atributo")
    )
    @MapKey(name = "atributo")
    private Map<String, Atributo> atributos = new HashMap<>();


    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "cla")
    @Type(PostgreSQLEnumType.class)
    private Cla cla;

    @OneToMany(mappedBy = "classe")
    private List<Personagem> personagems = new ArrayList<>();

}
