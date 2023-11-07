package MathOlympiad.rpg.controller;


import MathOlympiad.rpg.controller.request.PersonagemRequest;
import MathOlympiad.rpg.service.item.ComprarItemService;
import MathOlympiad.rpg.service.item.UsarItemService;
import MathOlympiad.rpg.service.item.VenderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/item")
public class ItemController {

    @Autowired
    ComprarItemService comprarItemService;

    @Autowired
    VenderItemService venderItemService;

    @Autowired
    UsarItemService usarItemService;


    @PostMapping("/comprar/{id}")
    public void comprarItem(@PathVariable Long id, @RequestBody PersonagemRequest request) {
        comprarItemService.comprar(id, request);
    }

    @PostMapping("/vender/{id}")
    public void venderItem(@PathVariable Long id,  @RequestBody PersonagemRequest request) {
        venderItemService.vender(id, request);
    }

    @PostMapping("/usar/{id}")
    public void usarItem(@PathVariable Long id,  @RequestBody PersonagemRequest request) {
        usarItemService.usar(id, request);
    }

}
