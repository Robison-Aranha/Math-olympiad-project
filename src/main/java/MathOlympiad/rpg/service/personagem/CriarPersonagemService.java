package MathOlympiad.rpg.service.personagem;

import MathOlympiad.rpg.controller.request.CriarPersonagemRequest;
import MathOlympiad.rpg.domain.Classe;
import MathOlympiad.rpg.domain.Personagem;
import MathOlympiad.rpg.repository.PersonagemRepository;
import MathOlympiad.rpg.service.VerificarParametrosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CriarPersonagemService {

    @Autowired
    VerificarParametrosService verificarParametrosService;

    @Autowired
    BuscarClasseService buscarClasseService;

    @Autowired
    PersonagemRepository personagemRepository;

    public void criar(CriarPersonagemRequest request) {

        verificarParametrosService.verificar(request.getNome());
        verificarParametrosService.verificar(request.getClasse());

        Classe classe = buscarClasseService.buscar(request.getNome());

        Personagem personagem = new Personagem();

        personagem.setClasse(classe);
        personagem.setNome(request.getNome());


        personagemRepository.save(personagem);
    }
}
