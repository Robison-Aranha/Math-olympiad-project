package MathOlympiad.rpg.service.sala;

import MathOlympiad.rpg.controller.response.VerificarHostSalaResponse;
import MathOlympiad.rpg.domain.Usuario;
import MathOlympiad.rpg.security.service.UsuarioAutenticadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificarSeUsuarioEOHostDaSala {

    @Autowired
    UsuarioAutenticadoService usuarioAutenticadoService;

    public VerificarHostSalaResponse verificar() {

        Usuario usuario = usuarioAutenticadoService.get();

        VerificarHostSalaResponse verificarHostSalaResponse = new VerificarHostSalaResponse();
        verificarHostSalaResponse.setUsuarioEOHost(usuario.getSalaAParticipar().getParticipantes().get(0).equals(usuario));

        return verificarHostSalaResponse;
    }
}
