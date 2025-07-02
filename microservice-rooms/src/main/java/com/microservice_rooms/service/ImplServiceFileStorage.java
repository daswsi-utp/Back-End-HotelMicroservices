package com.microservice_rooms.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class ImplServiceFileStorage implements IServiceFileStorage{

    private final Path uploadDir;

    public ImplServiceFileStorage(@Value("${file.upload-dir}") String uploadDirStr) throws IOException {
        this.uploadDir = Paths.get(uploadDirStr).toAbsolutePath().normalize();
        Files.createDirectories(this.uploadDir);
    }



    @Override
    public String storefile(MultipartFile file) throws IOException {
        String original = StringUtils.cleanPath(file.getOriginalFilename());
        String ext = StringUtils.getFilenameExtension(original);
        String filename = UUID.randomUUID() + (ext != null ? "." + ext : "");
        Path target = this.uploadDir.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        return filename;
    }

    @Override
    public void deletefile(String filename) throws IOException {
        Path target = this.uploadDir.resolve(filename).normalize();
        Files.deleteIfExists(target);
    }

    @Override
    public Resource loadFileResource(String filename) throws MalformedURLException {
        Path filePath = this.uploadDir.resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("No se encuentra el archivo: " + filename);
        }
    }
}
