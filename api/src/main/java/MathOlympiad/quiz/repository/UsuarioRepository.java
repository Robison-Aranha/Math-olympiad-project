package MathOlympiad.quiz.repository;

import MathOlympiad.quiz.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByNome(String nome);


    Optional<Usuario> findByNomeAndSenha(String nome, String senha);
}
