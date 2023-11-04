package MathOlympiad.rpg.service.personagem;

import MathOlympiad.rpg.controller.response.ListarPersonagensResponse;
import MathOlympiad.rpg.domain.Usuario;
import MathOlympiad.rpg.mapper.ListarPersonagensMapper;
import MathOlympiad.rpg.security.service.UsuarioAutenticadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ListarPersonagensService {

    @Autowired
    UsuarioAutenticadoService usuarioAutenticadoService;

    public Page<ListarPersonagensResponse> listar() {

        Usuario usuario = usuarioAutenticadoService.get();

        List<ListarPersonagensResponse> personagens = usuario.getPersonagens().stream().map(ListarPersonagensMapper::toResponse).collect(Collectors.toList());


        return new PageImpl<>(personagens);
    }



}
