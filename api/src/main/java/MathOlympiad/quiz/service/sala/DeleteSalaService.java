package MathOlympiad.quiz.service.sala;


import MathOlympiad.quiz.repository.SalaRepository;
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
