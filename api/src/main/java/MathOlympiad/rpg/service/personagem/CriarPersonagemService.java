package MathOlympiad.rpg.service.personagem;

import MathOlympiad.rpg.controller.request.CriarPersonagemRequest;
import MathOlympiad.rpg.domain.Classe;
import MathOlympiad.rpg.domain.Personagem;
import MathOlympiad.rpg.repository.PersonagemRepository;
import MathOlympiad.rpg.service.VerificarParametrosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class CriarPersonagemService {

    @Autowired
    VerificarParametrosService verificarParametrosService;

    @Autowired
    BuscarClasseService buscarClasseService;

    @Autowired
    BuscarPersonagemService buscarPersonagemService;

    @Autowired
    PersonagemRepository personagemRepository;

    public void criar(CriarPersonagemRequest request) {

        verificarParametrosService.verificar(request.getNome());
        verificarParametrosService.verificar(request.getClasse());

        Classe classe = buscarClasseService.buscar(request.getNome());

        Optional<Personagem> existente = buscarPersonagemService.buscarNome(request.getNome());

        if (existente.isEmpty()) {

            Personagem personagem = new Personagem();
            personagem.setClasse(classe);
            personagem.setNome(request.getNome());


            personagemRepository.save(personagem);
        }

        throw new ResponseStatusException(HttpStatus.CONFLICT, "Este nome de personagem ja esta em uso!");
    }
}
