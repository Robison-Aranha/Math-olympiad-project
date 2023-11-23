package MathOlympiad.rpg.websocket.service;


import MathOlympiad.rpg.domain.Usuario;
import MathOlympiad.rpg.repository.UsuarioRepository;
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
