package com.InterviewProject.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.profiles.active=test",
        "storage.strategy=file-system",
        "storage.file.base-path=uploads-test/"
})
@AutoConfigureMockMvc(addFilters = false)
public class PackageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldUploadPackageSuccessfully() throws Exception {
        // Mock .rep file
        MockMultipartFile repFile = new MockMultipartFile(
                "package", "mypackage.rep", "application/zip", "fakezipdata".getBytes());

        // Mock valid meta.json
        String validJson = """
            {
                "name": "mypackage",
                "version": "1.0.0",
                "author": "John Doe",
                "dependencies": [
                    {"packageName": "std", "version": "1.0.0"}
                ]
            }
        """;

        MockMultipartFile metaFile = new MockMultipartFile(
                "meta", "meta.json", "application/json", validJson.getBytes());

        mockMvc.perform(multipart("/mypackage/1.0.0")
                        .file(repFile)
                        .file(metaFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldRejectIfMetaJsonMissing() throws Exception {
        // Only the .rep file provided
        MockMultipartFile repFile = new MockMultipartFile(
                "package", "mypackage.rep", "application/zip", "fakezipdata".getBytes());

        mockMvc.perform(multipart("/mypackage/1.0.0")
                        .file(repFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest());
    }
}
