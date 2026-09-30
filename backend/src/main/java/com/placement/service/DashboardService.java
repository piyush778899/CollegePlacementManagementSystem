package com.placement.service;

import com.placement.dto.DashboardStatsResponse;
import com.placement.entity.DriveStatus;
import com.placement.repository.CompanyRepository;
import com.placement.repository.JobApplicationRepository;
import com.placement.repository.PlacementDriveRepository;
import com.placement.repository.StudentProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService {

    private final PlacementDriveRepository driveRepository;
    private final CompanyRepository companyRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final JobApplicationRepository applicationRepository;

    public DashboardService(PlacementDriveRepository driveRepository,
                            CompanyRepository companyRepository,
                            StudentProfileRepository studentProfileRepository,
                            JobApplicationRepository applicationRepository) {
        this.driveRepository = driveRepository;
        this.companyRepository = companyRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.applicationRepository = applicationRepository;
    }

    @Transactional(readOnly = true)
    public DashboardStatsResponse getStats() {
        DashboardStatsResponse stats = new DashboardStatsResponse();

        long totalDrives = driveRepository.count();
        long activeDrives = driveRepository.findByStatus(DriveStatus.ONGOING).size() +
                            driveRepository.findByStatus(DriveStatus.UPCOMING).size();
        long totalCompanies = companyRepository.count();
        long totalStudents = studentProfileRepository.count();
        long placedStudents = studentProfileRepository.countByIsPlacedTrue();
        long totalApplications = applicationRepository.count();
        Double avgPackage = driveRepository.findAveragePackage();
        Double maxPackage = driveRepository.findHighestPackage();

        stats.setTotalDrives(totalDrives);
        stats.setActiveDrives(activeDrives);
        stats.setTotalCompanies(totalCompanies);
        stats.setTotalStudents(totalStudents);
        stats.setPlacedStudents(placedStudents);
        stats.setTotalApplications(totalApplications);
        stats.setAveragePackage(avgPackage != null ? Math.round(avgPackage * 100.0) / 100.0 : 0.0);
        stats.setHighestPackage(maxPackage != null ? maxPackage : 0.0);

        double rate = totalStudents > 0 ? ((double) placedStudents / totalStudents) * 100.0 : 0.0;
        stats.setPlacementRatePercentage(Math.round(rate * 10.0) / 10.0);

        return stats;
    }
}
