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
public class ImplLocalFileCacheService implements ILocalFileCacheService {

    private final Path localDir;
    private final IS3Service s3service;

    public ImplLocalFileCacheService(@Value("${file.upload-dir}") String localDirStr, IS3Service s3Service) throws IOException {
        this.localDir = Paths.get(localDirStr).toAbsolutePath().normalize();
        this.s3service = s3Service;
        Files.createDirectories(this.localDir);
    }

    @Override
    public Resource loadOrDownload(String bucketName, String key) throws IOException {
        Path localFile = localDir.resolve(key).normalize();
        if(!Files.exists(localFile)){
            s3service.downloadFile(bucketName, key);
        }
        Resource resource = new UrlResource(localFile.toUri());

        if(resource.exists() && resource.isReadable()){
            return resource;
        } else {
            throw new IOException("Fil not found or it's unreadable: " + key);
        }
    }

    @Override
    public void deletefile(String filename) throws IOException {
        Path target = localDir.resolve(filename).normalize();
        Files.deleteIfExists(target);
        s3service.deleteFile("hotel-room-images-utp", filename);
    }
}
