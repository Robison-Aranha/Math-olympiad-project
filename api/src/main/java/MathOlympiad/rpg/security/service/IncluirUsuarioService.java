package MathOlympiad.rpg.security.service;

import MathOlympiad.rpg.domain.Usuario;
import MathOlympiad.rpg.repository.UsuarioRepository;
import MathOlympiad.rpg.security.controller.request.UsuarioRequest;
import MathOlympiad.rpg.security.domain.Permissao;
import MathOlympiad.rpg.service.ConverterImagemParaBase64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Random;

import static MathOlympiad.rpg.security.mapper.UsuarioMapper.toEntity;
import static MathOlympiad.rpg.security.mapper.UsuarioMapper.toResponse;

@Service
public class IncluirUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    ConverterImagemParaBase64 converterImagemParaBase64;

    @Autowired
    private PasswordEncoder passwordEncoder;


    private Integer NUMERO_DE_FOTOS = 6;

    public void incluir(UsuarioRequest request) throws IOException {

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
