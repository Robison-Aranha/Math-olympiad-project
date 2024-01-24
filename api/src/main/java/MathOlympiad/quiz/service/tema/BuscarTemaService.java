package MathOlympiad.quiz.service.tema;


import MathOlympiad.quiz.domain.Tema;
import MathOlympiad.quiz.enumerator.TemaPergunta;
import MathOlympiad.quiz.repository.TemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BuscarTemaService {

    @Autowired
    TemaRepository temaRepository;

    public List<Tema> porTema(List<TemaPergunta> temas) {
        return temaRepository.findByTema(temas).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum Tema encontrado para as correspondencias!"));
    }

}
