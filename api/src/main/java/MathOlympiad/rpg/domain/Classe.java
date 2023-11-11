package MathOlympiad.rpg.domain;

import MathOlympiad.rpg.enumerator.Cla;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


import java.util.ArrayList;
import java.util.List;

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

    @ManyToMany
    @JoinTable(
            name = "classe_atributo",
            joinColumns = @JoinColumn(name = "id_classe"),
            inverseJoinColumns = @JoinColumn(name = "id_atributo")
    )
    private List<Atributo> atributos = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Cla cla;

}
