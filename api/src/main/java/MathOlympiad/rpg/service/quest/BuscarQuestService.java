package MathOlympiad.rpg.service.quest;


import MathOlympiad.rpg.domain.Quest;
import MathOlympiad.rpg.repository.QuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class BuscarQuestService {

    @Autowired
    QuestRepository questRepository;

    public Quest buscar(Long id) {
        return questRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quest não encontrada!"));
    }

}
