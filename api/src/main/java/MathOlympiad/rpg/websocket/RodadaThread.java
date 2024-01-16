package MathOlympiad.rpg.websocket;

import MathOlympiad.rpg.domain.Pergunta;
import MathOlympiad.rpg.domain.Sala;
import MathOlympiad.rpg.repository.SalaRepository;
import MathOlympiad.rpg.service.sala.BuscarSalaService;
import MathOlympiad.rpg.websocket.domain.*;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static MathOlympiad.rpg.websocket.WebSocketUtils.retornarPerfils;
import static MathOlympiad.rpg.websocket.mapper.MensagemPerguntaMapper.toResponse;

@Data
public class RodadaThread implements Runnable{

    private final Integer CONTAGEM_INICIAL = 3;

    private final Integer TEMPO_RESPOSTA_PERGUNTA = 5;

    private String senhaSala;

    public RodadaThread(BuscarSalaService buscarSalaService, SalaRepository salaRepository, SimpMessagingTemplate simpMessagingTemplate, String senhaSala) {
        this.buscarSalaService = buscarSalaService;
        this.salaRepository = salaRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.senhaSala = senhaSala;
    }

    private BuscarSalaService buscarSalaService;

    private SalaRepository salaRepository;

    private SimpMessagingTemplate simpMessagingTemplate;


    @SneakyThrows
    @Override
    public void run() {

        try {

            Sala sala = buscarSalaService.porSenha(senhaSala);
            sala.getRespostasAvancar().clear();
            salaRepository.save(sala);

            Integer contagemIniciar = (int) 10 * CONTAGEM_INICIAL;

            synchronized (this){
                try{
                    cronometro(contagemIniciar, "contagemIniciar", sala.getWebSocketKey());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Sala salaIniciarJogo = buscarSalaService.porSenha(sala.getSenha());
            salaIniciarJogo.setJogoIniciou(true);
            salaRepository.save(salaIniciarJogo);

            MensagemInicioDeJogo mensagemInicioDeJogo = new MensagemInicioDeJogo();
            mensagemInicioDeJogo.setJogoIniciou(true);

            simpMessagingTemplate.convertAndSend("/jogoIniciou/" + sala.getWebSocketKey(), mensagemInicioDeJogo);

            do {

                Pergunta pergunta = sala.getPerguntas().get(0);

                MensagemPergunta rodadaPergunta = toResponse(pergunta);

                simpMessagingTemplate.convertAndSend("/pergunta/" + sala.getWebSocketKey(), rodadaPergunta);

                Thread.sleep(500);

                MensagemRespostaMensagem mensagemRespostaMensagem = new MensagemRespostaMensagem();
                mensagemRespostaMensagem.setResposta(pergunta.getResposta().name());

                simpMessagingTemplate.convertAndSend("/respostaPergunta/" + sala.getWebSocketKey(), mensagemRespostaMensagem);

                Integer tempoRodada = (int) 10 * sala.getTempoRodada();

                synchronized (this){
                    try{
                        cronometro(tempoRodada, "tempoPergunta", sala.getWebSocketKey());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Sala salaAtualizarPontos = buscarSalaService.porWebSocketKey(sala.getWebSocketKey());
                List<MensagemPerfil> perfils = retornarPerfils(salaAtualizarPontos);
                simpMessagingTemplate.convertAndSend("/perfil/" + sala.getWebSocketKey(), perfils);

                Integer tempoRespostaPergunta = (int) 10 * TEMPO_RESPOSTA_PERGUNTA;

                synchronized (this){
                    try{
                        cronometro(tempoRespostaPergunta, "tempoRespostaPergunta", sala.getWebSocketKey());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Sala salaReiniciarPergunta = buscarSalaService.porWebSocketKey(sala.getWebSocketKey());
                salaReiniciarPergunta.getPerguntas().remove(pergunta);
                salaReiniciarPergunta.getRespostasPerguntas().clear();
                salaRepository.save(salaReiniciarPergunta);

                sala.getPerguntas().remove(0);
            } while (!sala.getPerguntas().isEmpty());

            Sala salaFimDeJogo = buscarSalaService.porWebSocketKey(sala.getWebSocketKey());
            salaFimDeJogo.setJogoTerminou(true);
            salaFimDeJogo.setJogoIniciou(false);
            salaRepository.save(salaFimDeJogo);

            MensagemFimDeJogo mensagemFimDeJogo = new MensagemFimDeJogo();
            mensagemFimDeJogo.setFimDeJogo(true);

            simpMessagingTemplate.convertAndSend("/fimDeJogo/" + sala.getWebSocketKey(), mensagemFimDeJogo);

        } catch (JpaObjectRetrievalFailureException e) {
            System.out.println("JpaObjectRetrievalFailureException => " + e.getMessage());
        } catch (ResponseStatusException e) {
            System.out.println("ResponseStatusException => " + e.getMessage());
        }
    }

    public void cronometro(Integer tempoMil, String destino, String webSocketKeySala) throws InterruptedException {

        for (int contador = 1; contador <= 100; contador++) {

            Sala sala = buscarSalaService.porWebSocketKey(webSocketKeySala);

            if (destino == "tempoPergunta") {

                if (sala.getRespostasPerguntas().size() == sala.getParticipantes().size()) {
                    simpMessagingTemplate.convertAndSend("/"+ destino + "/" + webSocketKeySala, 100);
                    break;
                }
            }

            Thread.sleep(tempoMil);

            simpMessagingTemplate.convertAndSend("/"+ destino + "/" + webSocketKeySala, contador);
        }

    }
}
