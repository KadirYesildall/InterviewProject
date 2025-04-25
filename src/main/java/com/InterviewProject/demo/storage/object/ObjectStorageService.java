package com.InterviewProject.demo.storage.object;

import com.InterviewProject.demo.storage.StorageService;
import io.minio.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Getter
@Setter
@Service
@ConditionalOnProperty(value = "storage.strategy", havingValue = "object-storage")
public class ObjectStorageService implements StorageService {

    private final MinioClient minioClient;

    @Value("${storage.object.bucket}")
    private String bucket;

    public ObjectStorageService(@Value("${storage.object.url}") String url,
                                @Value("${storage.object.access-key}") String accessKey,
                                @Value("${storage.object.secret-key}") String secretKey) {
        this.minioClient = MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
    }

    @Override
    public void store(String path, MultipartFile file) throws Exception {
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        }

        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucket)
                .object(path)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build());
    }

    @Override
    public InputStream load(String path) throws Exception {
        return minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucket)
                .object(path)
                .build());
    }

    @Override
    public Resource loadAsResource(String path) throws Exception {
        return new InputStreamResource(load(path));
    }
}
