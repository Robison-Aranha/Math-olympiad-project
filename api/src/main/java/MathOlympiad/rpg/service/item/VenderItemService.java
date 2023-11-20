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

import java.math.BigDecimal;

@Service
public class VenderItemService {

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

    public void vender(Long id, PersonagemRequest request) {

        Personagem personagem = buscarPersonagemService.buscar(request.getPersonagemId());

        Item item = buscarItemService.buscar(id);

        verificarPersonagemPertenceAUsuarioService.verificar(personagem);

        verificarItemExisteNoIventarioService.verificar(item, personagem);

        personagem.getItens().remove(item);

        personagem.setDinheiro(item.getPreco().divide(BigDecimal.valueOf(2)));

        personagemRepository.save(personagem);
    }

}
