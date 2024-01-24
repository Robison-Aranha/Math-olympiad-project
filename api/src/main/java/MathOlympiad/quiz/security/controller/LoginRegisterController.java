package MathOlympiad.quiz.security.controller;


import MathOlympiad.quiz.security.controller.request.UsuarioRequest;
import MathOlympiad.quiz.security.controller.response.UsuarioResponse;
import MathOlympiad.quiz.security.service.BuscarUsuarioSecurityAuthService;
import MathOlympiad.quiz.security.service.IncluirUsuarioService;

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
