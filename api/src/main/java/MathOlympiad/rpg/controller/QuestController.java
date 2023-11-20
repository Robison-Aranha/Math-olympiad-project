package MathOlympiad.rpg.controller;


import MathOlympiad.rpg.controller.request.PersonagemRequest;
import MathOlympiad.rpg.service.quest.IniciarQuestService;
import MathOlympiad.rpg.service.quest.TerminarQuestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quest")
public class QuestController {

    @Autowired
    IniciarQuestService iniciarQuestService;

    @Autowired
    TerminarQuestService terminarQuestService;

    @PostMapping("/iniciar/{id}")
    public void iniciarQuest(@PathVariable Long id, @Valid @RequestBody PersonagemRequest request) {
        iniciarQuestService.iniciar(id, request);
    }

    @PostMapping("/terminar/")
    public void terminarQuest(@Valid @RequestBody PersonagemRequest request) {
        terminarQuestService.terminar(request);
    }


}
