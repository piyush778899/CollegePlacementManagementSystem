package com.placement.service;

import com.placement.dto.EligibilityResponse;
import com.placement.dto.JobApplicationResponse;
import com.placement.dto.UpdateApplicationStatusRequest;
import com.placement.entity.*;
import com.placement.repository.JobApplicationRepository;
import com.placement.repository.PlacementDriveRepository;
import com.placement.repository.StudentProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobApplicationServiceTest {

    @Mock
    private JobApplicationRepository applicationRepository;

    @Mock
    private PlacementDriveRepository driveRepository;

    @Mock
    private StudentProfileRepository profileRepository;

    @Mock
    private PlacementDriveService placementDriveService;

    @InjectMocks
    private JobApplicationService applicationService;

    private StudentProfile student;
    private PlacementDrive drive;

    @BeforeEach
    void setUp() {
        User user = new User("Jane Doe", "jane@example.com", "pass", Role.STUDENT);
        student = new StudentProfile();
        student.setId(10L);
        student.setUser(user);
        student.setRollNumber("CS201");
        student.setDepartment("CSE");
        student.setCgpa(8.8);
        student.setBacklogs(0);
        student.setTenthPercentage(90.0);
        student.setTwelfthPercentage(92.0);
        student.setIsPlaced(false);

        Company company = new Company();
        company.setId(1L);
        company.setName("Microsoft");

        drive = new PlacementDrive();
        drive.setId(5L);
        drive.setCompany(company);
        drive.setTitle("Software Engineer");
        drive.setJobRole("Software Engineer");
        drive.setPackageLpa(22.0);
    }

    @Test
    void testApplyForDrive_Success() {
        when(profileRepository.findByUserEmail("jane@example.com")).thenReturn(Optional.of(student));
        when(driveRepository.findById(5L)).thenReturn(Optional.of(drive));
        when(applicationRepository.existsByStudentProfileIdAndDriveId(10L, 5L)).thenReturn(false);
        when(placementDriveService.checkEligibility(5L, "jane@example.com"))
                .thenReturn(new EligibilityResponse(true, "Eligible", Collections.emptyList()));

        when(applicationRepository.save(any(JobApplication.class))).thenAnswer(i -> {
            JobApplication app = i.getArgument(0);
            app.setId(100L);
            return app;
        });

        JobApplicationResponse response = applicationService.applyForDrive(5L, "jane@example.com");

        assertNotNull(response);
        assertEquals(ApplicationStatus.APPLIED.name(), response.getStatus());
        assertEquals("Jane Doe", response.getStudentName());
        assertEquals("Microsoft", response.getCompanyName());
    }

    @Test
    void testApplyForDrive_IneligibleThrowsException() {
        when(profileRepository.findByUserEmail("jane@example.com")).thenReturn(Optional.of(student));
        when(driveRepository.findById(5L)).thenReturn(Optional.of(drive));
        when(applicationRepository.existsByStudentProfileIdAndDriveId(10L, 5L)).thenReturn(false);
        when(placementDriveService.checkEligibility(5L, "jane@example.com"))
                .thenReturn(new EligibilityResponse(false, "CGPA too low", Collections.emptyList()));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                applicationService.applyForDrive(5L, "jane@example.com"));

        assertTrue(ex.getMessage().contains("not eligible"));
        verify(applicationRepository, never()).save(any());
    }

    @Test
    void testUpdateApplicationStatus_SelectedMarksStudentPlaced() {
        JobApplication app = new JobApplication(student, drive);
        app.setId(100L);
        app.setStatus(ApplicationStatus.APPLIED);

        when(applicationRepository.findById(100L)).thenReturn(Optional.of(app));
        when(applicationRepository.save(any(JobApplication.class))).thenReturn(app);

        UpdateApplicationStatusRequest request = new UpdateApplicationStatusRequest();
        request.setStatus("SELECTED");
        request.setCurrentRound(3);
        request.setFeedback("Selected with highest recommendations");

        JobApplicationResponse response = applicationService.updateApplicationStatus(100L, request);

        assertEquals("SELECTED", response.getStatus());
        assertTrue(student.getIsPlaced());
        verify(profileRepository, times(1)).save(student);
    }
}
