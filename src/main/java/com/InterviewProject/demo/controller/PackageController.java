package com.InterviewProject.demo.controller;

import com.InterviewProject.demo.model.MetaDataDTO;
import com.InterviewProject.demo.model.PackageEntity;
import com.InterviewProject.demo.repository.PackageRepository;
import com.InterviewProject.demo.storage.StorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.Set;

@Setter
@Getter
@RestController
@RequestMapping("/")
public class PackageController {

    private final StorageService storageService;
    private final ObjectMapper objectMapper;
    private final PackageRepository packageRepository;
    @Autowired
    private Validator validator;

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

            Set<ConstraintViolation<MetaDataDTO>> violations = validator.validate(metaDataDTO);
            if (!violations.isEmpty()) {
                StringBuilder errors = new StringBuilder();
                violations.forEach(v -> errors.append(v.getMessage()).append("; "));
                return ResponseEntity.badRequest().body("Invalid meta.json: " + errors);
            }

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

    @GetMapping("/{packageName}/{version}/{fileName}")
    public ResponseEntity<?> downloadPackage(
            @PathVariable String packageName,
            @PathVariable String version,
            @PathVariable String fileName){
        try {
            String filePath = packageName + "/" + version + "/" + fileName;
            Resource file = storageService.loadAsResource(filePath);

            if (file.exists()){
                return ResponseEntity.ok()
                        .header("Content-Disposition", "attachment; filename=\"" + file.getFilename() + "/")
                        .body(file);
            }
            else {
                return ResponseEntity.notFound().build();
            }
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body("Failed to download !" + e.getMessage());
        }
    }
}
