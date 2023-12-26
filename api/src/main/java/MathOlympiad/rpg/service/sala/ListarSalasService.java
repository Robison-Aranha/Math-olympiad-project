package MathOlympiad.rpg.service.sala;


import MathOlympiad.rpg.controller.response.ListarSalaResponse;
import MathOlympiad.rpg.mapper.ListarSalaMapper;
import MathOlympiad.rpg.repository.SalaRepository;
import MathOlympiad.rpg.service.VerificarParametrosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListarSalasService {

    @Autowired
    SalaRepository salaRepository;

    @Autowired
    VerificarParametrosService verificarParametrosService;


    public List<ListarSalaResponse> listar(String nome) {

        verificarParametrosService.verificar(nome);

        return salaRepository.findSalas(nome).stream().map(ListarSalaMapper::toResponse).collect(Collectors.toList());

    }


}
