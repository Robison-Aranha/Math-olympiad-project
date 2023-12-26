package MathOlympiad.rpg.repository;

import MathOlympiad.rpg.domain.Tema;
import MathOlympiad.rpg.enumerator.TemaPergunta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TemaRepository extends JpaRepository<Tema, Long> {
   Optional<List<Tema>> findAllByTemaIn(List<TemaPergunta> temas);
}
