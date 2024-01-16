package MathOlympiad.rpg.security.service;

import MathOlympiad.rpg.domain.Usuario;
import MathOlympiad.rpg.repository.UsuarioRepository;
import MathOlympiad.rpg.security.controller.request.UsuarioRequest;
import MathOlympiad.rpg.security.domain.Permissao;
import MathOlympiad.rpg.service.ConverterImagemParaBase64;
import MathOlympiad.rpg.service.VerificarParametrosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Random;

import static MathOlympiad.rpg.security.mapper.UsuarioMapper.toEntity;


@Service
public class IncluirUsuarioService {

    @Autowired
    VerificarParametrosService verificarParametrosService;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    ConverterImagemParaBase64 converterImagemParaBase64;

    @Autowired
    PasswordEncoder passwordEncoder;

    private final Integer NUMERO_DE_FOTOS = 6;

    public void incluir(UsuarioRequest request) throws IOException {

        verificarParametrosService.verificar(request.getNome());

        if (usuarioRepository.findByNome(request.getNome()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.FOUND, "Nome de usuario ja existe!");
        }

        Usuario usuario = toEntity(request);

        Random random = new Random();
        Integer numerEscolhido = random.nextInt(NUMERO_DE_FOTOS);

        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuario.setImagem(converterImagemParaBase64.converter(numerEscolhido.toString() + ".jpg"));
        usuario.setAtivo(true);

        usuario.adicionarPermissao(Permissao.builder().nome("USUARIO").build());

        usuarioRepository.save(usuario);
    }
}
