package MathOlympiad.rpg.websocket;

import MathOlympiad.rpg.domain.*;
import MathOlympiad.rpg.enumerator.TemaPergunta;
import MathOlympiad.rpg.repository.*;
import MathOlympiad.rpg.service.pergunta.BuscarPerguntaService;
import MathOlympiad.rpg.websocket.domain.*;
import MathOlympiad.rpg.service.usuario.BuscarUsuarioService;
import MathOlympiad.rpg.service.sala.BuscarSalaService;
import MathOlympiad.rpg.service.sala.DeleteSalaService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.*;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ExecutionException;

import java.util.stream.Collectors;


import static MathOlympiad.rpg.websocket.WebSocketUtils.retornarPerfils;
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
    PlacarRepository placarRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    RespostaPerguntaRepository respostaPerguntaRepository;

    @Autowired
    RespostaAvancarRepository respostaAvancarRepository;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;




    @MessageMapping("/join")
    public void joinChat(@Payload MensagemRequest mensagemRequest, SimpMessageHeaderAccessor accessor) throws IOException {
        accessor.getSessionAttributes().put("user", mensagemRequest);

        Sala sala = buscarSalaService.porNomeUsuario(mensagemRequest.getNome());

        Mensagem mensagemChat = new Mensagem();
        mensagemChat.setContent(mensagemRequest.getNome() + " entrou na sala!");
        mensagemChat.setNome("Sala");

        List<MensagemPerfil> perfils = retornarPerfils(sala);

        String webSocketKeyPrivada = DigestUtils.md5Hex(sala.getWebSocketKey() + mensagemRequest.getNome());

        simpMessagingTemplate.convertAndSend("/perfil/" + sala.getWebSocketKey(), perfils);
        simpMessagingTemplate.convertAndSend("/chat/" + sala.getWebSocketKey(), mensagemChat);

        if (sala.isJogoIniciou()) {

            Pergunta pergunta = sala.getPerguntas().get(0);

            MensagemPergunta rodadaPergunta = toResponse(pergunta);

            MensagemRespostaMensagem mensagemRespostaMensagem = new MensagemRespostaMensagem();
            mensagemRespostaMensagem.setResposta(pergunta.getResposta().name());

            MensagemInicioDeJogo mensagemInicioDeJogo = new MensagemInicioDeJogo();
            mensagemInicioDeJogo.setJogoIniciou(true);

            simpMessagingTemplate.convertAndSend("/contagemIniciar/" + webSocketKeyPrivada, 1);
            simpMessagingTemplate.convertAndSend("/jogoIniciou/" + webSocketKeyPrivada, mensagemInicioDeJogo);
            simpMessagingTemplate.convertAndSend("/pergunta/" + webSocketKeyPrivada, rodadaPergunta);
            simpMessagingTemplate.convertAndSend("/respostaPergunta/" + webSocketKeyPrivada, mensagemRespostaMensagem);

        } else if (sala.isJogoTerminou()) {

            MensagemFimDeJogo mensagemFimDeJogo = new MensagemFimDeJogo();
            mensagemFimDeJogo.setFimDeJogo(true);
            simpMessagingTemplate.convertAndSend("/fimDeJogo/" + webSocketKeyPrivada, mensagemFimDeJogo);

        }
    }

    @MessageMapping("/rodada")
    public void proximoRodada(MensagemJogo mensagemJogo) throws InterruptedException {

        Sala sala = buscarSalaService.porNomeUsuario(mensagemJogo.getNome());

        RespostaAvancar respostaAvancar = new RespostaAvancar();
        respostaAvancar.setNome(mensagemJogo.getNome());
        respostaAvancar.setSala(sala);

        respostaAvancarRepository.save(respostaAvancar);

        simpMessagingTemplate.convertAndSend("/respostaAvancar/" + sala.getWebSocketKey(), sala.getRespostasAvancar().size() + 1);

        if (sala.getRespostasAvancar().size() + 1 == sala.getParticipantes().size()) {

            RodadaThread rodadaThread = new RodadaThread(buscarSalaService, salaRepository, simpMessagingTemplate, sala.getSenha());
            Thread thread = new Thread(rodadaThread);
            thread.start();
        }

    }

    @MessageMapping("/recomecar")
    public void recomecar(MensagemJogo mensagemJogo) throws InterruptedException {

        Sala sala = buscarSalaService.porNomeUsuario(mensagemJogo.getNome());

        RespostaAvancar respostaAvancar = new RespostaAvancar();
        respostaAvancar.setNome(mensagemJogo.getNome());
        respostaAvancar.setSala(sala);
        respostaAvancarRepository.save(respostaAvancar);

        simpMessagingTemplate.convertAndSend("/respostaAvancar/" + sala.getWebSocketKey(), sala.getRespostasAvancar().size() + 1);

        if (sala.getRespostasAvancar().size() + 1 == sala.getParticipantes().size()) {
            List<Pergunta> perguntas = buscarPerguntaService.buscarPorTema(sala.getTemas().stream().map(t -> t.getTema()).collect(Collectors.toList()), sala.getNumeroRodadas());
            sala.setPerguntas(perguntas);
            sala.setJogoTerminou(false);
            salaRepository.save(sala);

            RodadaThread rodadaThread = new RodadaThread(buscarSalaService, salaRepository, simpMessagingTemplate, sala.getSenha());
            Thread thread = new Thread(rodadaThread);
            thread.start();
        }
    }

    @MessageMapping("/resposta")
    public void resposta(MensagemResposta mensagemResposta) throws InterruptedException {

        Sala sala = buscarSalaService.porNomeUsuario(mensagemResposta.getNome());

        Pergunta pergunta = sala.getPerguntas().get(0);

        boolean respostaPerguntaVerificar = sala.getRespostasPerguntas().stream().filter(p -> p.getNome().equals(mensagemResposta.getNome())).collect(Collectors.toList()).isEmpty();

        if (respostaPerguntaVerificar) {

            RespostaPergunta resposta = new RespostaPergunta();
            resposta.setNome(mensagemResposta.getNome());
            resposta.setSala(sala);

            if (mensagemResposta.getResposta().equals(pergunta.getResposta().name().toString())) {

                Placar placarUsuario = sala.getPlacares().stream().filter(p -> p.getNome().equals(mensagemResposta.getNome())).collect(Collectors.toList()).get(0);

                Integer calculoPontos = (int) ((DIVISOR_PONTOS / mensagemResposta.getContagem()) * VALOR_PONTOS + placarUsuario.getPontos());

                placarUsuario.setPontos(calculoPontos);

                placarRepository.save(placarUsuario);
            }

            respostaPerguntaRepository.save(resposta);

        }

    }


    @MessageMapping("/message")
    public void message(@Payload Mensagem mensagem) {

        Sala sala = buscarSalaService.porNomeUsuario(mensagem.getNome());

        simpMessagingTemplate.convertAndSend("/chat/" + sala.getWebSocketKey(), mensagem);
    }

    @EventListener
    public void disconnect(SessionDisconnectEvent event) throws InterruptedException, ExecutionException, URISyntaxException {
        StompHeaderAccessor wrap = StompHeaderAccessor.wrap(event.getMessage());
        if (wrap.getSessionAttributes().containsKey("user")) {

            MensagemRequest mensagemRequest = (MensagemRequest) wrap.getSessionAttributes().get("user");

            Usuario usuarioARemover = buscarUsuarioService.porNome(mensagemRequest.getNome());
            Sala sala = buscarSalaService.porNomeUsuario(mensagemRequest.getNome());

            sala.removerParticipante(usuarioARemover);

            if (sala.getParticipantes().size() + 1 > 1) {

                if (!sala.getRespostasPerguntas().isEmpty()) {

                    List<RespostaPergunta> respostaPerguntaRemover = sala.getRespostasPerguntas().stream().filter(respostaPergunta -> respostaPergunta.getNome().equals(mensagemRequest.getNome())).collect(Collectors.toList());

                    if (!respostaPerguntaRemover.isEmpty()) {
                        sala.getRespostasPerguntas().remove(respostaPerguntaRemover.get(0));
                    }
                }
                if (!sala.getRespostasAvancar().isEmpty()) {

                    List<RespostaAvancar> respostaAvancarRemover = sala.getRespostasAvancar().stream().filter(respostaAvancar -> respostaAvancar.getNome().equals(mensagemRequest.getNome())).collect(Collectors.toList());

                    if (!respostaAvancarRemover.isEmpty()) {
                        sala.getRespostasAvancar().remove(respostaAvancarRemover.get(0));
                    }

                    simpMessagingTemplate.convertAndSend("/respostaAvancar/" + sala.getWebSocketKey(), sala.getRespostasAvancar().size());

                    if (!sala.isJogoIniciou() && !sala.isJogoTerminou()) {
                        if (sala.getRespostasAvancar().size() == sala.getParticipantes().size()) {

                            RodadaThread rodadaThread = new RodadaThread(buscarSalaService, salaRepository, simpMessagingTemplate, sala.getSenha());
                            Thread thread = new Thread(rodadaThread);
                            thread.start();

                        }
                    }
                }
                if (!sala.getPlacares().isEmpty()) {

                    List<Placar> placarRemover = sala.getPlacares().stream().filter(placar -> placar.getNome().equals(mensagemRequest.getNome())).collect(Collectors.toList());

                    if (!placarRemover.isEmpty()) {
                        sala.getPlacares().remove(placarRemover.get(0));
                    }
                }

                salaRepository.save(sala);
                usuarioRepository.save(usuarioARemover);

                Mensagem mensagem = new Mensagem();
                mensagem.setNome("Sala");
                mensagem.setContent(mensagemRequest.getNome() + " saiu da sala!");

                simpMessagingTemplate.convertAndSend("/chat/" + sala.getWebSocketKey(), mensagem);

                List<MensagemPerfil> perfils = retornarPerfils(sala);
                simpMessagingTemplate.convertAndSend("/perfil/" + sala.getWebSocketKey(), perfils);

            } else {
                usuarioRepository.save(usuarioARemover);
                deleteSalaService.deletar(sala.getId());
            }

        }
    }

}
