package com.placement.service;

import com.placement.dto.EligibilityResponse;
import com.placement.dto.PlacementDriveRequest;
import com.placement.dto.PlacementDriveResponse;
import com.placement.entity.Company;
import com.placement.entity.PlacementDrive;
import com.placement.entity.StudentProfile;
import com.placement.entity.User;
import com.placement.repository.CompanyRepository;
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
class PlacementDriveServiceTest {

    @Mock
    private PlacementDriveRepository driveRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private StudentProfileRepository studentProfileRepository;

    @Mock
    private JobApplicationRepository jobApplicationRepository;

    @InjectMocks
    private PlacementDriveService driveService;

    private PlacementDrive drive;
    private StudentProfile eligibleStudent;
    private StudentProfile ineligibleStudent;

    @BeforeEach
    void setUp() {
        Company company = new Company();
        company.setId(1L);
        company.setName("Google");
        company.setLocation("Bangalore");

        drive = new PlacementDrive();
        drive.setId(10L);
        drive.setCompany(company);
        drive.setTitle("Software Engineer 2026");
        drive.setJobRole("Software Engineer");
        drive.setPackageLpa(24.0);
        drive.setMinCgpa(7.5);
        drive.setMaxBacklogs(0);
        drive.setMinTenthPercentage(70.0);
        drive.setMinTwelfthPercentage(70.0);
        drive.setEligibleDepartments("CSE,IT");

        User user1 = new User("Alice", "alice@example.com", "pass", com.placement.entity.Role.STUDENT);
        eligibleStudent = new StudentProfile();
        eligibleStudent.setId(100L);
        eligibleStudent.setUser(user1);
        eligibleStudent.setRollNumber("CS101");
        eligibleStudent.setDepartment("CSE");
        eligibleStudent.setCgpa(8.5);
        eligibleStudent.setBacklogs(0);
        eligibleStudent.setTenthPercentage(85.0);
        eligibleStudent.setTwelfthPercentage(88.0);

        User user2 = new User("Bob", "bob@example.com", "pass", com.placement.entity.Role.STUDENT);
        ineligibleStudent = new StudentProfile();
        ineligibleStudent.setId(200L);
        ineligibleStudent.setUser(user2);
        ineligibleStudent.setRollNumber("ME101");
        ineligibleStudent.setDepartment("Mechanical");
        ineligibleStudent.setCgpa(6.8);
        ineligibleStudent.setBacklogs(1);
        ineligibleStudent.setTenthPercentage(65.0);
        ineligibleStudent.setTwelfthPercentage(68.0);
    }

    @Test
    void testCheckEligibility_EligibleStudent() {
        when(driveRepository.findById(10L)).thenReturn(Optional.of(drive));
        when(studentProfileRepository.findByUserEmail("alice@example.com")).thenReturn(Optional.of(eligibleStudent));

        EligibilityResponse response = driveService.checkEligibility(10L, "alice@example.com");

        assertTrue(response.isEligible());
        assertTrue(response.getChecks().stream().allMatch(EligibilityResponse.CriterionCheck::isPassed));
    }

    @Test
    void testCheckEligibility_IneligibleStudent() {
        when(driveRepository.findById(10L)).thenReturn(Optional.of(drive));
        when(studentProfileRepository.findByUserEmail("bob@example.com")).thenReturn(Optional.of(ineligibleStudent));

        EligibilityResponse response = driveService.checkEligibility(10L, "bob@example.com");

        assertFalse(response.isEligible());
        long failedChecks = response.getChecks().stream().filter(c -> !c.isPassed()).count();
        assertTrue(failedChecks > 0);
    }

    @Test
    void testCreateDrive() {
        Company company = new Company();
        company.setId(2L);
        company.setName("Amazon");

        PlacementDriveRequest request = new PlacementDriveRequest();
        request.setCompanyId(2L);
        request.setTitle("SDE 1");
        request.setJobRole("SDE");
        request.setPackageLpa(18.5);
        request.setMinCgpa(7.0);

        when(companyRepository.findById(2L)).thenReturn(Optional.of(company));
        when(driveRepository.save(any(PlacementDrive.class))).thenAnswer(i -> {
            PlacementDrive d = i.getArgument(0);
            d.setId(99L);
            return d;
        });
        when(jobApplicationRepository.findByDriveId(99L)).thenReturn(Collections.emptyList());

        PlacementDriveResponse response = driveService.createDrive(request);

        assertNotNull(response);
        assertEquals("SDE 1", response.getTitle());
        assertEquals("Amazon", response.getCompanyName());
        assertEquals(18.5, response.getPackageLpa());
    }
}
