package com.placement.dto;

public class StudentProfileResponse {

    private Long id;
    private Long userId;
    private String studentName;
    private String studentEmail;
    private String rollNumber;
    private String department;
    private Double cgpa;
    private Double tenthPercentage;
    private Double twelfthPercentage;
    private Integer backlogs;
    private Integer passingYear;
    private String phone;
    private String gender;
    private String skills;
    private String resumeUrl;
    private Boolean isPlaced;

    public StudentProfileResponse() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }

    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public Double getCgpa() { return cgpa; }
    public void setCgpa(Double cgpa) { this.cgpa = cgpa; }

    public Double getTenthPercentage() { return tenthPercentage; }
    public void setTenthPercentage(Double tenthPercentage) { this.tenthPercentage = tenthPercentage; }

    public Double getTwelfthPercentage() { return twelfthPercentage; }
    public void setTwelfthPercentage(Double twelfthPercentage) { this.twelfthPercentage = twelfthPercentage; }

    public Integer getBacklogs() { return backlogs; }
    public void setBacklogs(Integer backlogs) { this.backlogs = backlogs; }

    public Integer getPassingYear() { return passingYear; }
    public void setPassingYear(Integer passingYear) { this.passingYear = passingYear; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public String getResumeUrl() { return resumeUrl; }
    public void setResumeUrl(String resumeUrl) { this.resumeUrl = resumeUrl; }

    public Boolean getIsPlaced() { return isPlaced; }
    public void setIsPlaced(Boolean placed) { isPlaced = placed; }
}
