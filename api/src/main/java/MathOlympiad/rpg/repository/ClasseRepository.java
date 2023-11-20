package MathOlympiad.rpg.repository;

import MathOlympiad.rpg.domain.Classe;
import MathOlympiad.rpg.enumerator.Cla;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClasseRepository extends JpaRepository<Classe, Long> {

    Optional<Classe> findByNome(String nome);

    Optional<List<Classe>> findAllByCla(Cla cla);
}
