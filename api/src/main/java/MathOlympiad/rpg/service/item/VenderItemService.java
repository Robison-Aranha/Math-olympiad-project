package MathOlympiad.rpg.service.item;

import MathOlympiad.rpg.domain.Item;
import MathOlympiad.rpg.domain.Usuario;
import MathOlympiad.rpg.repository.UsuarioRepository;
import MathOlympiad.rpg.security.service.UsuarioAutenticadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class VenderItemService {

    @Autowired
    UsuarioAutenticadoService usuarioAutenticadoService;

    @Autowired
    BuscarItemService buscarItemService;

    @Autowired
    UsuarioRepository usuarioRepository;

    public void vender(Long id) {

        Usuario usuario = usuarioAutenticadoService.get();

        Item item = buscarItemService.buscar(id);

        usuario.getItens().remove(item);

        usuario.setDinheiro(item.getPreco().divide(BigDecimal.valueOf(2)));

        usuarioRepository.save(usuario);
    }

}
