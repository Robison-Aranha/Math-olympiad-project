package MathOlympiad.rpg.websocket.service;

import MathOlympiad.rpg.domain.Sala;
import MathOlympiad.rpg.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class BuscarSalaService {

    @Autowired
    UsuarioRepository usuarioRepository;

    public Sala porNomeUsuario(String nome) {

        return usuarioRepository.findByNome(nome).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario não encontrado para este nome@")).getSalaAParticipar();

    }


}
