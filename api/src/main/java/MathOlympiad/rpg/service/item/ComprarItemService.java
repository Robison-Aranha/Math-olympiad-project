package MathOlympiad.rpg.service.item;

import MathOlympiad.rpg.controller.request.PersonagemRequest;
import MathOlympiad.rpg.domain.Item;
import MathOlympiad.rpg.domain.Personagem;
import MathOlympiad.rpg.repository.PersonagemRepository;
import MathOlympiad.rpg.service.VerificarPersonagemPertenceAUsuarioService;
import MathOlympiad.rpg.service.personagem.BuscarPersonagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;


@Service
public class ComprarItemService {

    @Autowired
    BuscarPersonagemService buscarPersonagemService;

    @Autowired
    VerificarPersonagemPertenceAUsuarioService verificarPersonagemPertenceAUsuarioService;

    @Autowired
    PersonagemRepository personagemRepository;

    @Autowired
    BuscarItemService buscarItemService;

    @Autowired
    VerificarSeItemEEquipavelService verificarSeItemEEquipavelService;


    public void comprar(Long id, PersonagemRequest request) {

        Personagem personagem = buscarPersonagemService.buscar(request.getPeronsagemId());

        verificarPersonagemPertenceAUsuarioService.verificar(personagem);

        Item item = buscarItemService.buscar(id);

        Integer comparador = personagem.getDinheiro().compareTo(item.getPreco());

        if (comparador == 0 || comparador == 1) {

            if (verificarSeItemEEquipavelService.verificar(item)) {

                Item itemATrocar = personagem.getItens().stream().filter(p -> p.getTipo().toString().equals(item.getTipo().toString())).collect(Collectors.toList()).get(0);

                personagem.getItens().remove(itemATrocar);

            }

            personagem.getItens().add(item);
            personagem.setDinheiro(personagem.getDinheiro().subtract(item.getPreco()));

            personagemRepository.save(personagem);
        }

        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Dinheiro Insuficiente!");

    }

}
