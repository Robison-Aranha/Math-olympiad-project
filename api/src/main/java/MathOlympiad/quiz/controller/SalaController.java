package MathOlympiad.quiz.controller;


import MathOlympiad.quiz.controller.request.CriarSalaRequest;
import MathOlympiad.quiz.controller.request.EntrarSalaPrivadaRequest;
import MathOlympiad.quiz.controller.request.EntrarSalaPublicaRequest;
import MathOlympiad.quiz.controller.response.ListarSalaResponse;
import MathOlympiad.quiz.controller.response.SalaResponse;
import MathOlympiad.quiz.service.sala.*;
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

}
