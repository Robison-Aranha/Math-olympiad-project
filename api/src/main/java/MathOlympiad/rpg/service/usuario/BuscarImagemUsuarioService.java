package MathOlympiad.rpg.service.usuario;

import MathOlympiad.rpg.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class BuscarImagemUsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;


    public String porNome(String nome) {
        return usuarioRepository.findByNome(nome).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum usuario para este nome!")).getImagem();
    }

}
