package MathOlympiad.rpg.repository;

import MathOlympiad.rpg.domain.Sala;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SalaRepository extends JpaRepository<Sala, Long> {

    Optional<Sala> findBySenha(String senha);

    Optional<Sala> findByNome(String nome);


    @Query(value = "select * from sala where (nome like :nome%)", nativeQuery = true)
    List<Sala> findSalas(String nome);

    Optional<Sala> findByWebSocketKey(String webSocketKey);
}
