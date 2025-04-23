package com.InterviewProject.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/")
public class PackageController {

    public ResponseEntity<String> uploadPackage(
            @PathVariable String packageName,
            @PathVariable String version,
            @RequestParam("package") MultipartFile packageFile,
            @RequestParam("meta") MultipartFile metaFile){

        return ResponseEntity.ok("upload endpoint successful");
    }

    public ResponseEntity<?> downloadPackage(
            @PathVariable String packageName,
            @PathVariable String version,
            @PathVariable String fileName){

        return ResponseEntity.ok("Download endpoint successful");
    }
}
