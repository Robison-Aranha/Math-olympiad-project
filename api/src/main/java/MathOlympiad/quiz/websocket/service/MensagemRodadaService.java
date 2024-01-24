package MathOlympiad.quiz.websocket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import MathOlympiad.quiz.domain.Sala;
import MathOlympiad.quiz.service.sala.BuscarSalaService;
import MathOlympiad.quiz.websocket.domain.Mensagem;

@Service
public class MensagemRodadaService {

	@Autowired
	BuscarSalaService buscarSalaService;

	@Autowired
	SimpMessagingTemplate simpMessagingTemplate;

	public void mensagem(Mensagem mensagem) {
		Sala sala = buscarSalaService.porNomeUsuario(mensagem.getNome());

		simpMessagingTemplate.convertAndSend("/chat/" + sala.getWebSocketKey(), mensagem);
	}

}
