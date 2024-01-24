package MathOlympiad.quiz.websocket.service;

import static MathOlympiad.quiz.websocket.WebSocketUtils.retornarPerfils;
import static MathOlympiad.quiz.websocket.mapper.MensagemPerguntaMapper.toResponse;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import MathOlympiad.quiz.domain.Pergunta;
import MathOlympiad.quiz.domain.Sala;
import MathOlympiad.quiz.service.sala.BuscarSalaService;
import MathOlympiad.quiz.websocket.domain.Mensagem;
import MathOlympiad.quiz.websocket.domain.MensagemFimDeJogo;
import MathOlympiad.quiz.websocket.domain.MensagemInicioDeJogo;
import MathOlympiad.quiz.websocket.domain.MensagemPerfil;
import MathOlympiad.quiz.websocket.domain.MensagemPergunta;
import MathOlympiad.quiz.websocket.domain.MensagemRequest;
import MathOlympiad.quiz.websocket.domain.MensagemRespostaMensagem;

@Service
public class JoinSalaService {

	@Autowired
	BuscarSalaService buscarSalaService;

	@Autowired
	SimpMessagingTemplate simpMessagingTemplate;

	public void entrar(MensagemRequest mensagemRequest, SimpMessageHeaderAccessor accessor) {

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

}
