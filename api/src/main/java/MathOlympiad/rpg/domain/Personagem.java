package MathOlympiad.rpg.domain;


import MathOlympiad.rpg.enumerator.Cla;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Type;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Data
@EqualsAndHashCode(of = "id") @ToString(of = "id")
public class Personagem {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String nome;

    @OneToOne
    @JoinColumn(name = "id_classe")
    private Classe classe;


    @ManyToOne
    @JoinColumn(name = "id_quest")
    private Quest quest;


    @JoinColumn(name = "termino_ultima_quest")
    private LocalDateTime  terminoUltimaQuest;

}
