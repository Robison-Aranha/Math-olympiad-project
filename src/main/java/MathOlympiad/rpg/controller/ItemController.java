package MathOlympiad.rpg.controller;


import MathOlympiad.rpg.service.item.ComprarItemService;
import MathOlympiad.rpg.service.item.VenderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/item")
public class ItemController {

    @Autowired
    ComprarItemService comprarItemService;

    @Autowired
    VenderItemService venderItemService;


    @PostMapping("/comprar/{id}")
    public void comprarItem(@PathVariable Long id) {
        comprarItemService.comprar(id);
    }

    @PostMapping("/vender/{id}")
    public void venderItem(@PathVariable Long id) {

    }


}
