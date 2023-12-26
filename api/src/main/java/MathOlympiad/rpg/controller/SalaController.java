package MathOlympiad.rpg.controller;


import MathOlympiad.rpg.controller.request.CriarSalaRequest;
import MathOlympiad.rpg.controller.request.EntrarSalaPrivadaRequest;
import MathOlympiad.rpg.controller.request.EntrarSalaPublicaRequest;
import MathOlympiad.rpg.controller.response.ListarSalaResponse;
import MathOlympiad.rpg.controller.response.SalaResponse;
import MathOlympiad.rpg.controller.response.VerificarHostSalaResponse;
import MathOlympiad.rpg.service.sala.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/sala")
@RestController
public class SalaController {

    @Autowired
    CriarSalaService criarSalaService;

    @Autowired
    EntrarSalaService entrarSalaService;

    @Autowired
    ListarSalasService listarSalasService;


    @Autowired
    VerificarSeUsuarioEOHostDaSala verificarSeUsuarioEOHostDaSala;

    @PostMapping("/criar")
    public SalaResponse criarSala(@RequestBody @Valid CriarSalaRequest request) {
        return criarSalaService.criar(request);
    }

    @PostMapping("/buscar-sala")
    public List<ListarSalaResponse> buscarSala(@RequestBody @Valid @RequestParam("nome") String nome) {
        return listarSalasService.listar(nome);
    }

    @PostMapping("/entrar-privada")
    public SalaResponse entrarSalaPrivada(@RequestBody @Valid EntrarSalaPrivadaRequest request) {
        return entrarSalaService.entrarPrivada(request);
    }

    @PostMapping("/entrar-publica")
    public SalaResponse entrarSalaPublica(@RequestBody @Valid EntrarSalaPublicaRequest request) {
        return entrarSalaService.entrarPublica(request);
    }

    @GetMapping("/verificar-host")
    public VerificarHostSalaResponse verificarHost() {
        return verificarSeUsuarioEOHostDaSala.verificar();
    }

}
