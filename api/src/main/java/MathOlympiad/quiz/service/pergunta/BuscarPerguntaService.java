package MathOlympiad.quiz.service.pergunta;


import MathOlympiad.quiz.domain.Pergunta;
import MathOlympiad.quiz.enumerator.TemaPergunta;
import MathOlympiad.quiz.repository.PerguntaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BuscarPerguntaService {


    @Autowired
    PerguntaRepository perguntaRepository;

    public List<Pergunta> buscarPorTema(List<TemaPergunta> temas ,Integer quantidade) {
        return perguntaRepository.findPerguntaTemas(temas, quantidade).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Erro ao retornar Perguntas!"));
    }

}
