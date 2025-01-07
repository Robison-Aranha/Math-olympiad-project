package MathOlympiad.quiz.websocket.service;

import static MathOlympiad.quiz.websocket.WebSocketUtils.retornarPerfils;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import MathOlympiad.quiz.domain.Placar;
import MathOlympiad.quiz.domain.RespostaAvancar;
import MathOlympiad.quiz.domain.RespostaPergunta;
import MathOlympiad.quiz.domain.Sala;
import MathOlympiad.quiz.domain.Usuario;
import MathOlympiad.quiz.repository.SalaRepository;
import MathOlympiad.quiz.repository.UsuarioRepository;
import MathOlympiad.quiz.service.sala.BuscarSalaService;
import MathOlympiad.quiz.service.sala.DeleteSalaService;
import MathOlympiad.quiz.service.usuario.BuscarUsuarioService;
import MathOlympiad.quiz.websocket.RodadaThread;
import MathOlympiad.quiz.websocket.domain.Mensagem;
import MathOlympiad.quiz.websocket.domain.MensagemJogo;
import MathOlympiad.quiz.websocket.domain.MensagemPerfil;
import MathOlympiad.quiz.websocket.domain.MensagemRequest;

@Service
public class LeaveSalaService {

	@Autowired
	BuscarSalaService buscarSalaService;

	@Autowired
	BuscarUsuarioService buscarUsuarioService;

	@Autowired
	SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	SalaRepository salaRepository;

	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	DeleteSalaService deleteSalaService;

	@Autowired
	RecomecarRodadaService recomecarRodadaService;

	public void sair(SessionDisconnectEvent event) {
		StompHeaderAccessor wrap = StompHeaderAccessor.wrap(event.getMessage());
		if (wrap.getSessionAttributes().containsKey("user")) {

			MensagemRequest mensagemRequest = (MensagemRequest) wrap.getSessionAttributes().get("user");

			Usuario usuarioARemover = buscarUsuarioService.porNome(mensagemRequest.getNome());
			Sala sala = buscarSalaService.porNomeUsuario(mensagemRequest.getNome());

			sala.removerParticipante(usuarioARemover);

			if (sala.getParticipantes().size() + 1 > 1) {

				if (!sala.getRespostasPerguntas().isEmpty()) {

					List<RespostaPergunta> respostaPerguntaRemover = sala.getRespostasPerguntas().stream()
							.filter(respostaPergunta -> respostaPergunta.getNome().equals(mensagemRequest.getNome()))
							.collect(Collectors.toList());

					if (!respostaPerguntaRemover.isEmpty()) {
						sala.getRespostasPerguntas().remove(respostaPerguntaRemover.get(0));
					}
				}
				if (!sala.getRespostasAvancar().isEmpty()) {

					List<RespostaAvancar> respostaAvancarRemover = sala.getRespostasAvancar().stream()
							.filter(respostaAvancar -> respostaAvancar.getNome().equals(mensagemRequest.getNome()))
							.collect(Collectors.toList());

					if (!respostaAvancarRemover.isEmpty()) {
						sala.getRespostasAvancar().remove(respostaAvancarRemover.get(0));
					}

					simpMessagingTemplate.convertAndSend("/respostaAvancar/" + sala.getWebSocketKey(),
							sala.getRespostasAvancar().size());

					if (!sala.isJogoIniciou()) {
						if (sala.getRespostasAvancar().size() == sala.getParticipantes().size()) {
							MensagemJogo mensagemJogo = new MensagemJogo();
							mensagemJogo.setNome(mensagemRequest.getNome());
							recomecarRodadaService.recomecar(mensagemJogo);
						}
					}
				}
				if (!sala.getPlacares().isEmpty()) {

					List<Placar> placarRemover = sala.getPlacares().stream()
							.filter(placar -> placar.getNome().equals(mensagemRequest.getNome()))
							.collect(Collectors.toList());

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
