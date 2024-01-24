package MathOlympiad.quiz.service.sala;


import MathOlympiad.quiz.controller.request.EntrarSalaPrivadaRequest;
import MathOlympiad.quiz.controller.request.EntrarSalaPublicaRequest;
import MathOlympiad.quiz.controller.response.SalaResponse;
import MathOlympiad.quiz.domain.Placar;
import MathOlympiad.quiz.domain.Sala;
import MathOlympiad.quiz.domain.Usuario;
import MathOlympiad.quiz.repository.SalaRepository;
import MathOlympiad.quiz.security.service.UsuarioAutenticadoService;
import MathOlympiad.quiz.service.VerificarParametrosService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EntrarSalaService {


    @Autowired
    UsuarioAutenticadoService usuarioAutenticadoService;

    @Autowired
    BuscarSalaService buscarSalaService;

    @Autowired
    VerificarParametrosService verificarParametrosService;

    @Autowired
    SalaRepository salaRepository;

    public SalaResponse entrarPrivada(EntrarSalaPrivadaRequest request) {

        verificarParametrosService.verificar(request.getSenha());

        String senha = DigestUtils.md5Hex(request.getSenha());

        Usuario usuario = usuarioAutenticadoService.get();
        Sala sala = buscarSalaService.porSenha(senha);

        if (sala.getNumeroJogadores() == sala.getParticipantes().size()) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Sala ja esta lotada!");
        }

        Placar placar = new Placar();
        placar.setNome(usuario.getNome());
        placar.setSala(sala);
        placar.setPontos(0);

        sala.adicionarPlacar(placar);

        sala.adicionarParticipante(usuario);

        salaRepository.save(sala);

        SalaResponse salaResponse = new SalaResponse();
        salaResponse.setNumeroJogadores(sala.getNumeroJogadores());
        salaResponse.setWebSocketKey(sala.getWebSocketKey());

        return salaResponse;
    }

    public SalaResponse entrarPublica(EntrarSalaPublicaRequest request) {

        Usuario usuario = usuarioAutenticadoService.get();

        Sala sala = buscarSalaService.porId(request.getSalaId());

        if (sala.getNumeroJogadores() == sala.getParticipantes().size()) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Sala ja esta lotada!");
        }

        Placar placar = new Placar();
        placar.setNome(usuario.getNome());
        placar.setSala(sala);
        placar.setPontos(0);

        sala.adicionarPlacar(placar);

        sala.adicionarParticipante(usuario);

        salaRepository.save(sala);

        SalaResponse salaResponse = new SalaResponse();
        salaResponse.setNumeroJogadores(sala.getNumeroJogadores());
        salaResponse.setWebSocketKey(sala.getWebSocketKey());

        return salaResponse;
    }


}
