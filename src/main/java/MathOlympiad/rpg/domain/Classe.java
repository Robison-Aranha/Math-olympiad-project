package MathOlympiad.rpg.domain;

import MathOlympiad.rpg.enumerator.Cla;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


import static jakarta.persistence.GenerationType.IDENTITY;


@Entity
@Data
@EqualsAndHashCode(of = "id") @ToString(of = "id")
public class Classe {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String imagem;

    private Integer forca;

    private Integer fama;

    private Integer destreza;

    private Integer vida;

    private Integer nivel;

    private String descricao;

    @Enumerated(EnumType.STRING)
    private Cla cla;

}