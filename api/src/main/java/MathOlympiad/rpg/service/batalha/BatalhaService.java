package MathOlympiad.rpg.service.batalha;


import MathOlympiad.rpg.controller.request.BatalhaRequest;
import MathOlympiad.rpg.controller.response.ListarLogsBatalhaResponse;
import MathOlympiad.rpg.domain.Item;
import MathOlympiad.rpg.domain.Personagem;
import MathOlympiad.rpg.service.VerificarItemExisteNoIventarioService;
import MathOlympiad.rpg.service.VerificarPersonagemPertenceAUsuarioService;
import MathOlympiad.rpg.service.item.BuscarItemService;
import MathOlympiad.rpg.service.personagem.BuscarPersonagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BatalhaService {

    @Autowired
    BuscarPersonagemService buscarPersonagemService;

    @Autowired
    VerificarPersonagemPertenceAUsuarioService verificarPersonagemPertenceAUsuarioService;

    @Autowired
    BuscarItemService buscarItemService;

    @Autowired
    VerificarItemExisteNoIventarioService verificarItemExisteNoIventarioService;


    public ListarLogsBatalhaResponse batalhar(BatalhaRequest request) {

        Personagem personagemUsuario = buscarPersonagemService.buscar(request.getPersonagemUsuario());

        verificarPersonagemPertenceAUsuarioService.verificar(personagemUsuario);

        Personagem personagemAsversario = buscarPersonagemService.buscar(request.getPersonagemAdversario());

        List<Item> lista = new ArrayList<>();

        if (!request.getItensAUsar().isEmpty()) {

            for (Long itemId : request.getItensAUsar()) {

                Item item = buscarItemService.buscar(itemId);

                verificarItemExisteNoIventarioService.verificar(item, personagemUsuario);

                lista.add(item);

            }

        }




    }
}
