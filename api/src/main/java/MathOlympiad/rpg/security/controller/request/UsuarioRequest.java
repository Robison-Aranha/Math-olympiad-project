package MathOlympiad.rpg.security.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


@Data
public class UsuarioRequest {
    @Pattern(regexp = "^[a-zA-Z0-9]{6,12}$",
            message = "O nome de usuário deve ter de 6 a 12 de comprimento sem caracteres especiais!")
    @NotBlank
    @NotNull
    private String nome;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{4,12}$",
            message = "A senha deve ter no mínimo 4 e no máximo 12 de comprimento contendo pelo menos 1 maiúscula, 1 minúscula, 1 caractere especial e 1 dígito!")
    @NotBlank
    @NotNull
    private String senha;



}
