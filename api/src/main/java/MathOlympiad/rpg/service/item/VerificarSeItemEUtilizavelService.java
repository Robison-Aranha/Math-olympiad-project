package MathOlympiad.rpg.service.item;


import MathOlympiad.rpg.domain.Item;
import MathOlympiad.rpg.enumerator.ItemTipos;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class VerificarSeItemEUtilizavelService {

    public void verificar(Item item) {

        List<ItemTipos> utilitarios = new ArrayList<>();
        utilitarios.add(ItemTipos.ARMA);
        utilitarios.add(ItemTipos.ARMADURA);

        if (utilitarios.contains(item.getTipo())) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Item não é utilizavel!");
        }

    }

}
