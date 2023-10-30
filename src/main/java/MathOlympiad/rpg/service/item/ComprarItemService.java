package MathOlympiad.rpg.service.item;

import MathOlympiad.rpg.domain.Item;
import MathOlympiad.rpg.domain.Usuario;
import MathOlympiad.rpg.repository.ItemRepository;
import MathOlympiad.rpg.repository.UsuarioRepository;
import MathOlympiad.rpg.security.service.UsuarioAutenticadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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

        usuario.getItens().add(item);
        usuario.setDinheiro(usuario.getDinheiro().subtract(item.getPreco()));

        usuarioRepository.save(usuario);
    }

}
