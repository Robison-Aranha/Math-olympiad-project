package MathOlympiad.quiz.mapper;


import MathOlympiad.quiz.domain.Tema;
import MathOlympiad.quiz.enumerator.TemaPergunta;

public class TemaMapper {

    public static Tema toEntity(String tema) {
        return Tema.builder()
                .tema(TemaPergunta.valueOf(tema))
                .build();
    }

}
