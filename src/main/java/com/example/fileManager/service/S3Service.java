package com.example.fileManager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${aws.bucket-name}")
    private String bucketName;

    public String uploadFile(String fileName, Path filePath) throws IOException {
        byte[] fileBytes = Files.readAllBytes(filePath);
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        s3Client.putObject(putObjectRequest, filePath);
        return "File uploaded successfully: " + fileName;
    }

    public List<String> listFiles() {
        ListObjectsV2Request request = ListObjectsV2Request.builder().bucket(bucketName).build();
        ListObjectsV2Response result = s3Client.listObjectsV2(request);
        return result.contents().stream()
                .map(S3Object::key)
                .collect(Collectors.toList());
    }
    public List<S3Object> listFilesFromS3() {
        ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder().bucket(bucketName).build();
        ListObjectsV2Response listObjectsV2Response = s3Client.listObjectsV2(listObjectsV2Request);
        return listObjectsV2Response.contents();
    }

    public InputStream downloadFile(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(key).build();
        ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
        return s3Object;
    }

    public void deleteFile(String filename) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(filename)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
    }
}
