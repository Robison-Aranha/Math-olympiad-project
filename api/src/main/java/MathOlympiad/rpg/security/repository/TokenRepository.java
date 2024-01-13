package MathOlympiad.rpg.security.repository;

import MathOlympiad.rpg.security.domain.Token;
import org.antlr.v4.runtime.misc.MultiMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String jwt);
}
