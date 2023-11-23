package MathOlympiad.rpg.service.sala;


import MathOlympiad.rpg.controller.request.CriarSalaRequest;
import MathOlympiad.rpg.domain.Pergunta;
import MathOlympiad.rpg.domain.Sala;
import MathOlympiad.rpg.domain.Usuario;
import MathOlympiad.rpg.repository.SalaRepository;
import MathOlympiad.rpg.repository.UsuarioRepository;
import MathOlympiad.rpg.security.service.UsuarioAutenticadoService;
import MathOlympiad.rpg.service.VerificarParametrosService;
import MathOlympiad.rpg.service.pergunta.BuscarPerguntaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CriarSalaService {

    @Autowired
    VerificarParametrosService verificarParametrosService;

    @Autowired
    BuscarPerguntaService buscarPerguntaService;

    @Autowired
    UsuarioAutenticadoService usuarioAutenticadoService;

    @Autowired
    SalaRepository salaRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    private final Integer NUMERO_MAXIMO_DE_RODADAS = 15;

    private final Integer NUMERO_MAXIMO_DE_JOGADORES = 10;

    public void criar(CriarSalaRequest request) {

        if (request.getNumeroRodadas() > NUMERO_MAXIMO_DE_RODADAS && request.getNumeroRodadas() <= 0) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Numero de rodadas invalido!");
        }

        if (request.getNumeroJogadores() > NUMERO_MAXIMO_DE_JOGADORES && request.getNumeroJogadores() <= 0) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Numero de jogadores invalido!");
        }

        Usuario usuario = usuarioAutenticadoService.get();
        verificarParametrosService.verificar(request.getSenha());
        verificarParametrosService.verificar(request.getNome());
        request.getTemas().forEach(t -> verificarParametrosService.verificar(t));

        List<Pergunta> perguntas = buscarPerguntaService.buscarPorTema(request.getTemas(), request.getNumeroRodadas());

        Sala sala = new Sala();
        sala.setPrivado(request.isPrivado());
        sala.setSenha(request.getSenha());
        sala.setNome(request.getNome());
        sala.getParticipantes().add(usuario);
        sala.setNumeroRodadas(request.getNumeroRodadas());
        sala.setNumeroJogadores(request.getNumeroJogadores());
        sala.setPerguntas(perguntas);


        salaRepository.save(sala);
    }
}
