package MathOlympiad.rpg.service.item;


import MathOlympiad.rpg.controller.request.PersonagemRequest;
import MathOlympiad.rpg.domain.Atributo;
import MathOlympiad.rpg.domain.Item;
import MathOlympiad.rpg.domain.Personagem;
import MathOlympiad.rpg.repository.AtributoRepository;
import MathOlympiad.rpg.repository.PersonagemRepository;
import MathOlympiad.rpg.service.VerificarItemExisteNoIventarioService;
import MathOlympiad.rpg.service.VerificarPersonagemPertenceAUsuarioService;
import MathOlympiad.rpg.service.personagem.BuscarPersonagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

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
    AtributoRepository atributoRepository;

    @Autowired
    PersonagemRepository personagemRepository;

    @Autowired
    VerificarSeItemEUtilizavelService verificarSeItemEUtilizavelService;


    public void usar(Long id, PersonagemRequest request) {

        Personagem personagem = buscarPersonagemService.buscar(request.getPersonagemId());

        Item item = buscarItemService.buscar(id);

        verificarSeItemEUtilizavelService.verificar(item);

        verificarPersonagemPertenceAUsuarioService.verificar(personagem);

        verificarItemExisteNoIventarioService.verificar(item, personagem);

        Atributo atributo = personagem.getAtributos().get(item.getTipo().name());
        atributo.setValor(atributo.getValor() + item.getValor());

        atributoRepository.save(atributo);

        personagem.getItens().remove(item);

        personagemRepository.save(personagem);

    }

}
