package MathOlympiad.rpg.websocket.service;

import MathOlympiad.rpg.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BuscarImagemUsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;


    public String porNome(String nome) {
        return usuarioRepository.findByNome(nome).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum usuario para este nome!")).getImagem();
    }

}