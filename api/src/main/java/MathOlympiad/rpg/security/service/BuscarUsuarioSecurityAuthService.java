package MathOlympiad.rpg.security.service;

import MathOlympiad.rpg.domain.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuscarUsuarioSecurityAuthService {

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticadoService;


    public void buscar() {
        Usuario usuarioAutenticado = usuarioAutenticadoService.get();
    }
}
