package MathOlympiad.rpg.mapper;


import MathOlympiad.rpg.domain.Tema;
import MathOlympiad.rpg.enumerator.TemaPergunta;

public class TemaMapper {

    public static Tema toEntity(String tema) {
        return Tema.builder()
                .tema(TemaPergunta.valueOf(tema))
                .build();
    }

}
