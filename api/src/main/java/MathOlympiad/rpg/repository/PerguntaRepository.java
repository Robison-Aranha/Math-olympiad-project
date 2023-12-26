package MathOlympiad.rpg.repository;

import MathOlympiad.rpg.domain.Pergunta;
import MathOlympiad.rpg.enumerator.TemaPergunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PerguntaRepository extends JpaRepository<Pergunta, Long> {

    @Query(value = "select p from Pergunta p where p.tema in :temas order by random() limit :quantidade")
    Optional<List<Pergunta>> findPerguntaTemas(List<TemaPergunta> temas, Integer quantidade);
}
