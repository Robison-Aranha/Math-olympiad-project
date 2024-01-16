package MathOlympiad.rpg.security.controller;


import MathOlympiad.rpg.security.controller.request.UsuarioRequest;
import MathOlympiad.rpg.security.controller.response.UsuarioResponse;
import MathOlympiad.rpg.security.service.BuscarUsuarioSecurityAuthService;
import MathOlympiad.rpg.security.service.IncluirUsuarioService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;

@RestController
@RequestMapping()
public class LoginRegisterController {

    @Autowired
    BuscarUsuarioSecurityAuthService buscarUsuarioService;

    @Autowired
    IncluirUsuarioService incluirUsuarioService;

    @PostMapping("/login")
    public UsuarioResponse login(@RequestBody UsuarioRequest request) {
        return buscarUsuarioService.buscar(request);
    }

    @PostMapping("/register")
    public void incluir(@Valid @RequestBody UsuarioRequest request) throws IOException {

        incluirUsuarioService.incluir(request);
    }
}
