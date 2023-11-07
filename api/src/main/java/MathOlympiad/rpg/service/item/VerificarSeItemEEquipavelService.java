package MathOlympiad.rpg.service.item;


import MathOlympiad.rpg.domain.Item;
import MathOlympiad.rpg.enumerator.ItemTipos;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VerificarSeItemEEquipavelService {

    public boolean verificar(Item item) {

        List<ItemTipos> utilitarios = new ArrayList<>();
        utilitarios.add(ItemTipos.ARMA);
        utilitarios.add(ItemTipos.DEFESA);
        utilitarios.add(ItemTipos.ARMADURA);

        if (utilitarios.contains(item.getTipo())) {

            return true;

        }

        return false;
    }


}
