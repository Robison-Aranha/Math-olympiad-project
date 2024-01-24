package MathOlympiad.quiz.service;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

@Service
public class ConverterImagemParaBase64 {

    public String converter(String name) throws IOException {

        File imagem = new File("src/main/java/MathOlympiad/quiz/assets/" + name);

        String fileEncoded = Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(imagem));

        String tipoConteudo = Files.probeContentType(imagem.toPath());

        StringBuilder sb = new StringBuilder();

        sb.append("data:" + tipoConteudo + ";base64,");
        sb.append(fileEncoded);

        return sb.toString();
    }

}
