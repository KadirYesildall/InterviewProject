package com.InterviewProject.demo.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MetaDataDTO {
    @NotBlank(message = "Package name is required")
    private String name;
    @NotBlank(message = "Version is required")
    private String version;
    @NotBlank(message = "Author is required")
    private String author;
    @NotEmpty(message = "At least one dependency is required")
    private List<Dependency> dependencies;

    @Getter
    @Setter
    public static class Dependency{
        @NotBlank(message = "Dependency packageName is required")
        private String packageName;
        @NotBlank(message = "Dependency version is required")
        private String version;
    }
}
