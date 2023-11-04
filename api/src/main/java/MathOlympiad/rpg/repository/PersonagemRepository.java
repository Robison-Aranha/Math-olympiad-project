package MathOlympiad.rpg.repository;

import MathOlympiad.rpg.domain.Personagem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonagemRepository extends JpaRepository<Personagem, Long> {
    Optional<Personagem> findByNome(String nome);
}
