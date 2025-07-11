package com.microservice_rooms.aws_config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
public class StorageConfig {
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;
    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean
    public S3Client getS3Client(){
        AwsCredentials basicCredentials = AwsBasicCredentials.create(accessKey, secretKey);
        return S3Client.builder()
                .region(Region.of(region))
                .endpointOverride(URI.create("https://s3.us-east-2.amazonaws.com"))
                .credentialsProvider(StaticCredentialsProvider.create(basicCredentials))
                .build();
    }
    @Bean
    public S3AsyncClient getS3AsyncClient(){
        AwsCredentials basicCredentials = AwsBasicCredentials.create(accessKey, secretKey);
        return S3AsyncClient.builder()
                .region(Region.of(region))
                .endpointOverride(URI.create("https://s3.us-east-2.amazonaws.com"))
                .credentialsProvider(StaticCredentialsProvider.create(basicCredentials))
                .build();
    }
}
