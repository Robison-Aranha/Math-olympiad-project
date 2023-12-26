package MathOlympiad.rpg.security.controller;

import MathOlympiad.rpg.repository.UsuarioRepository;
import MathOlympiad.rpg.security.controller.request.UsuarioRequest;
import MathOlympiad.rpg.security.controller.response.UsuarioResponse;
import MathOlympiad.rpg.security.service.BuscarUsuarioSecurityAuthService;
import MathOlympiad.rpg.security.service.IncluirUsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
@RequestMapping()
public class LoginRegisterController {

    @Autowired
    private BuscarUsuarioSecurityAuthService buscarUsuarioService;

    @Autowired
    private IncluirUsuarioService incluirUsuarioService;
    @Autowired
    UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public UsuarioResponse login() {
        return buscarUsuarioService.buscar();
    }

    @PostMapping("/register")
    public void incluir(@Valid @RequestBody UsuarioRequest request) throws IOException {

        if (usuarioRepository.findByNome(request.getNome()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.FOUND, "Nome ja esta sendo usado!!");
        }

        incluirUsuarioService.incluir(request);
    }
}
