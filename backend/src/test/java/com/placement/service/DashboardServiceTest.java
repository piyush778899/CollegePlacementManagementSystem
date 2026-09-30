package com.placement.service;

import com.placement.dto.DashboardStatsResponse;
import com.placement.entity.DriveStatus;
import com.placement.repository.CompanyRepository;
import com.placement.repository.JobApplicationRepository;
import com.placement.repository.PlacementDriveRepository;
import com.placement.repository.StudentProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private PlacementDriveRepository driveRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private StudentProfileRepository studentProfileRepository;

    @Mock
    private JobApplicationRepository applicationRepository;

    @InjectMocks
    private DashboardService dashboardService;

    @Test
    void testGetStats() {
        when(driveRepository.count()).thenReturn(10L);
        when(driveRepository.findByStatus(DriveStatus.ONGOING)).thenReturn(Collections.emptyList());
        when(driveRepository.findByStatus(DriveStatus.UPCOMING)).thenReturn(Collections.emptyList());
        when(companyRepository.count()).thenReturn(5L);
        when(studentProfileRepository.count()).thenReturn(100L);
        when(studentProfileRepository.countByIsPlacedTrue()).thenReturn(80L);
        when(applicationRepository.count()).thenReturn(250L);
        when(driveRepository.findAveragePackage()).thenReturn(12.75);
        when(driveRepository.findHighestPackage()).thenReturn(45.0);

        DashboardStatsResponse stats = dashboardService.getStats();

        assertNotNull(stats);
        assertEquals(10L, stats.getTotalDrives());
        assertEquals(5L, stats.getTotalCompanies());
        assertEquals(100L, stats.getTotalStudents());
        assertEquals(80L, stats.getPlacedStudents());
        assertEquals(80.0, stats.getPlacementRatePercentage());
        assertEquals(12.75, stats.getAveragePackage());
        assertEquals(45.0, stats.getHighestPackage());
    }
}
