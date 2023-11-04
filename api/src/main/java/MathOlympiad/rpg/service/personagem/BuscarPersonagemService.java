package MathOlympiad.rpg.service.personagem;


import MathOlympiad.rpg.domain.Personagem;
import MathOlympiad.rpg.repository.PersonagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class BuscarPersonagemService {

    @Autowired
    PersonagemRepository personagemRepository;

    public Personagem buscar(Long id) {

        return personagemRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Personagem não encontrado!"));

    }

    public Optional<Personagem> buscarNome(String nome) {
        return personagemRepository.findByNome(nome);
    }

}
