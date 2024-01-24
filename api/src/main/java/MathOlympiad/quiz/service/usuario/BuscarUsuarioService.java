package MathOlympiad.quiz.service.usuario;


import MathOlympiad.quiz.domain.Usuario;
import MathOlympiad.quiz.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class BuscarUsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    public Usuario porNome(String nome) {
        return usuarioRepository.findByNome(nome).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum usuario para este nome!"));
    }

}
