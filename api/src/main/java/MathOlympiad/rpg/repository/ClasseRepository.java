package MathOlympiad.rpg.repository;

import MathOlympiad.rpg.domain.Classe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClasseRepository extends JpaRepository<Classe, Long> {

    Optional<Classe> findByNome(String nome);
}
