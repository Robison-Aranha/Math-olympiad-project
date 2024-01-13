package MathOlympiad.rpg.repository;

import MathOlympiad.rpg.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByNome(String nome);


    Optional<Usuario> findByNomeAndSenha(String nome, String senha);
}
