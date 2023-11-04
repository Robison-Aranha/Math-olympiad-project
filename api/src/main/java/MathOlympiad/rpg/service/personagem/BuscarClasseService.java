package MathOlympiad.rpg.service.personagem;

import MathOlympiad.rpg.domain.Classe;
import MathOlympiad.rpg.repository.ClasseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class BuscarClasseService {

    @Autowired
    ClasseRepository classeRepository;


    public Classe buscar(String nome) {

        return classeRepository.findByNome(nome).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classe não encontrada!"));

    }

}
