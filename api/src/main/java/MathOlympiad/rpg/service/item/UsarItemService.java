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
public class UsarItemService {

    @Autowired
    UsuarioAutenticadoService usuarioAutenticadoService;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    BuscarItemService buscarItemService;

    public void usar(Long id) {

        Usuario usuario = usuarioAutenticadoService.get();

        Item item = buscarItemService.buscar(id);

        if (usuario.getItens().contains(item)) {

            usuario.getItens().remove(item);

            usuarioRepository.save(usuario);
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item não encontrado no iventario!");
    }

}
