package MathOlympiad.rpg.service.sala;


import MathOlympiad.rpg.controller.request.EntrarSalaPrivadaRequest;
import MathOlympiad.rpg.controller.request.EntrarSalaPublicaRequest;
import MathOlympiad.rpg.controller.response.SalaResponse;
import MathOlympiad.rpg.domain.Placar;
import MathOlympiad.rpg.domain.Sala;
import MathOlympiad.rpg.domain.Usuario;
import MathOlympiad.rpg.repository.PlacarRepository;
import MathOlympiad.rpg.repository.SalaRepository;
import MathOlympiad.rpg.repository.UsuarioRepository;
import MathOlympiad.rpg.security.service.UsuarioAutenticadoService;
import MathOlympiad.rpg.service.VerificarParametrosService;
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
