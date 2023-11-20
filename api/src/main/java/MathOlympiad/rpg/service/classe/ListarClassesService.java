package MathOlympiad.rpg.service.classe;


import MathOlympiad.rpg.controller.response.classe.ClasseResponse;
import MathOlympiad.rpg.controller.response.classe.ListarClassesResponse;
import MathOlympiad.rpg.domain.Classe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static MathOlympiad.rpg.mapper.ListarClasseMapper.toResponse;

@Service
public class ListarClassesService {

    @Autowired
    BuscarClassesService buscarClassesService;

    public ListarClassesResponse listar(String cla) throws IOException {


        List<Classe> listaClasses = buscarClassesService.porCla(cla);
        List<ClasseResponse> listaClassesResponse = new ArrayList<>();

        for (Classe classe : listaClasses) {


            ClasseResponse classeResponse = toResponse(classe);

            listaClassesResponse.add(classeResponse);
        }

        ListarClassesResponse listarClasseResponse = new ListarClassesResponse();
        listarClasseResponse.setClasses(listaClassesResponse);

        return listarClasseResponse;
    }
}
