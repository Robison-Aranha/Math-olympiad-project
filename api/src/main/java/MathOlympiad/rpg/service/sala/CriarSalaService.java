package MathOlympiad.rpg.service.sala;


import MathOlympiad.rpg.controller.request.CriarSalaRequest;
import MathOlympiad.rpg.controller.response.SalaResponse;
import MathOlympiad.rpg.domain.*;
import MathOlympiad.rpg.enumerator.TemaPergunta;
import MathOlympiad.rpg.mapper.TemaMapper;
import MathOlympiad.rpg.repository.PlacarRepository;
import MathOlympiad.rpg.repository.SalaRepository;
import MathOlympiad.rpg.repository.UsuarioRepository;
import MathOlympiad.rpg.security.service.UsuarioAutenticadoService;
import MathOlympiad.rpg.service.VerificarParametrosService;
import MathOlympiad.rpg.service.pergunta.BuscarPerguntaService;
import MathOlympiad.rpg.service.tema.BuscarTemaService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CriarSalaService {

    @Autowired
    VerificarParametrosService verificarParametrosService;

    @Autowired
    BuscarPerguntaService buscarPerguntaService;

    @Autowired
    BuscarTemaService buscarTemaService;

    @Autowired
    UsuarioAutenticadoService usuarioAutenticadoService;

    @Autowired
    SalaRepository salaRepository;

    @Value("${application.security.password.secret-key}")
    private String SECRET_KEY;

    private final Integer NUMERO_MAXIMO_DE_RODADAS = 15;

    private final Integer NUMERO_MAXIMO_DE_JOGADORES = 10;

    public SalaResponse criar(CriarSalaRequest request) {

        if (request.getNumeroRodadas() > NUMERO_MAXIMO_DE_RODADAS && request.getNumeroRodadas() <= 0) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Numero de rodadas invalido!");
        }

        if (request.getNumeroJogadores() > NUMERO_MAXIMO_DE_JOGADORES && request.getNumeroJogadores() <= 0) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Numero de jogadores invalido!");
        }


        Usuario usuario = usuarioAutenticadoService.get();

        verificarParametrosService.verificar(request.getNome());

        if (!request.getSenha().isEmpty()) {
            verificarParametrosService.verificar(request.getSenha());
        }
        request.getTemas().stream().forEach(t -> verificarParametrosService.verificar(t));

        List<Pergunta> perguntas = buscarPerguntaService.buscarPorTema(request.getTemas().stream().map(t -> TemaPergunta.valueOf(t)).collect(Collectors.toList()), request.getNumeroRodadas());

        Sala sala = new Sala();

        String senha = null;
        String webSocketKey;

        if (!request.getSenha().isEmpty()) {
            senha = DigestUtils.md5Hex(request.getSenha());
            webSocketKey = DigestUtils.md5Hex(request.getSenha() + System.currentTimeMillis() + SECRET_KEY);
        } else {
            webSocketKey = DigestUtils.md5Hex(request.getNome() + System.currentTimeMillis() + SECRET_KEY);
        }


        List<Tema> temasSala = buscarTemaService.porTema(request.getTemas().stream().map(t -> TemaPergunta.valueOf(t)).collect(Collectors.toList()));

        sala.setNome(request.getNome());
        sala.setSenha(senha);
        sala.setWebSocketKey(webSocketKey);
        sala.setNumeroRodadas(request.getNumeroRodadas());
        sala.setNumeroJogadores(request.getNumeroJogadores());
        sala.setPerguntas(perguntas);
        sala.setPrivado(request.isPrivado());
        sala.setTempoRodada(request.getTempoRodada());
        sala.setTemas(temasSala);
        sala.adicionarParticipante(usuario);

        Placar placar = new Placar();
        placar.setSala(sala);
        placar.setPontos(0);
        placar.setNome(usuario.getNome());

        sala.adicionarPlacar(placar);

        salaRepository.save(sala);

        SalaResponse salaResponse = new SalaResponse();
        salaResponse.setNumeroJogadores(sala.getNumeroJogadores());
        salaResponse.setWebSocketKey(webSocketKey);


        return salaResponse;
    }
}
