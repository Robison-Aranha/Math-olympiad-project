package MathOlympiad.rpg.controller;


import MathOlympiad.rpg.controller.response.classe.ListarClassesResponse;
import MathOlympiad.rpg.service.classe.ListarClassesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequestMapping("/classe")
@RestController
public class ClasseController {

    @Autowired
    ListarClassesService listarClassesService;

    @GetMapping("/{cla}")
    public ListarClassesResponse lista(@PathVariable String cla) throws IOException {
        return listarClassesService.listar(cla);
    }


}
