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

    private boolean jogoIniciou;

    private boolean jogoTerminou;

    private Integer numeroRodadas;

    private Integer tempoRodada;

    private Integer numeroJogadores;

    private boolean privado;

    private String senha;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "salaAParticipar")
    private List<Usuario> participantes = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sala")
    private List<RespostaPergunta> respostasPerguntas = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sala")
    private List<RespostaAvancar> respostasAvancar = new ArrayList<>();


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "sala_pergunta",
            joinColumns = @JoinColumn(name = "id_sala"),
            inverseJoinColumns = @JoinColumn(name = "id_pergunta")
    )
    private List<Pergunta> perguntas = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "sala_tema",
            joinColumns = @JoinColumn(name = "id_sala"),
            inverseJoinColumns = @JoinColumn(name = "id_tema")
    )
    private List<Tema> temas = new ArrayList<>();


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sala", cascade = CascadeType.REMOVE)
    private List<Placar> placares = new ArrayList<>();

}
