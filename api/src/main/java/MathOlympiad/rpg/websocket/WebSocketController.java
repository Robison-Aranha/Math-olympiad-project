package MathOlympiad.rpg.websocket;

import MathOlympiad.rpg.domain.Sala;
import MathOlympiad.rpg.domain.Usuario;
import MathOlympiad.rpg.repository.SalaRepository;
import MathOlympiad.rpg.repository.UsuarioRepository;
import MathOlympiad.rpg.websocket.domain.MensagemRequest;
import MathOlympiad.rpg.websocket.enumerator.TipoMensagemPerfil;
import MathOlympiad.rpg.websocket.service.BuscarUsuarioService;
import MathOlympiad.rpg.service.ConverterImagemParaBase64;
import MathOlympiad.rpg.websocket.service.BuscarSalaService;
import MathOlympiad.rpg.websocket.service.DeleteSalaService;
import MathOlympiad.rpg.websocket.domain.Mensagem;
import MathOlympiad.rpg.websocket.domain.MensagemPerfil;
import MathOlympiad.rpg.websocket.service.BuscarImagemUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.io.IOException;

@RestController
public class WebSocketController {


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
    ConverterImagemParaBase64 converterImagemParaBase64;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/join")
    public void join(@Payload MensagemRequest mensagemRequest, SimpMessageHeaderAccessor accessor) throws IOException {

        String imagemUsuario = converterImagemParaBase64.converter(buscarImagemUsuarioService.porNome(mensagemRequest.getNome()));

        MensagemPerfil mensagemPerfil = new MensagemPerfil();
        mensagemPerfil.setImagemPerfil(imagemUsuario);
        mensagemPerfil.setNome(mensagemRequest.getNome());

        Mensagem mensagemChat = new Mensagem();
        mensagemChat.setContent(mensagemRequest.getNome() + " entrou na sala!");
        mensagemChat.setNome(mensagemRequest.getNome());


        accessor.getSessionAttributes().put("user", mensagemRequest);

        simpMessagingTemplate.convertAndSend("/chat/" + mensagemRequest.getChave(), mensagemChat);
        simpMessagingTemplate.convertAndSend("/perfils/" + mensagemRequest.getChave(), mensagemPerfil);
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
            mensagemPerfil.setTipo(TipoMensagemPerfil.LEFT);
            mensagemPerfil.setImagemPerfil(null);

            if (sala.getParticipantes().size() == 1) {
                deleteSalaService.deletar(sala.getId());
            } else {
                simpMessagingTemplate.convertAndSend("/chat/" + sala.getSenha(), mensagem);
                simpMessagingTemplate.convertAndSend("/perfils/" + sala.getSenha());
            }
            
            salaRepository.save(sala);
        }
    }

}
