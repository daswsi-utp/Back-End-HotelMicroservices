package com.microservice_rooms.service;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import java.io.IOException;
import java.net.MalformedURLException;

public interface ILocalFileCacheService {

    //String storefile(MultipartFile file) throws IOException;
    void deletefile(String filename) throws IOException;
    //Resource loadFileResource(String filename) throws MalformedURLException;
    Resource loadOrDownload(String bucketName, String key) throws IOException;

}
