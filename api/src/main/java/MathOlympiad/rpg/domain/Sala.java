package MathOlympiad.rpg.domain;


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
public class Sala {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String nome;

    private Integer numeroRodadas;

    private Integer tempoRodada;

    private Integer numeroJogadores;

    private boolean privado;

    private String senha;

    @OneToMany(mappedBy = "salaAParticipar")
    private List<Usuario> participantes = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "sala_pergunta",
            joinColumns = @JoinColumn(name = "id_sala"),
            inverseJoinColumns = @JoinColumn(name = "id_pergunta")
    )
    private List<Pergunta> perguntas = new ArrayList<>();


    @OneToMany(mappedBy = "sala")
    private List<Placar> placares = new ArrayList<>();

}
