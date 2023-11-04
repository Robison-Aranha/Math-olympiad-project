package MathOlympiad.rpg.controller;


import MathOlympiad.rpg.controller.request.IniciarQuestRequest;
import MathOlympiad.rpg.service.quest.IniciarQuestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quest")
public class QuestController {

    @Autowired
    IniciarQuestService iniciarQuestService;

    @PostMapping("/iniciar/{id}")
    public void iniciarQuest(@PathVariable Long id, @Valid @RequestBody IniciarQuestRequest request) {
        iniciarQuestService.iniciar(id, request);
    }



}
