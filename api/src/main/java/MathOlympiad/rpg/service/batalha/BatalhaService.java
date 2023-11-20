package MathOlympiad.rpg.service.batalha;


import MathOlympiad.rpg.controller.request.BatalhaRequest;
import MathOlympiad.rpg.controller.response.batalha.BatalhaResponse;
import MathOlympiad.rpg.controller.response.batalha.ListarLogsBatalhaResponse;
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

        Double nivelPersonagemUsuario = personagemUsuario.getAtributos().get("NIVEL").getValor() + personagemUsuario.getClasse().getAtributos().get("NIVEL").getValor();
        Double nivelPersonagemAdversario = personagemAdversario.getAtributos().get("NIVEL").getValor();

        Double vidaPersonagemUsuario = personagemUsuario.getAtributos().get("VIDA").getValor() + personagemUsuario.getClasse().getAtributos().get("NIVEL").getValor();
        Double vidaPersonagemAdversario = personagemAdversario.getAtributos().get("VIDA").getValor();

        if (nivelPersonagemUsuario > nivelPersonagemAdversario) {
            listaAAtacar.add(personagemUsuario);
            listaAAtacar.add(personagemAdversario);
            listaVida.add(vidaPersonagemUsuario);
            listaVida.add(vidaPersonagemAdversario);
        } else if (nivelPersonagemUsuario == nivelPersonagemAdversario){
            Random random = new Random();

            int numeroAleatorio = random.nextInt(1);

            if (numeroAleatorio == 0) {
                listaAAtacar.add(personagemUsuario);
                listaAAtacar.add(personagemAdversario);
                listaVida.add(vidaPersonagemUsuario);
                listaVida.add(vidaPersonagemAdversario);
            } else {
                listaAAtacar.add(personagemAdversario);
                listaAAtacar.add(personagemUsuario);
                listaVida.add(vidaPersonagemUsuario);
                listaVida.add(vidaPersonagemAdversario);
            }

        } else {
            listaAAtacar.add(personagemAdversario);
            listaAAtacar.add(personagemUsuario);
            listaVida.add(vidaPersonagemUsuario);
            listaVida.add(vidaPersonagemAdversario);
        }

        int count = 0;

        while(1 == 1) {

            Personagem personagemAAtacar = listaAAtacar.get(0 + count);

            Personagem personagemAReceber = listaAAtacar.get(1 - count);

            Double vidaPersonagemAReceber = listaVida.get(1 - count);

            Double porcentagemDesvioAdversario = personagemAReceber.getAtributos().get("AGILIDADE").getValor();

            Random random = new Random();

            Integer numeroEscolhido = random.nextInt(99);

            if (numeroEscolhido > porcentagemDesvioAdversario) {

                Double danoTotal = null;

                Double danoArma = personagemAAtacar.getItens().stream().filter(i -> i.getTipo().name().equals("ARMA")).collect(Collectors.toList()).get(0).getValor();
                Double forcaClassePersonagem = personagemAAtacar.getClasse().getAtributos().get("FORCA").getValor();
                Double forcaPersonagem = personagemAAtacar.getAtributos().get("FORCA").getValor();
                Double destrezaClassePersonagem = personagemAAtacar.getClasse().getAtributos().get("DESTREZA").getValor();
                Double destrezaPersonagem = personagemAAtacar.getAtributos().get("DESTREZA").getValor();

                danoTotal = (forcaPersonagem + forcaClassePersonagem + danoArma) * (destrezaPersonagem + destrezaClassePersonagem);

                Double defesaArmadura = personagemAReceber.getItens().stream().filter(i -> i.getTipo().name().equals("ARMADURA")).collect(Collectors.toList()).get(0).getValor();

                Double danoDesviado = danoTotal / 100 * defesaArmadura;

                danoTotal -= danoDesviado;
                Double vidaAtualizadaPersonagemAReceber = vidaPersonagemAReceber - danoTotal;
                listaVida.set(1 - count, vidaAtualizadaPersonagemAReceber);

                BatalhaResponse batalhaResponse = new BatalhaResponse();

                batalhaResponse.setCritico(destrezaPersonagem);
                batalhaResponse.setDanoTotal(danoTotal);
                batalhaResponse.setPorcentagemdanoDesviado(danoDesviado);
                batalhaResponse.setNome(personagemAAtacar.getNome());
                batalhaResponse.setAdversarioDesviou(false);

                logs.add(batalhaResponse);

                if (vidaAtualizadaPersonagemAReceber <= 0) {
                    listaBatalhaResponse.setVencedor(listaAAtacar.get(0 + count).getNome());
                    break;
                }
            } else {

                BatalhaResponse batalhaResponse = new BatalhaResponse();

                batalhaResponse.setCritico(0.0);
                batalhaResponse.setDanoTotal(0.0);
                batalhaResponse.setPorcentagemdanoDesviado(0.0);
                batalhaResponse.setNome(personagemAAtacar.getNome());
                batalhaResponse.setAdversarioDesviou(true);

                logs.add(batalhaResponse);

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
