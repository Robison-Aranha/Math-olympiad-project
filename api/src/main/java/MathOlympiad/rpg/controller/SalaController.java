package MathOlympiad.rpg.controller;


import MathOlympiad.rpg.controller.request.CriarSalaRequest;
import MathOlympiad.rpg.service.sala.CriarSalaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/sala")
@RestController
public class SalaController {

    @Autowired
    CriarSalaService criarSalaService;

    @PostMapping("/criar")
    public void criarSala(@RequestBody @Valid CriarSalaRequest request) {
        criarSalaService.criar(request);
    }

}
