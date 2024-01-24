package MathOlympiad.quiz.repository;

import MathOlympiad.quiz.domain.Placar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlacarRepository extends JpaRepository<Placar, Long> {
}
