package MathOlympiad.rpg.service.personagem;


import MathOlympiad.rpg.controller.request.PersonagemRequest;
import MathOlympiad.rpg.controller.response.item.ItemResponse;
import MathOlympiad.rpg.controller.response.personagem.ListarItensIventarioResponse;
import MathOlympiad.rpg.domain.Item;
import MathOlympiad.rpg.domain.Personagem;
import MathOlympiad.rpg.mapper.ItemMapper;
import MathOlympiad.rpg.service.VerificarPersonagemPertenceAUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class ListarInventarioService {

    @Autowired
    BuscarPersonagemService buscarPersonagemService;

    @Autowired
    VerificarPersonagemPertenceAUsuarioService verificarPersonagemPertenceAUsuarioService;


    public ListarItensIventarioResponse listar(PersonagemRequest request) {

        Personagem personagem = buscarPersonagemService.buscar(request.getPersonagemId());

        verificarPersonagemPertenceAUsuarioService.verificar(personagem);

        ListarItensIventarioResponse lista = new ListarItensIventarioResponse();

        for (Item item : personagem.getItens()) {

            ItemResponse itemResponse = ItemMapper.toResponse(item);

            if (!lista.getItens().containsValue(itemResponse)) {

                Integer quantidade = personagem.getItens().stream().filter(p -> item == p).collect(Collectors.toList()).size();

                lista.getItens().put(quantidade, itemResponse);
            }
        }

        return lista;
    }
}
