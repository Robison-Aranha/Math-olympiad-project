package MathOlympiad.rpg.security.service;

import MathOlympiad.rpg.domain.Usuario;
import MathOlympiad.rpg.security.controller.response.UsuarioResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static MathOlympiad.rpg.security.mapper.UsuarioMapper.toResponse;

@Service
public class BuscarUsuarioSecurityAuthService {

    @Autowired
    UsuarioAutenticadoService usuarioAutenticadoService;


    public UsuarioResponse buscar() {
        Usuario usuarioAutenticado = usuarioAutenticadoService.get();
        return toResponse(usuarioAutenticado);
    }
}
