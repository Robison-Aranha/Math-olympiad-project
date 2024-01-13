package MathOlympiad.rpg.security.mapper;

import MathOlympiad.rpg.domain.Usuario;
import MathOlympiad.rpg.security.controller.request.UsuarioRequest;
import MathOlympiad.rpg.security.controller.response.UsuarioResponse;

public class UsuarioMapper {

    public static Usuario toEntity(UsuarioRequest request) {
        Usuario entity = new Usuario();
        entity.setNome(request.getNome());
        return entity;
    }

    public static UsuarioResponse toResponse(Usuario entity) {
        return UsuarioResponse.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .imagemPerfil(entity.getImagem())
                .build();
    }

    public static UsuarioResponse toResponseToken(Usuario entity, String token) {
        return UsuarioResponse.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .token(token)
                .imagemPerfil(entity.getImagem())
                .build();
    }


}