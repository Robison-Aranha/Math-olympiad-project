package MathOlympiad.rpg.service.personagem;


import MathOlympiad.rpg.domain.Personagem;
import MathOlympiad.rpg.domain.Usuario;
import MathOlympiad.rpg.repository.UsuarioRepository;
import MathOlympiad.rpg.security.service.UsuarioAutenticadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class DeletarPersonagemService {

    @Autowired
    UsuarioAutenticadoService usuarioAutenticadoService;

    @Autowired
    BuscarPersonagemService buscarPersonagemService;

    @Autowired
    UsuarioRepository usuarioRepository;


    public void deletar(Long id) {

        Usuario usuario = usuarioAutenticadoService.get();

        Personagem personagem = buscarPersonagemService.buscar(id);

        if (usuario.getPersonagens().contains(personagem)) {

            usuario.getPersonagens().remove(personagem);

            usuarioRepository.save(usuario);

        }

        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Personagem não encontrado para este usuario!");
    }


}
