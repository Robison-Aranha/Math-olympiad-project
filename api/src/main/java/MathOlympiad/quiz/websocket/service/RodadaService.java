package MathOlympiad.quiz.websocket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import MathOlympiad.quiz.domain.RespostaAvancar;
import MathOlympiad.quiz.domain.Sala;
import MathOlympiad.quiz.repository.RespostaAvancarRepository;
import MathOlympiad.quiz.repository.SalaRepository;
import MathOlympiad.quiz.service.sala.BuscarSalaService;
import MathOlympiad.quiz.websocket.RodadaThread;
import MathOlympiad.quiz.websocket.domain.MensagemJogo;

@Service
public class RodadaService {

	@Autowired
	BuscarSalaService buscarSalaService;

	@Autowired
	SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	SalaRepository salaRepository;

	@Autowired
	RespostaAvancarRepository respostaAvancarRepository;

	public void rodada(MensagemJogo mensagemJogo) {
		Sala sala = buscarSalaService.porNomeUsuario(mensagemJogo.getNome());

		RespostaAvancar respostaAvancar = new RespostaAvancar();
		respostaAvancar.setNome(mensagemJogo.getNome());
		respostaAvancar.setSala(sala);

		respostaAvancarRepository.save(respostaAvancar);

		simpMessagingTemplate.convertAndSend("/respostaAvancar/" + sala.getWebSocketKey(),
				sala.getRespostasAvancar().size() + 1);

		if (sala.getRespostasAvancar().size() + 1 == sala.getParticipantes().size()) {

			RodadaThread rodadaThread = new RodadaThread(buscarSalaService, salaRepository, simpMessagingTemplate,
					sala.getSenha());
			Thread thread = new Thread(rodadaThread);
			thread.start();
		}
	}

}
