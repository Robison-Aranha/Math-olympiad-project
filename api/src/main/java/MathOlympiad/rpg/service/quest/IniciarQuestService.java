package MathOlympiad.rpg.service.quest;

import MathOlympiad.rpg.controller.request.IniciarQuestRequest;
import MathOlympiad.rpg.domain.Personagem;
import MathOlympiad.rpg.domain.Quest;
import MathOlympiad.rpg.domain.Usuario;
import MathOlympiad.rpg.repository.PersonagemRepository;
import MathOlympiad.rpg.security.service.UsuarioAutenticadoService;
import MathOlympiad.rpg.service.personagem.BuscarPersonagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Period;

@Service
public class IniciarQuestService {

    @Autowired
    UsuarioAutenticadoService usuarioAutenticadoService;

    @Autowired
    BuscarQuestService buscarQuestService;

    @Autowired
    BuscarPersonagemService buscarPersonagemService;

    @Autowired
    PersonagemRepository personagemRepository;


    public void iniciar(Long id, IniciarQuestRequest request) {

        Usuario usuario = usuarioAutenticadoService.get();

        Quest quest = buscarQuestService.buscar(id);

        Personagem personagem = buscarPersonagemService.buscar(request.getPersonagemId());

        if (usuario.getPersonagens().contains(personagem)) {

            personagem.setQuest(quest);

            LocalDateTime dataAtual = LocalDateTime.now().plusMinutes(quest.getTempo());

            personagem.setTerminoUltimaQuest(dataAtual);

            personagemRepository.save(personagem);
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Personagem não encontrado para usuario!");
    }

}
