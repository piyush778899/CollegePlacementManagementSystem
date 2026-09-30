package com.placement.service;

import com.placement.dto.StudentProfileRequest;
import com.placement.dto.StudentProfileResponse;
import com.placement.entity.StudentProfile;
import com.placement.entity.User;
import com.placement.exception.ResourceNotFoundException;
import com.placement.repository.StudentProfileRepository;
import com.placement.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentProfileService {

    private final StudentProfileRepository profileRepository;
    private final UserRepository userRepository;

    public StudentProfileService(StudentProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public StudentProfileResponse getProfileByEmail(String email) {
        StudentProfile profile = profileRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found for user: " + email));
        return mapToResponse(profile);
    }

    @Transactional(readOnly = true)
    public StudentProfileResponse getProfileById(Long id) {
        StudentProfile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found with id: " + id));
        return mapToResponse(profile);
    }

    @Transactional(readOnly = true)
    public List<StudentProfileResponse> getAllProfiles() {
        return profileRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public StudentProfileResponse saveOrUpdateProfile(String userEmail, StudentProfileRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));

        StudentProfile profile = profileRepository.findByUser(user)
                .orElseGet(() -> {
                    StudentProfile p = new StudentProfile();
                    p.setUser(user);
                    return p;
                });

        // Validate roll number uniqueness if changed
        if (!request.getRollNumber().equalsIgnoreCase(profile.getRollNumber())) {
            if (profileRepository.existsByRollNumber(request.getRollNumber())) {
                throw new IllegalArgumentException("Roll number already registered: " + request.getRollNumber());
            }
        }

        profile.setRollNumber(request.getRollNumber().toUpperCase().trim());
        profile.setDepartment(request.getDepartment().toUpperCase().trim());
        profile.setCgpa(request.getCgpa());
        profile.setTenthPercentage(request.getTenthPercentage());
        profile.setTwelfthPercentage(request.getTwelfthPercentage());
        profile.setBacklogs(request.getBacklogs());
        profile.setPassingYear(request.getPassingYear());
        profile.setPhone(request.getPhone());
        profile.setGender(request.getGender());
        profile.setSkills(request.getSkills());
        profile.setResumeUrl(request.getResumeUrl());

        StudentProfile saved = profileRepository.save(profile);
        return mapToResponse(saved);
    }

    public StudentProfileResponse mapToResponse(StudentProfile profile) {
        StudentProfileResponse resp = new StudentProfileResponse();
        resp.setId(profile.getId());
        resp.setUserId(profile.getUser().getId());
        resp.setStudentName(profile.getUser().getFullName());
        resp.setStudentEmail(profile.getUser().getEmail());
        resp.setRollNumber(profile.getRollNumber());
        resp.setDepartment(profile.getDepartment());
        resp.setCgpa(profile.getCgpa());
        resp.setTenthPercentage(profile.getTenthPercentage());
        resp.setTwelfthPercentage(profile.getTwelfthPercentage());
        resp.setBacklogs(profile.getBacklogs());
        resp.setPassingYear(profile.getPassingYear());
        resp.setPhone(profile.getPhone());
        resp.setGender(profile.getGender());
        resp.setSkills(profile.getSkills());
        resp.setResumeUrl(profile.getResumeUrl());
        resp.setIsPlaced(profile.getIsPlaced());
        return resp;
    }
}
