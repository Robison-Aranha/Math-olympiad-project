package MathOlympiad.quiz.websocket.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import MathOlympiad.quiz.domain.Pergunta;
import MathOlympiad.quiz.domain.RespostaAvancar;
import MathOlympiad.quiz.domain.Sala;
import MathOlympiad.quiz.repository.RespostaAvancarRepository;
import MathOlympiad.quiz.repository.SalaRepository;
import MathOlympiad.quiz.service.pergunta.BuscarPerguntaService;
import MathOlympiad.quiz.service.sala.BuscarSalaService;
import MathOlympiad.quiz.websocket.RodadaThread;
import MathOlympiad.quiz.websocket.domain.MensagemJogo;

@Service
public class RecomecarRodadaService {
	
	@Autowired
	BuscarSalaService buscarSalaService;
	
	@Autowired
	SimpMessagingTemplate simpMessagingTemplate;
	
	@Autowired
	BuscarPerguntaService buscarPerguntaService;
	
	@Autowired
	RespostaAvancarRepository respostaAvancarRepository;
	
	@Autowired
	SalaRepository salaRepository;
	
	
	
	public void recomecar(MensagemJogo mensagemJogo) {
		
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

}
