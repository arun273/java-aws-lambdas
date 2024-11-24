package org.example.lambda3;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;

public class AWSS3Client {
    private final S3Client s3Client = S3Client.create();

    public void uploadFile(String bucketName, String key, byte[] content) {
        s3Client.putObject(PutObjectRequest.builder().bucket(bucketName).key(key).build(),
                software.amazon.awssdk.core.sync.RequestBody.fromBytes(content));
    }

    public InputStream downloadFile(String bucketName, String key) {
        return s3Client.getObject(GetObjectRequest.builder().bucket(bucketName).key(key).build());
    }
}
