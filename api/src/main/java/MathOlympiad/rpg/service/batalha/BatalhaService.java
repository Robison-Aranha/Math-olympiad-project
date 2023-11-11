package MathOlympiad.rpg.service.batalha;


import MathOlympiad.rpg.controller.request.BatalhaRequest;
import MathOlympiad.rpg.controller.response.BatalhaResponse;
import MathOlympiad.rpg.controller.response.ListarLogsBatalhaResponse;
import MathOlympiad.rpg.domain.Item;
import MathOlympiad.rpg.domain.Personagem;
import MathOlympiad.rpg.service.VerificarItemExisteNoIventarioService;
import MathOlympiad.rpg.service.VerificarPersonagemPertenceAUsuarioService;
import MathOlympiad.rpg.service.item.BuscarItemService;
import MathOlympiad.rpg.service.personagem.BuscarPersonagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class BatalhaService {

    @Autowired
    BuscarPersonagemService buscarPersonagemService;

    @Autowired
    VerificarPersonagemPertenceAUsuarioService verificarPersonagemPertenceAUsuarioService;

    @Autowired
    BuscarItemService buscarItemService;

    @Autowired
    VerificarItemExisteNoIventarioService verificarItemExisteNoIventarioService;


    public ListarLogsBatalhaResponse batalhar(BatalhaRequest request) {

        Personagem personagemUsuario = buscarPersonagemService.buscar(request.getPersonagemUsuario());

        verificarPersonagemPertenceAUsuarioService.verificar(personagemUsuario);

        Personagem personagemAdversario = buscarPersonagemService.buscar(request.getPersonagemAdversario());

        ListarLogsBatalhaResponse listaBatalhaResponse = new ListarLogsBatalhaResponse();

        ArrayList<BatalhaResponse> logs = new ArrayList<>();

        List<Personagem> listaAAtacar = new ArrayList<>();
        List<Double> listaVida = new ArrayList<>();

        if (personagemUsuario.getAtributo().getNivel() > personagemAdversario.getAtributo().getNivel()) {
            listaAAtacar.add(personagemUsuario);
            listaAAtacar.add(personagemAdversario);
            listaVida.add(personagemUsuario.getAtributo().getVida());
            listaVida.add(personagemAdversario.getAtributo().getVida());
        } else if (personagemUsuario.getAtributo().getNivel() == personagemAdversario.getAtributo().getNivel()){
            Random random = new Random();

            int numeroAleatorio = random.nextInt(1);

            if (numeroAleatorio == 0) {
                listaAAtacar.add(personagemUsuario);
                listaAAtacar.add(personagemAdversario);
                listaVida.add(personagemUsuario.getAtributo().getVida());
                listaVida.add(personagemAdversario.getAtributo().getVida());
            } else {
                listaAAtacar.add(personagemAdversario);
                listaAAtacar.add(personagemUsuario);
                listaVida.add(personagemAdversario.getAtributo().getVida());
                listaVida.add(personagemUsuario.getAtributo().getVida());
            }

        } else {
            listaAAtacar.add(personagemAdversario);
            listaAAtacar.add(personagemUsuario);
            listaVida.add(personagemAdversario.getAtributo().getVida());
            listaVida.add(personagemUsuario.getAtributo().getVida());
        }

        int count = 0;

        while(1 == 1) {

            Personagem personagemAAtacar = listaAAtacar.get(0 + count);

            Personagem personagemAReceber = listaAAtacar.get(1 - count);

            Double vidaPersonagemAReceber = listaVida.get(1 - count);

            Double danoTotal = null;

            Double danoArma = personagemAAtacar.getItens().stream().filter(item -> item.getTipo().name() == "ARMA").collect(Collectors.toList()).get(0).getValor();

            danoTotal = (danoTotal + personagemAAtacar.getAtributo().getForca() + danoArma) * personagemAAtacar.getAtributo().getDestreza();

            Double defesaArmadura = personagemAReceber.getItens().stream().filter(item -> item.getTipo().name() == "ARMADURA").collect(Collectors.toList()).get(0).getValor();

            Double danoDesviado = danoTotal / 100 * defesaArmadura;

            danoTotal -= danoDesviado;
            Double vidaAtualizadaPersonagemAReceber = vidaPersonagemAReceber - danoTotal;
            listaVida.set(1 - count, vidaAtualizadaPersonagemAReceber);

            BatalhaResponse batalhaResponse = new BatalhaResponse();

            batalhaResponse.setCritico(personagemAAtacar.getAtributo().getDestreza());
            batalhaResponse.setDanoTotal(danoTotal);
            batalhaResponse.setPorcentagemdanoDesviado(danoDesviado);
            batalhaResponse.setNome(personagemAAtacar.getNome());

            logs.add(batalhaResponse);

            if (vidaAtualizadaPersonagemAReceber <= 0) {

                listaBatalhaResponse.setVencedor(listaAAtacar.get(0 + count).getNome());
                break;
            }

            if (count == 0) {
                count = 1;
            } else {
                count = 0;
            }

        }

        listaBatalhaResponse.setListaLogsBatalha(logs);

        return listaBatalhaResponse;

    }
}
