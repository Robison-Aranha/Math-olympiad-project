package MathOlympiad.rpg.service.item;


import MathOlympiad.rpg.controller.request.PersonagemRequest;
import MathOlympiad.rpg.domain.Item;
import MathOlympiad.rpg.domain.Personagem;
import MathOlympiad.rpg.repository.PersonagemRepository;
import MathOlympiad.rpg.service.VerificarItemExisteNoIventarioService;
import MathOlympiad.rpg.service.VerificarPersonagemPertenceAUsuarioService;
import MathOlympiad.rpg.service.personagem.BuscarPersonagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsarItemService {

    @Autowired
    BuscarPersonagemService buscarPersonagemService;

    @Autowired
    BuscarItemService buscarItemService;

    @Autowired
    VerificarPersonagemPertenceAUsuarioService verificarPersonagemPertenceAUsuarioService;

    @Autowired
    VerificarItemExisteNoIventarioService verificarItemExisteNoIventarioService;

    @Autowired
    PersonagemRepository personagemRepository;

    @Autowired
    VerificarSeItemEUtilizavelService verificarSeItemEUtilizavelService;


    public void usar(Long id, PersonagemRequest request) {

        Personagem personagem = buscarPersonagemService.buscar(request.getPeronsagemId());

        Item item = buscarItemService.buscar(id);

        verificarSeItemEUtilizavelService.verificar(item);

        verificarPersonagemPertenceAUsuarioService.verificar(personagem);

        verificarItemExisteNoIventarioService.verificar(item, personagem);

        personagem.getItens().remove(item);

        personagemRepository.save(personagem);

    }

}
