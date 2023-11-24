package MathOlympiad.rpg.websocket;

import MathOlympiad.rpg.domain.Pergunta;
import MathOlympiad.rpg.domain.Placar;
import MathOlympiad.rpg.domain.Sala;
import MathOlympiad.rpg.repository.PlacarRepository;
import MathOlympiad.rpg.repository.SalaRepository;
import MathOlympiad.rpg.repository.UsuarioRepository;
import MathOlympiad.rpg.websocket.domain.*;
import MathOlympiad.rpg.websocket.enumerator.TipoMensagemPerfil;
import MathOlympiad.rpg.websocket.mapper.MensagemPerguntaMapper;
import MathOlympiad.rpg.websocket.service.BuscarUsuarioService;
import MathOlympiad.rpg.service.ConverterImagemParaBase64;
import MathOlympiad.rpg.websocket.service.BuscarSalaService;
import MathOlympiad.rpg.websocket.service.DeleteSalaService;
import MathOlympiad.rpg.websocket.service.BuscarImagemUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static MathOlympiad.rpg.websocket.mapper.MensagemPerguntaMapper.toResponse;

@RestController
public class WebSocketController {

    private final Integer DIVISOR_PONTOS = 30;

    private final Integer VALOR_PONTOS = 10;

    private final String STOMP_URL = "http://localhost:8080/ws";

    @Autowired
    BuscarImagemUsuarioService buscarImagemUsuarioService;

    @Autowired
    BuscarSalaService buscarSalaService;

    @Autowired
    BuscarUsuarioService buscarUsuarioService;

    @Autowired
    DeleteSalaService deleteSalaService;

    @Autowired
    SalaRepository salaRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    PlacarRepository placarRepository;

    @Autowired
    ConverterImagemParaBase64 converterImagemParaBase64;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private WebSocketStompClient stompClient;

    @MessageMapping("/join")
    public void join(@Payload MensagemRequest mensagemRequest, SimpMessageHeaderAccessor accessor) throws IOException {

        String imagemUsuario = converterImagemParaBase64.converter(buscarImagemUsuarioService.porNome(mensagemRequest.getNome()));

        Sala sala = buscarSalaService.porNomeUsuario(mensagemRequest.getNome());

        Placar placar = new Placar();
        placar.setNome(mensagemRequest.getNome());
        placar.setSala(sala);
        placar.setPontos(0);
        sala.getPlacares().add(placar);

        MensagemPerfil mensagemPerfil = new MensagemPerfil();
        mensagemPerfil.setImagemPerfil(imagemUsuario);
        mensagemPerfil.setNome(mensagemRequest.getNome());
        mensagemPerfil.setTipo(TipoMensagemPerfil.JOIN.name());
        mensagemPerfil.setPontos(0);

        Mensagem mensagemChat = new Mensagem();
        mensagemChat.setContent(mensagemRequest.getNome() + " entrou na sala!");
        mensagemChat.setNome(mensagemRequest.getNome());


        accessor.getSessionAttributes().put("user", mensagemRequest);

        simpMessagingTemplate.convertAndSend("/chat/" + sala.getSenha(), mensagemChat);
        simpMessagingTemplate.convertAndSend("/perfils/" + sala.getSenha(), mensagemPerfil);
    }


    @MessageMapping("/rodadas")
    public void proximoRodada(MensagemJogo mensagemJogo) {

        Sala sala = buscarSalaService.porNomeUsuario(mensagemJogo.getNome());

        sala.getPerguntas().stream().forEach(pergunta -> {

            MensagemPergunta rodadaPergunta = toResponse(pergunta);

            simpMessagingTemplate.convertAndSend("/rodada/" + sala.getSenha(), rodadaPergunta);

            for (int i = sala.getTempoRodada(); i < sala.getTempoRodada(); i++) {

                MensagemTempoRodada mensagemTempoRodada = new MensagemTempoRodada();
                mensagemTempoRodada.setTempoRodada(i);

                simpMessagingTemplate.convertAndSend("/tempoRodada/" + sala.getSenha(), mensagemTempoRodada);
            }
        });
    }

    @MessageMapping("/resposta")
    public void resposta(MensagemResposta mensagemResposta) {

        Sala sala = buscarSalaService.porNomeUsuario(mensagemResposta.getNome());

        Pergunta pergunta = sala.getPerguntas().get(mensagemResposta.getRodada());

        Placar placarUsuario = sala.getPlacares().stream().filter(p -> p.getNome().equals(mensagemResposta.getNome())).collect(Collectors.toList()).get(0);

        if (mensagemResposta.getResposta().equals(pergunta.getResposta())) {

            placarUsuario.setPontos((mensagemResposta.getContagem() / DIVISOR_PONTOS) * VALOR_PONTOS);

        }

        MensagemPerfil mensagemPerfil = new MensagemPerfil();
        mensagemPerfil.setPontos(placarUsuario.getPontos());
        mensagemPerfil.setNome(mensagemPerfil.getNome());
        mensagemPerfil.setTipo(TipoMensagemPerfil.GAME.name());

        placarRepository.save(placarUsuario);

        simpMessagingTemplate.convertAndSend("/perfils/" + sala.getSenha(), mensagemPerfil);
    }


    @MessageMapping("/fim-rodada")
    public void fimRodada() {





    }


    @MessageMapping("/message")
    public void message(@Payload Mensagem mensagem) {

        Sala sala = buscarSalaService.porNomeUsuario(mensagem.getNome());

        simpMessagingTemplate.convertAndSend("/sala/" + sala.getSenha(), mensagem);
    }

    @EventListener
    public void disconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor wrap = StompHeaderAccessor.wrap(event.getMessage());
        if (wrap.getSessionAttributes().containsKey("user")) {

            Mensagem mensagem = (Mensagem) wrap.getSessionAttributes().get("user");

            mensagem.setContent(mensagem.getNome() + " saiu da sala!");

            Sala sala = buscarSalaService.porNomeUsuario(mensagem.getNome());
            sala.getParticipantes().remove(buscarUsuarioService.porNome(mensagem.getNome()));

            MensagemPerfil mensagemPerfil = new MensagemPerfil();
            mensagemPerfil.setNome(mensagem.getNome());
            mensagemPerfil.setTipo(TipoMensagemPerfil.LEFT.name());
            mensagemPerfil.setImagemPerfil(null);

            if (sala.getParticipantes().size() == 1) {
                deleteSalaService.deletar(sala.getId());
            } else {
                simpMessagingTemplate.convertAndSend("/chat/" + sala.getSenha(), mensagem);
                simpMessagingTemplate.convertAndSend("/perfils/" + sala.getSenha(), mensagemPerfil);
            }
            
            salaRepository.save(sala);
        }
    }

}
