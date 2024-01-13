package MathOlympiad.rpg.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestControllerAdvice
public class UserControllerAdvice {

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionMessageHandler> failedValidateCredentials(MethodArgumentNotValidException failedValidateCredentials) {

        List<FieldError> listaDeErros = failedValidateCredentials.getFieldErrors();

        StringBuilder expressaoFinal = new StringBuilder();

        expressaoFinal.append('[');

        int index = 1;

        for (FieldError erro : listaDeErros) {

            expressaoFinal.append('"' + erro.getDefaultMessage() + '"');

            if (listaDeErros.size() != index){

                expressaoFinal.append(",");

            }

            index++;
        }

        expressaoFinal.append(']');

        ExceptionMessageHandler error = new ExceptionMessageHandler(
                HttpStatus.CONFLICT.value(), "Credenciais invalidas!", expressaoFinal.toString()
        );


        return new ResponseEntity<>(error, HttpStatus.CONFLICT);

    }

    @ResponseBody
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ExceptionMessageHandler> responseStatus(ResponseStatusException responseStatusException) {

        ExceptionMessageHandler error = new ExceptionMessageHandler(
                responseStatusException.getStatusCode().value(), responseStatusException.getReason(), ""
        );

        return new ResponseEntity<>(error, responseStatusException.getStatusCode());
    }

}