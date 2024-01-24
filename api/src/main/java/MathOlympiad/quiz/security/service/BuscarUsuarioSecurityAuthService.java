package MathOlympiad.quiz.security.service;

import MathOlympiad.quiz.domain.Usuario;
import MathOlympiad.quiz.repository.UsuarioRepository;
import MathOlympiad.quiz.security.controller.request.UsuarioRequest;
import MathOlympiad.quiz.security.controller.response.UsuarioResponse;
import MathOlympiad.quiz.security.jwt.JwtService;
import MathOlympiad.quiz.service.VerificarParametrosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import static MathOlympiad.quiz.security.mapper.UsuarioMapper.toResponseToken;

@Service
public class BuscarUsuarioSecurityAuthService {


    @Autowired
    JwtService jwtService;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    VerificarParametrosService verificarParametrosService;

    public UsuarioResponse buscar(UsuarioRequest request) {

        verificarParametrosService.verificar(request.getNome());
        verificarParametrosService.verificar(request.getSenha());

        Usuario usuario = usuarioRepository.findByNome(request.getNome()).get();

        if (!passwordEncoder.matches(request.getSenha(), usuario.getSenha())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        jwtService.deleteToken(usuario);

        String newToken = jwtService.generateToken(usuario);

        jwtService.saveToken(usuario, newToken);

        return toResponseToken(usuario, newToken);
    }
}
