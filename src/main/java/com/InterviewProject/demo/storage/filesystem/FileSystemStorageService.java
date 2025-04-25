package com.InterviewProject.demo.storage.filesystem;

import com.InterviewProject.demo.storage.StorageService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Getter
@Setter
@Service
@ConditionalOnProperty(value = "storage.strategy", havingValue = "file-system")
public class FileSystemStorageService implements StorageService {

    @Value("${storage.file.base-path:uploads/}")
    private String basePath;

    @Override
    public void store(String path, MultipartFile file) throws IOException{
        Path fullPath = Paths.get(basePath, path).normalize();
        Files.createDirectories(fullPath.getParent());
        Files.copy(file.getInputStream(), fullPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public InputStream load(String path) throws IOException{
        Path file = Paths.get(basePath, path).normalize();
        return Files.newInputStream(file);
    }

    @Override
    public Resource loadAsResource(String path){
        Path file = Paths.get(basePath, path).normalize();
        return new FileSystemResource(file);
    }
}
