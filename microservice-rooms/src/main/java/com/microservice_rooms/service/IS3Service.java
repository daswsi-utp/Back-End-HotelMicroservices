package com.microservice_rooms.service;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

public interface IS3Service {
    String createBucket(String bucketName);
    String checkIfBucketExists(String bucketName);
    List<String> getAllBuckets();
    Boolean uploadFile(String bucketName, String key, Path fileLocation);
    void downloadFile(String bucket, String key) throws IOException;
    String generatePreSignedUpUploadUrl(String bucketName, String key, Duration duration);
    String generatePreSignedUpDownloadUrl(String bucketName, String key, Duration duration);
    void deleteFile(String bucketName, String key);
}
