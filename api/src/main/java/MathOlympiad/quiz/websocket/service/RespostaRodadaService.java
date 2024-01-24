package MathOlympiad.quiz.websocket.service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import MathOlympiad.quiz.domain.Pergunta;
import MathOlympiad.quiz.domain.Placar;
import MathOlympiad.quiz.domain.RespostaPergunta;
import MathOlympiad.quiz.domain.Sala;
import MathOlympiad.quiz.repository.PlacarRepository;
import MathOlympiad.quiz.repository.RespostaPerguntaRepository;
import MathOlympiad.quiz.service.sala.BuscarSalaService;
import MathOlympiad.quiz.websocket.domain.MensagemResposta;

@Service
public class RespostaRodadaService {
	
	private final Integer DIVISOR_PONTOS = 30;

    private final Integer VALOR_PONTOS = 10;
	
	@Autowired
	BuscarSalaService buscarSalaService;
	
	@Autowired
	PlacarRepository placarRepository;

	@Autowired
	RespostaPerguntaRepository respostaPerguntaRepository;
	
	
	public void resposta(MensagemResposta mensagemResposta) {
		
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

}
