package com.daniel.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${file.upload.location}")
    private String uploadDirectory;

    public String uploadFile(MultipartFile productImageFile) throws IOException {
        String originalFileName = productImageFile.getOriginalFilename();

        UUID uuid = UUID.randomUUID();

        String uploadFileName = uuid + "_" + originalFileName;

        File file = new File(uploadDirectory, uploadFileName);

        productImageFile.transferTo(file);

        return uploadFileName;
    }

    public void deleteFile(String productImageName) {
        File existingFile = new File(uploadDirectory, productImageName);
        if (existingFile.exists()) {
            existingFile.delete();
        }
    }
}
