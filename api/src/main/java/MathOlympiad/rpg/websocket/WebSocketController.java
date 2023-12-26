package MathOlympiad.rpg.websocket;

import MathOlympiad.rpg.domain.*;
import MathOlympiad.rpg.repository.*;
import MathOlympiad.rpg.service.pergunta.BuscarPerguntaService;
import MathOlympiad.rpg.websocket.domain.*;
import MathOlympiad.rpg.service.usuario.BuscarUsuarioService;
import MathOlympiad.rpg.service.sala.BuscarSalaService;
import MathOlympiad.rpg.service.sala.DeleteSalaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static MathOlympiad.rpg.websocket.mapper.MensagemPerfilsMapper.toResponseJoin;
import static MathOlympiad.rpg.websocket.mapper.MensagemPerguntaMapper.toResponse;

@RestController
public class WebSocketController {

    private final Integer DIVISOR_PONTOS = 30;

    private final Integer VALOR_PONTOS = 10;


    @Autowired
    BuscarSalaService buscarSalaService;

    @Autowired
    BuscarUsuarioService buscarUsuarioService;

    @Autowired
    BuscarPerguntaService buscarPerguntaService;

    @Autowired
    DeleteSalaService deleteSalaService;

    @Autowired
    SalaRepository salaRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    PlacarRepository placarRepository;

    @Autowired
    RespostaAvancarRepository respostaAvancarRepository;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;


    @MessageMapping("/join")
    public void joinChat(@Payload MensagemRequest mensagemRequest, SimpMessageHeaderAccessor accessor) throws IOException {
        accessor.getSessionAttributes().put("user", mensagemRequest);

        String senhaSala = buscarSalaService.porNomeUsuario(mensagemRequest.getNome()).getSenha();

        Mensagem mensagemChat = new Mensagem();
        mensagemChat.setContent(mensagemRequest.getNome() + " entrou na sala!");
        mensagemChat.setNome("Sala");

        List<MensagemPerfil> perfils = retornarPerfils(mensagemRequest.getNome());

        simpMessagingTemplate.convertAndSend("/perfil/" + senhaSala, perfils);
        simpMessagingTemplate.convertAndSend("/chat/" + senhaSala, mensagemChat);
    }

    @MessageMapping("/rodada")
    public void proximoRodada(MensagemJogo mensagemJogo) throws InterruptedException {

        Sala sala = buscarSalaService.porNomeUsuario(mensagemJogo.getNome());

        RespostaAvancar respostaAvancar = new RespostaAvancar();
        respostaAvancar.setNome(mensagemJogo.getNome());
        respostaAvancar.setSala(sala);

        respostaAvancarRepository.save(respostaAvancar);

        simpMessagingTemplate.convertAndSend("/respostaAvancar/" + sala.getSenha(), "");

        if (sala.getRespostasAvancar().size() + 1 == sala.getParticipantes().size()) {

            for (int i = 3; i >= 0; i--) {

                Thread.sleep(1000);

                simpMessagingTemplate.convertAndSend("/contagemIniciar/" + sala.getSenha(), i);
            }

            sala.setJogoIniciou(true);
            salaRepository.save(sala);

            for ( Pergunta pergunta : sala.getPerguntas()) {

                MensagemPergunta rodadaPergunta = toResponse(pergunta);

                simpMessagingTemplate.convertAndSend("/pergunta/" + sala.getSenha(), rodadaPergunta);

                sala.getPerguntas().remove(pergunta);

                salaRepository.save(sala);

                for (int contador = sala.getTempoRodada(); contador >= 0; contador--) {

                    Sala salaVerificação;

                    try {
                        salaVerificação = buscarSalaService.porSenha(sala.getSenha());
                    } catch (ResponseStatusException e) {
                        return;
                    }

                    if (salaVerificação.getRespostasPerguntas().size() == salaVerificação.getParticipantes().size()) {
                        break;
                    }

                    Thread.sleep(1000);

                    simpMessagingTemplate.convertAndSend("/tempoPergunta/" + sala.getSenha(), contador);
                }

                Sala salaReiniciarPerguntas = buscarSalaService.porSenha(sala.getSenha());
                sala.setRespostasPerguntas(new ArrayList<>());
                salaRepository.save(salaReiniciarPerguntas);
            }

            Sala salaFimDeJogo = buscarSalaService.porSenha(sala.getSenha());
            salaFimDeJogo.setJogoTerminou(true);
            salaFimDeJogo.setJogoIniciou(false);
            salaRepository.save(salaFimDeJogo);

            simpMessagingTemplate.convertAndSend("/fim-rodadas/" + sala.getSenha(), "fim");
        }
    }

    @MessageMapping("/recomecar")
    public void recomecar(MensagemJogo mensagemJogo) throws InterruptedException {

        Sala sala = buscarSalaService.porNomeUsuario(mensagemJogo.getNome());

        RespostaAvancar respostaAvancar = new RespostaAvancar();
        respostaAvancar.setNome(mensagemJogo.getNome());
        respostaAvancar.setSala(sala);
        respostaAvancarRepository.save(respostaAvancar);

        simpMessagingTemplate.convertAndSend("/reiniciar/" + sala.getSenha(), "recomecar");

        if (sala.getRespostasAvancar().size() + 1 == sala.getParticipantes().size()) {
            List<Pergunta> perguntas = buscarPerguntaService.buscarPorTema(sala.getTemas().stream().map(t -> t.getTema()).collect(Collectors.toList()), sala.getNumeroRodadas());
            sala.setPerguntas(perguntas);
            salaRepository.save(sala);

            proximoRodada(mensagemJogo);
        }
    }

    @MessageMapping("/resposta")
    public void resposta(MensagemResposta mensagemResposta) {

        Sala sala = buscarSalaService.porNomeUsuario(mensagemResposta.getNome());

        Pergunta pergunta = sala.getPerguntas().get(sala.getPerguntas().size());

        RespostaPergunta resposta = new RespostaPergunta();
        resposta.setNome(mensagemResposta.getNome());

        sala.getRespostasPerguntas().add(resposta);

        Placar placarUsuario = sala.getPlacares().stream().filter(p -> p.getNome().equals(mensagemResposta.getNome())).collect(Collectors.toList()).get(0);

        if (mensagemResposta.getResposta().equals(pergunta.getResposta())) {

            placarUsuario.setPontos((mensagemResposta.getContagem() / DIVISOR_PONTOS) * VALOR_PONTOS);

        }

        placarRepository.save(placarUsuario);

        List<MensagemPerfil> perfils = retornarPerfils(mensagemResposta.getNome());

        simpMessagingTemplate.convertAndSend("/perfil/" + sala.getSenha(), perfils);
    }


    @MessageMapping("/message")
    public void message(@Payload Mensagem mensagem) {

        Sala sala = buscarSalaService.porNomeUsuario(mensagem.getNome());

        simpMessagingTemplate.convertAndSend("/chat/" + sala.getSenha(), mensagem);
    }

    @EventListener
    public void disconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor wrap = StompHeaderAccessor.wrap(event.getMessage());
        if (wrap.getSessionAttributes().containsKey("user")) {

            MensagemRequest mensagemRequest = (MensagemRequest) wrap.getSessionAttributes().get("user");

            Mensagem mensagem = new Mensagem();
            mensagem.setNome("Sala");
            mensagem.setContent(mensagemRequest.getNome() + " saiu da sala!");

            Usuario usuarioARemover = buscarUsuarioService.porNome(mensagemRequest.getNome());
            Sala sala = buscarSalaService.porNomeUsuario(mensagemRequest.getNome());

            if (sala.getParticipantes().size() > 1) {

                sala.setRespostasPerguntas(sala.getRespostasPerguntas().stream().filter(r -> r.getNome() != mensagemRequest.getNome()).collect(Collectors.toList()));
                sala.setRespostasAvancar(sala.getRespostasAvancar().stream().filter(r -> r.getNome() != mensagemRequest.getNome()).collect(Collectors.toList()));
                sala.setPlacares(sala.getPlacares().stream().filter(p -> p.getNome() != mensagemRequest.getNome()).collect(Collectors.toList()));

                salaRepository.save(sala);

                usuarioARemover.setSalaAParticipar(null);

                usuarioRepository.save(usuarioARemover);

                List<MensagemPerfil> perfils = retornarPerfils(mensagemRequest.getNome());

                simpMessagingTemplate.convertAndSend("/chat/" + sala.getSenha(), mensagem);
                simpMessagingTemplate.convertAndSend("/perfil/" + sala.getSenha(), perfils);

            } else {
                deleteSalaService.deletar(sala.getId());
            }
        }
    }


    public List<MensagemPerfil> retornarPerfils(String nomeUsuario) {

        Sala sala = buscarSalaService.porNomeUsuario(nomeUsuario);

        List<MensagemPerfil> perfils = new ArrayList<>();

        sala.getParticipantes().stream().forEach(perfil -> {

            Placar placar = sala.getPlacares().stream().filter(p -> p.getNome().equals(perfil.getNome())).collect(Collectors.toList()).get(0);

            MensagemPerfil mensagemPerfil = toResponseJoin(perfil);
            mensagemPerfil.setPontos(placar.getPontos());

            perfils.add(mensagemPerfil);
        });

        perfils.sort(Comparator.comparing(MensagemPerfil::getPontos));
        Collections.reverse(perfils);

        return perfils;
    }

}
