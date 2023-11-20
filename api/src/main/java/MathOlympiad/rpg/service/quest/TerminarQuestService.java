package MathOlympiad.rpg.service.quest;


import MathOlympiad.rpg.controller.request.PersonagemRequest;
import MathOlympiad.rpg.domain.Personagem;
import MathOlympiad.rpg.repository.PersonagemRepository;
import MathOlympiad.rpg.service.VerificarPersonagemPertenceAUsuarioService;
import MathOlympiad.rpg.service.personagem.BuscarPersonagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class TerminarQuestService {

    @Autowired
    BuscarPersonagemService buscarPersonagemService;

    @Autowired
    VerificarPersonagemPertenceAUsuarioService verificarPersonagemPertenceAUsuarioService;

    @Autowired
    PersonagemRepository personagemRepository;

    public void terminar(PersonagemRequest request) {

        Personagem personagem = buscarPersonagemService.buscar(request.getPersonagemId());

        verificarPersonagemPertenceAUsuarioService.verificar(personagem);

        LocalDateTime dataAtual = LocalDateTime.now();

        Integer comparadorDatas = personagem.getTerminoUltimaQuest().compareTo(dataAtual);

        if (comparadorDatas < 0) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "O personagem ainda não terminou a quest!");
        }

        personagem.setQuest(null);
        personagem.setTerminoUltimaQuest(null);

        personagemRepository.save(personagem);
    }

}
