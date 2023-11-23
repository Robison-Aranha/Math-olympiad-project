package MathOlympiad.rpg.repository;

import MathOlympiad.rpg.domain.Pergunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PerguntaRepository extends JpaRepository<Pergunta, Long> {

    @Query(nativeQuery = true, value = "select * from pergunta where tema in :temas order by random() limit :quantidade")
    Optional<List<Pergunta>> findByTema(List<String> temas, Integer quantidade);
}
