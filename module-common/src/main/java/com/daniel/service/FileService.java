package com.daniel.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    private static final String FILE_NOT_FOUNT="파일을 찾을 수 없습니다.";
    private static final String FAILED_DOWNLOAD = "파일을 다운로드 할 수 없습니다.";
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

    public Resource loadFile(String fileName) throws FileNotFoundException {
        try {
            Path directoryLocation = Paths.get(uploadDirectory)
                    .toAbsolutePath().normalize();

            Path file = directoryLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new FileNotFoundException(FILE_NOT_FOUNT);
            }
        } catch (MalformedURLException e) {
            throw new FileNotFoundException(FAILED_DOWNLOAD);
        }
    }

    public String getDownloadURI (String uploadFileName) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/products/download/")
                .path(uploadFileName)
                .toUriString();
    }
}
