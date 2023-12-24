package MathOlympiad.rpg.websocket.service;


import MathOlympiad.rpg.repository.SalaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteSalaService {

    @Autowired
    SalaRepository salaRepository;

    public void deletar(Long id) {
        salaRepository.deleteById(id);
    }

}
