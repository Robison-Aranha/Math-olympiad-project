package MathOlympiad.quiz.websocket;

import MathOlympiad.quiz.websocket.domain.*;
import MathOlympiad.quiz.websocket.service.JoinSalaService;
import MathOlympiad.quiz.websocket.service.LeaveSalaService;
import MathOlympiad.quiz.websocket.service.MensagemRodadaService;
import MathOlympiad.quiz.websocket.service.RecomecarRodadaService;
import MathOlympiad.quiz.websocket.service.RespostaRodadaService;
import MathOlympiad.quiz.websocket.service.RodadaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

@RestController
public class WebSocketController {

	@Autowired
	JoinSalaService joinSalaService;

	@Autowired
	RodadaService rodadaService;

	@Autowired
	RecomecarRodadaService recomecarRodadaService;

	@Autowired
	RespostaRodadaService respostaRodadaService;

	@Autowired
	MensagemRodadaService mensagemRodadaService;

	@Autowired
	LeaveSalaService leaveSalaService;

	@MessageMapping("/join")
	public void joinChat(@Payload MensagemRequest mensagemRequest, SimpMessageHeaderAccessor accessor)
			throws IOException {
		joinSalaService.entrar(mensagemRequest, accessor);
	}

	@MessageMapping("/rodada")
	public void proximoRodada(MensagemJogo mensagemJogo) throws InterruptedException {
		rodadaService.rodada(mensagemJogo);
	}

	@MessageMapping("/recomecar")
	public void recomecar(MensagemJogo mensagemJogo) throws InterruptedException {
		recomecarRodadaService.recomecar(mensagemJogo);
	}

	@MessageMapping("/resposta")
	public void resposta(MensagemResposta mensagemResposta) throws InterruptedException {
		respostaRodadaService.resposta(mensagemResposta);
	}

	@MessageMapping("/message")
	public void message(@Payload Mensagem mensagem) {
		mensagemRodadaService.mensagem(mensagem);
	}

	@EventListener
	public void disconnect(SessionDisconnectEvent event)
			throws InterruptedException, ExecutionException, URISyntaxException {
		leaveSalaService.sair(event);
	}

}
