package MathOlympiad.rpg.controller;


import MathOlympiad.rpg.controller.request.BatalhaRequest;
import MathOlympiad.rpg.controller.response.batalha.ListarLogsBatalhaResponse;
import MathOlympiad.rpg.service.batalha.BatalhaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batalhar")
public class BatalhaController {

    @Autowired
    BatalhaService batalhaService;

    @PutMapping
    public ListarLogsBatalhaResponse batalhar(@Valid @RequestBody BatalhaRequest request) {
        return batalhaService.batalhar(request);
    }



}
