package com.InterviewProject.demo.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface StorageService {
    void store(String path, MultipartFile file) throws Exception;
    InputStream load(String path) throws Exception;
}
