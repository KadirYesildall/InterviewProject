package com.InterviewProject.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "packages")
public class PackageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String packageName;
    private String version;
    private String author;

    @Column(columnDefinition = "TEXT")
    private String metaData;

    private LocalDateTime uploadedAt = LocalDateTime.now();

}
