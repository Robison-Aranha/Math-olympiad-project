package MathOlympiad.rpg.security.mapper;

import MathOlympiad.rpg.domain.Usuario;
import MathOlympiad.rpg.security.controller.request.UsuarioRequest;

public class UsuarioMapper {

    public static Usuario toEntity(UsuarioRequest request) {
        Usuario entity = new Usuario();
        entity.setNome(request.getNome());
        return entity;
    }
}