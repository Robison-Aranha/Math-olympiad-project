package MathOlympiad.rpg.repository;

import MathOlympiad.rpg.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
