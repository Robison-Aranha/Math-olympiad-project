package MathOlympiad.quiz.domain;

import MathOlympiad.quiz.security.domain.Permissao;
import MathOlympiad.quiz.security.domain.Token;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Data
@EqualsAndHashCode(of = "id") @ToString(of = "id")
public class Usuario {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nome;

    private String imagem;

    private String senha;

    private boolean ativo;

    @OneToOne(mappedBy = "usuarioToken")
    private Token token;


    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Permissao> permissoes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "id_sala")
    private Sala salaAParticipar;

    public void adicionarPermissao(Permissao permissao) {
        this.permissoes.add(permissao);
        permissao.setUsuario(this);
    }
}
