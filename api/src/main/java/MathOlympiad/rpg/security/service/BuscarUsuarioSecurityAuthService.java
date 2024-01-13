package MathOlympiad.rpg.security.service;

import MathOlympiad.rpg.domain.Usuario;
import MathOlympiad.rpg.repository.UsuarioRepository;
import MathOlympiad.rpg.security.controller.request.UsuarioRequest;
import MathOlympiad.rpg.security.controller.response.UsuarioResponse;
import MathOlympiad.rpg.security.jwt.JwtService;
import MathOlympiad.rpg.security.repository.TokenRepository;
import MathOlympiad.rpg.service.VerificarParametrosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import static MathOlympiad.rpg.security.mapper.UsuarioMapper.toResponseToken;

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
