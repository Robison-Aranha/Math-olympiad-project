package MathOlympiad.quiz.repository;

import MathOlympiad.quiz.domain.Pergunta;
import MathOlympiad.quiz.enumerator.TemaPergunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PerguntaRepository extends JpaRepository<Pergunta, Long> {

    @Query(value = "select p from Pergunta p where p.tema.tema in :temas order by random() limit :quantidade")
    Optional<List<Pergunta>> findPerguntaTemas(List<TemaPergunta> temas, Integer quantidade);
}
