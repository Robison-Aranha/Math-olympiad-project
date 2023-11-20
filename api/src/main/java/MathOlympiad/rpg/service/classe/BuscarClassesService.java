package MathOlympiad.rpg.service.classe;


import MathOlympiad.rpg.domain.Classe;
import MathOlympiad.rpg.enumerator.Cla;
import MathOlympiad.rpg.repository.ClasseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class BuscarClassesService {


    @Autowired
    ClasseRepository classeRepository;

    public List<Classe> porCla(String cla) {
        return classeRepository.findAllByCla(Cla.valueOf(cla)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhuma classe para este cla!"));
    }

}
