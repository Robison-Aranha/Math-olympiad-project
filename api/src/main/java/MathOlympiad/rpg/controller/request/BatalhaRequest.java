package MathOlympiad.rpg.controller.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;

@Data
public class BatalhaRequest {

    @NotBlank
    private Long personagemUsuario;

    @NotBlank
    private Long personagemAdversario;


    private ArrayList<Long> itensAUsar;

}
