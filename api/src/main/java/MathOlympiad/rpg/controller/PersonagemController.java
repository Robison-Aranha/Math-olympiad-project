package MathOlympiad.rpg.controller;


import MathOlympiad.rpg.controller.request.CriarPersonagemRequest;
import MathOlympiad.rpg.controller.request.PersonagemRequest;
import MathOlympiad.rpg.controller.response.personagem.ListarItensIventarioResponse;
import MathOlympiad.rpg.controller.response.personagem.ListarPersonagensResponse;
import MathOlympiad.rpg.service.personagem.CriarPersonagemService;
import MathOlympiad.rpg.service.personagem.DeletarPersonagemService;
import MathOlympiad.rpg.service.personagem.ListarInventarioService;
import MathOlympiad.rpg.service.personagem.ListarPersonagensService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/personagem")
public class PersonagemController {

    @Autowired
    CriarPersonagemService criarPersonagemService;

    @Autowired
    DeletarPersonagemService deletarPersonagemService;

    @Autowired
    ListarPersonagensService listarPersonagensService;

    @Autowired
    ListarInventarioService listarInventarioService;

    @PostMapping("/criar")
    public void criarPersonagem(@Valid @RequestBody CriarPersonagemRequest request) {
        criarPersonagemService.criar(request);
    }

    @PostMapping("/deletar/{id}")
    public void deletarPersonagem(@PathVariable Long id) {

    }
    @GetMapping("/iventario/{id}")
    public ListarItensIventarioResponse listarInventario(@Valid @RequestBody PersonagemRequest request) {
        return listarInventarioService.listar(request);
    }

    @GetMapping()
    public Page<ListarPersonagensResponse> listarPersonagens() {
        return listarPersonagensService.listar();
    }


}
