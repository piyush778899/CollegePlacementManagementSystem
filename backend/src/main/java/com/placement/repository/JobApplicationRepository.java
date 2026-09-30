package com.placement.repository;

import com.placement.entity.ApplicationStatus;
import com.placement.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByStudentProfileId(Long studentProfileId);
    List<JobApplication> findByStudentProfileUserEmail(String email);
    List<JobApplication> findByDriveId(Long driveId);
    Optional<JobApplication> findByStudentProfileIdAndDriveId(Long studentProfileId, Long driveId);
    boolean existsByStudentProfileIdAndDriveId(Long studentProfileId, Long driveId);
    long countByStatus(ApplicationStatus status);
}
