package MathOlympiad.rpg.service.item;

import MathOlympiad.rpg.domain.Item;
import MathOlympiad.rpg.domain.Usuario;
import MathOlympiad.rpg.repository.UsuarioRepository;
import MathOlympiad.rpg.security.service.UsuarioAutenticadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class ComprarItemService {

    @Autowired
    UsuarioAutenticadoService usuarioAutenticadoService;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    BuscarItemService buscarItemService;


    public void comprar(Long id) {

        Usuario usuario = usuarioAutenticadoService.get();

        Item item = buscarItemService.buscar(id);

        Integer comparador = usuario.getDinheiro().compareTo(item.getPreco());

        if (comparador == 0 || comparador == 1) {

            usuario.getItens().add(item);
            usuario.setDinheiro(usuario.getDinheiro().subtract(item.getPreco()));

            usuarioRepository.save(usuario);
        }

        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Dinheiro Insuficiente!");
    }

}
