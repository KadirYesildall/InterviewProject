package com.InterviewProject.demo.model;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MetaDataDTO {

    private String name;
    private String version;
    private String author;
    private List<Dependency> dependencies;

    @Getter
    @Setter
    public static class Dependency{
        private String packageName;
        private String version;
    }
}
