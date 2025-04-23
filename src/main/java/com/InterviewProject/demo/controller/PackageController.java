package com.InterviewProject.demo.controller;

import com.InterviewProject.demo.model.MetaDataDTO;
import com.InterviewProject.demo.model.PackageEntity;
import com.InterviewProject.demo.repository.PackageRepository;
import com.InterviewProject.demo.storage.StorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

@Setter
@Getter
@RestController
@RequestMapping("/")
public class PackageController {

    private final StorageService storageService;
    private final ObjectMapper objectMapper;
    private final PackageRepository packageRepository;

    @Value("${storage.strategy}")
    private String strategy;

    public PackageController(StorageService storageService, ObjectMapper objectMapper, PackageRepository packageRepository){
        this.storageService = storageService;
        this.objectMapper = objectMapper;
        this.packageRepository = packageRepository;
    }

    @PostMapping("/{packageName}/{version}")
    public ResponseEntity<String> uploadPackage(
            @PathVariable String packageName,
            @PathVariable String version,
            @RequestParam("package") MultipartFile packageFile,
            @RequestParam("meta") MultipartFile metaFile){
        try{
            if (!packageFile.getOriginalFilename().endsWith(".rep")){
                return ResponseEntity.badRequest().body("Invalid package file it must be .rep !");
            }
            if (!metaFile.getOriginalFilename().endsWith(".json")){
                return ResponseEntity.badRequest().body("Invalid metadata file it must be .json !");
            }

            String json = new String(metaFile.getBytes(), StandardCharsets.UTF_8);
            MetaDataDTO metaDataDTO = objectMapper.readValue(json, MetaDataDTO.class);

            PackageEntity packageEntity = new PackageEntity();
            packageEntity.setPackageName(packageName);
            packageEntity.setVersion(version);
            packageEntity.setAuthor(metaDataDTO.getAuthor());
            packageEntity.setMetaData(json);
            packageRepository.save(packageEntity);

            String basePath = packageName + "/" + version + "/";
            storageService.store(basePath + "package.rep", packageFile);
            storageService.store(basePath+ "meta.json", metaFile);

            return ResponseEntity.ok("upload endpoint successful");
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Upload failed: " + e.getMessage());
        }
    }

    public ResponseEntity<?> downloadPackage(
            @PathVariable String packageName,
            @PathVariable String version,
            @PathVariable String fileName){

        return ResponseEntity.ok("Download endpoint successful");
    }
}
