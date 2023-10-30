package MathOlympiad.rpg.service.item;

import MathOlympiad.rpg.domain.Item;
import MathOlympiad.rpg.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class BuscarItemService {

    @Autowired
    ItemRepository itemRepository;


    public Item buscar(Long id) {

        return itemRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item não encontrado!"));

    }


}
