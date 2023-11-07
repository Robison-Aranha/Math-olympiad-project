package MathOlympiad.rpg.service;


import MathOlympiad.rpg.domain.Item;
import MathOlympiad.rpg.domain.Personagem;
import MathOlympiad.rpg.security.service.UsuarioAutenticadoService;
import MathOlympiad.rpg.service.item.BuscarItemService;
import MathOlympiad.rpg.service.personagem.BuscarPersonagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class VerificarItemExisteNoIventarioService {

    @Autowired
    BuscarPersonagemService buscarPersonagemService;

    @Autowired
    BuscarItemService buscarItemService;

    public void verificar(Item item, Personagem personagem) {

        if (!personagem.getItens().contains(item)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item não encontrado no iventario!");
        }
    }

}
