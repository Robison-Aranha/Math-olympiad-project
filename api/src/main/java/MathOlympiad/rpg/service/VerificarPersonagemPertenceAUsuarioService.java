package MathOlympiad.rpg.service;


import MathOlympiad.rpg.domain.Personagem;
import MathOlympiad.rpg.domain.Usuario;
import MathOlympiad.rpg.security.service.UsuarioAutenticadoService;
import MathOlympiad.rpg.service.personagem.BuscarPersonagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class VerificarPersonagemPertenceAUsuarioService {

    @Autowired
    UsuarioAutenticadoService usuarioAutenticadoService;

    @Autowired
    BuscarPersonagemService buscarPersonagemService;

    public void verificar(Personagem personagem) {

        Usuario usuario = usuarioAutenticadoService.get();

        if (!usuario.getPersonagens().contains(personagem)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Personagem não pertence a este usuario!");
        }
    }

}
