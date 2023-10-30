package MathOlympiad.rpg.repository;

import MathOlympiad.rpg.domain.Personagem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonagemRepository extends JpaRepository<Personagem, Long> {
}
