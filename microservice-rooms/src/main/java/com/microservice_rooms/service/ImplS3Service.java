package com.microservice_rooms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImplS3Service implements  IS3Service{
    @Value("${spring.destination.folder}")
    private String destinationFolder;
    @Autowired
    private S3Client s3Client;
    @Autowired
    private S3Presigner s3Presigner;
    @Override
    public String createBucket(String bucketName) {
        CreateBucketResponse response = this.s3Client.createBucket(bucketBuilder -> bucketBuilder.bucket(bucketName));
        return "Bucket created at location:" + response.location();
    }

    @Override
    public String checkIfBucketExists(String bucketName) {
        try {
            this.s3Client.headBucket(headBucket -> headBucket.bucket(bucketName));
            return "Bucket " + bucketName + " was found";
        }catch (S3Exception e){
            return "Bucket " + bucketName + " was not found: " + e;
        }
    }

    @Override
    public List<String> getAllBuckets() {
        ListBucketsResponse bucketsResponse = this.s3Client.listBuckets();
        if(!bucketsResponse.hasBuckets()){
            return List.of();
        }
        return bucketsResponse.buckets()
                .stream()
                .map(Bucket::name)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean uploadFile(String bucketName, String key, Path fileLocation) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        PutObjectResponse putObjectResponse = this.s3Client.putObject(putObjectRequest, fileLocation);
        return putObjectResponse.sdkHttpResponse().isSuccessful();
    }

    @Override
    public void downloadFile(String bucket, String key) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        ResponseBytes<GetObjectResponse> objectBytes = this.s3Client.getObjectAsBytes(getObjectRequest);
        String filename;
        if(key.contains("/")){
            filename = key.substring(key.lastIndexOf("/"));
        } else {
            filename = key;
        }
        String filePath = Paths.get(destinationFolder, filename).toString();
        File file = new File(filePath);
        file.getParentFile().mkdir();
        try(FileOutputStream fileOutputStream = new FileOutputStream(file)){
            fileOutputStream.write(objectBytes.asByteArray());
        }catch (IOException e){
            throw new IOException("Image download failed: " + e);
        }
    }

    @Override
    public String generatePreSignedUpUploadUrl(String bucketName, String key, Duration duration) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(duration)
                .putObjectRequest(putObjectRequest)
                .build();
        PresignedPutObjectRequest presignedPutObjectRequest = this.s3Presigner.presignPutObject(presignRequest);
        URL presignedUrl = presignedPutObjectRequest.url();
        return presignedUrl.toString();
    }

    @Override
    public String generatePreSignedUpDownloadUrl(String bucketName, String key, Duration duration) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(duration)
                .getObjectRequest(getObjectRequest)
                .build();
        PresignedGetObjectRequest presignedRequest = this.s3Presigner.presignGetObject(presignRequest);
        URL presignUrl = presignedRequest.url();
        return presignUrl.toString();
    }
    @Override
    public void deleteFile(String bucketName, String key){
        try{
            this.s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build());
        } catch (S3Exception e){
            throw new RuntimeException("Failed to delete file from S3: " + e.awsErrorDetails().errorMessage());
        }
    }
}
