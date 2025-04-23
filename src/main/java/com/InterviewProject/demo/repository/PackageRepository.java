package com.InterviewProject.demo.repository;


import com.InterviewProject.demo.model.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PackageRepository extends JpaRepository<PackageEntity, Long> {
    Optional<PackageEntity> findByPackageNameAndVersion(String packageName, String version);
}
