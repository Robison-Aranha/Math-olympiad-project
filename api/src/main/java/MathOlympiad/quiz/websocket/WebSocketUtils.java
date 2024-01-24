package MathOlympiad.quiz.websocket;


import MathOlympiad.quiz.domain.Placar;
import MathOlympiad.quiz.domain.Sala;
import MathOlympiad.quiz.websocket.domain.MensagemPerfil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static MathOlympiad.quiz.websocket.mapper.MensagemPerfilsMapper.toResponseJoin;

@Service
public class WebSocketUtils {

    public static List<MensagemPerfil> retornarPerfils(Sala sala) {

        List<MensagemPerfil> perfils = new ArrayList<>();

        sala.getParticipantes().stream().forEach(perfil -> {

            Placar placar = sala.getPlacares().stream().filter(p -> p.getNome().equals(perfil.getNome())).collect(Collectors.toList()).get(0);

            MensagemPerfil mensagemPerfil = toResponseJoin(perfil);
            mensagemPerfil.setPontos(placar.getPontos());

            perfils.add(mensagemPerfil);
        });

        perfils.sort(Comparator.comparing(MensagemPerfil::getPontos));
        Collections.reverse(perfils);

        return perfils;
    }

}
