package MathOlympiad.rpg.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class VerificarParametrosService {

    public void verificar(String parametro) {

        String regex = "^[a-zA-Z0-9]";

        if (regex.matches(parametro) || parametro == null || parametro.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Input Invalido!");
        }
    }

}
