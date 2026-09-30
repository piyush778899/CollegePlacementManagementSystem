package com.placement.repository;

import com.placement.entity.DriveStatus;
import com.placement.entity.PlacementDrive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlacementDriveRepository extends JpaRepository<PlacementDrive, Long> {
    List<PlacementDrive> findByStatus(DriveStatus status);
    List<PlacementDrive> findByCompanyId(Long companyId);
    
    @Query("SELECT AVG(d.packageLpa) FROM PlacementDrive d")
    Double findAveragePackage();

    @Query("SELECT MAX(d.packageLpa) FROM PlacementDrive d")
    Double findHighestPackage();
}
