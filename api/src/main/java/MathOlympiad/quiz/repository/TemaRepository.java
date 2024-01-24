package MathOlympiad.quiz.repository;

import MathOlympiad.quiz.domain.Tema;
import MathOlympiad.quiz.enumerator.TemaPergunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TemaRepository extends JpaRepository<Tema, Long> {

   @Query(value = "select t from Tema t where t.tema in :temas")
   Optional<List<Tema>> findByTema(List<TemaPergunta> temas);
}
