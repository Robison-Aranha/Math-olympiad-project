package MathOlympiad.rpg.service.sala;

import MathOlympiad.rpg.domain.Sala;
import MathOlympiad.rpg.repository.SalaRepository;
import MathOlympiad.rpg.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


@Service
public class BuscarSalaService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    SalaRepository salaRepository;

    public Sala porNomeUsuario(String nome) {

        return usuarioRepository.findByNome(nome).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario não encontrado para este nome@")).getSalaAParticipar();

    }

    public Sala porId(Long id) {

        return salaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sala não encontrada!"));

    }

    public Sala porSenha(String senha) {

        return salaRepository.findBySenha(senha).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhuma sala para esta senha!"));

    }

    public Optional<Sala> porNome(String nome) {

        return salaRepository.findByNome(nome);

    }


}
