package MathOlympiad.rpg.mapper;

import MathOlympiad.rpg.controller.response.classe.ClasseResponse;
import MathOlympiad.rpg.domain.Atributo;
import MathOlympiad.rpg.domain.Classe;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class ListarClasseMapper {

    public static ClasseResponse toResponse(Classe entity) throws IOException {

        return ClasseResponse.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .imagem(imageToBase64(entity.getImagem()))
                .status(atributosToResponse(entity.getAtributos()))
                .build();


    }

    private static Map<String, Double> atributosToResponse(Map<String, Atributo> status) {

        Map<String, Double> novosAtributos = new HashMap<>();

        status.values().forEach(a -> novosAtributos.put(a.getAtributo().name(), a.getValor()));

        return novosAtributos;
    }

    private static String imageToBase64(String name) throws IOException {

        File imagem = new File("src/main/java/MathOlympiad/rpg/assets/" + name);

        String fileEncoded = Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(imagem));

        String tipoConteudo = Files.probeContentType(imagem.toPath());

        StringBuilder sb = new StringBuilder();

        sb.append("data:" + tipoConteudo + ";base64,");
        sb.append(fileEncoded);

        return sb.toString();
    }


}
