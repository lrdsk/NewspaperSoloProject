package org.example.services.servicesImpl;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Service
public class MultipartServiceImpl {
    private final Path filesLocation = Paths.get("src/main/resources/templates");

    @Transactional
    public String savePhoto(MultipartFile file) throws IOException {
        File uploadDir = new File(filesLocation.toAbsolutePath().toString());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
        String curDate = LocalDateTime.now().format(formatter);
        String fileName =
                "attach_" + curDate + "_" + file.getOriginalFilename().toLowerCase()
                        .replaceAll(" ", "-");
        String contentType = file.getContentType();

        if (contentType != null && (contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
            file.transferTo(new File(uploadDir + "/" + fileName));
        } else {
            throw new IllegalArgumentException("Тип файла не поддерживается. Поддерживаемые типы: JPG, PNG.");
        }
        return fileName;
    }

    public Resource loadFileAsResource(String fileName)
            throws MalformedURLException {
        Path fileStorageLocation =
                Paths.get(filesLocation.toAbsolutePath().toString()).normalize();
        Path filePath = fileStorageLocation.resolve(fileName).normalize();
        return new UrlResource(filePath.toUri());
    }
}
