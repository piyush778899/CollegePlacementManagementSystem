package com.placement.config;

import com.placement.entity.*;
import com.placement.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final StudentProfileRepository profileRepository;
    private final CompanyRepository companyRepository;
    private final PlacementDriveRepository driveRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           StudentProfileRepository profileRepository,
                           CompanyRepository companyRepository,
                           PlacementDriveRepository driveRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.companyRepository = companyRepository;
        this.driveRepository = driveRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedUsersAndProfiles();
        seedCompaniesAndDrives();
    }

    private void seedUsersAndProfiles() {
        // 1. Admin
        if (!userRepository.existsByEmail("admin@college.edu")) {
            User admin = new User(
                    "Placement Director",
                    "admin@college.edu",
                    passwordEncoder.encode("Admin@123"),
                    Role.ADMIN
            );
            userRepository.save(admin);
        }

        // 2. Demo Student 1
        if (!userRepository.existsByEmail("rahul.sharma@college.edu")) {
            User rahul = new User(
                    "Rahul Sharma",
                    "rahul.sharma@college.edu",
                    passwordEncoder.encode("Student@123"),
                    Role.STUDENT
            );
            userRepository.save(rahul);

            StudentProfile profile1 = new StudentProfile();
            profile1.setUser(rahul);
            profile1.setRollNumber("2024CS101");
            profile1.setDepartment("CSE");
            profile1.setCgpa(8.75);
            profile1.setTenthPercentage(92.0);
            profile1.setTwelfthPercentage(89.5);
            profile1.setBacklogs(0);
            profile1.setPassingYear(2025);
            profile1.setPhone("+91 98765 43210");
            profile1.setGender("Male");
            profile1.setSkills("Java, Spring Boot, React, MySQL, Docker, DSA");
            profile1.setIsPlaced(false);
            profileRepository.save(profile1);
        }

        // 3. Demo Student 2
        if (!userRepository.existsByEmail("priya.patel@college.edu")) {
            User priya = new User(
                    "Priya Patel",
                    "priya.patel@college.edu",
                    passwordEncoder.encode("Student@123"),
                    Role.STUDENT
            );
            userRepository.save(priya);

            StudentProfile profile2 = new StudentProfile();
            profile2.setUser(priya);
            profile2.setRollNumber("2024IT204");
            profile2.setDepartment("IT");
            profile2.setCgpa(9.10);
            profile2.setTenthPercentage(95.0);
            profile2.setTwelfthPercentage(93.0);
            profile2.setBacklogs(0);
            profile2.setPassingYear(2025);
            profile2.setPhone("+91 91234 56789");
            profile2.setGender("Female");
            profile2.setSkills("Python, Machine Learning, SQL, AWS, Flask");
            profile2.setIsPlaced(false);
            profileRepository.save(profile2);
        }
    }

    private void seedCompaniesAndDrives() {
        if (companyRepository.count() > 0) return;

        // Company 1: Google
        Company google = new Company(
                "Google",
                "https://careers.google.com",
                "Technology",
                "Global leader in cloud, artificial intelligence, search, and software systems.",
                "campus-india@google.com",
                "+91 80 6721 8000",
                "Bengaluru / Hyderabad"
        );
        google.setLogoUrl("https://upload.wikimedia.org/wikipedia/commons/2/2f/Google_2015_logo.svg");
        companyRepository.save(google);

        // Company 2: Microsoft
        Company microsoft = new Company(
                "Microsoft",
                "https://careers.microsoft.com",
                "Technology & Cloud",
                "Empowering every person and every organization on the planet to achieve more.",
                "university-recruiting@microsoft.com",
                "+91 40 6695 0000",
                "Hyderabad / Noida / Bengaluru"
        );
        microsoft.setLogoUrl("https://upload.wikimedia.org/wikipedia/commons/9/96/Microsoft_logo_%282012%29.svg");
        companyRepository.save(microsoft);

        // Company 3: Amazon
        Company amazon = new Company(
                "Amazon",
                "https://amazon.jobs",
                "E-Commerce & Cloud Computing",
                "Earth's most customer-centric company, pioneering AWS and global logistics.",
                "campus-hiring@amazon.com",
                "+91 40 4000 5000",
                "Hyderabad / Bengaluru / Pune"
        );
        amazon.setLogoUrl("https://upload.wikimedia.org/wikipedia/commons/a/a9/Amazon_logo.svg");
        companyRepository.save(amazon);

        // Company 4: Deloitte
        Company deloitte = new Company(
                "Deloitte",
                "https://deloitte.com/careers",
                "Consulting & Advisory",
                "Leading professional services firm delivering audit, consulting, and financial advisory.",
                "campus@deloitte.com",
                "+91 22 6185 4000",
                "Mumbai / Gurugram / Bengaluru"
        );
        deloitte.setLogoUrl("https://upload.wikimedia.org/wikipedia/commons/1/15/Deloitte.svg");
        companyRepository.save(deloitte);

        // Placement Drive 1: Google SDE
        PlacementDrive drive1 = new PlacementDrive();
        drive1.setCompany(google);
        drive1.setTitle("Software Development Engineer - Campus 2025");
        drive1.setJobRole("Software Development Engineer (SDE-1)");
        drive1.setDescription("Build large-scale distributed systems, solve algorithmic problems, and design mission-critical software.");
        drive1.setPackageLpa(32.5);
        drive1.setLocation("Bengaluru, Karnataka");
        drive1.setDriveDate(LocalDate.now().plusDays(15));
        drive1.setDeadline(LocalDateTime.now().plusDays(10));
        drive1.setMinCgpa(8.0);
        drive1.setMaxBacklogs(0);
        drive1.setMinTenthPercentage(80.0);
        drive1.setMinTwelfthPercentage(80.0);
        drive1.setEligibleDepartments("CSE,IT,ECE");
        drive1.setStatus(DriveStatus.UPCOMING);
        driveRepository.save(drive1);

        // Placement Drive 2: Microsoft SDE
        PlacementDrive drive2 = new PlacementDrive();
        drive2.setCompany(microsoft);
        drive2.setTitle("Software Engineer - Core Engineering");
        drive2.setJobRole("Software Engineer");
        drive2.setDescription("Innovate and build Azure cloud services, developer tools, and cutting-edge operating system features.");
        drive2.setPackageLpa(28.0);
        drive2.setLocation("Hyderabad, Telangana");
        drive2.setDriveDate(LocalDate.now().plusDays(20));
        drive2.setDeadline(LocalDateTime.now().plusDays(14));
        drive2.setMinCgpa(7.5);
        drive2.setMaxBacklogs(0);
        drive2.setMinTenthPercentage(75.0);
        drive2.setMinTwelfthPercentage(75.0);
        drive2.setEligibleDepartments("CSE,IT,ECE,EE");
        drive2.setStatus(DriveStatus.UPCOMING);
        driveRepository.save(drive2);

        // Placement Drive 3: Amazon Cloud Support & SDE
        PlacementDrive drive3 = new PlacementDrive();
        drive3.setCompany(amazon);
        drive3.setTitle("AWS Cloud Associate & Systems Engineer");
        drive3.setJobRole("Cloud Support Engineer");
        drive3.setDescription("Work with AWS customers and architectures, diagnose distributed cloud workloads, and build automation.");
        drive3.setPackageLpa(18.5);
        drive3.setLocation("Hyderabad, Telangana");
        drive3.setDriveDate(LocalDate.now().plusDays(7));
        drive3.setDeadline(LocalDateTime.now().plusDays(4));
        drive3.setMinCgpa(7.0);
        drive3.setMaxBacklogs(1);
        drive3.setMinTenthPercentage(70.0);
        drive3.setMinTwelfthPercentage(70.0);
        drive3.setEligibleDepartments("ALL");
        drive3.setStatus(DriveStatus.ONGOING);
        driveRepository.save(drive3);

        // Placement Drive 4: Deloitte Technology Analyst
        PlacementDrive drive4 = new PlacementDrive();
        drive4.setCompany(deloitte);
        drive4.setTitle("Technology Consulting Analyst");
        drive4.setJobRole("Associate Analyst");
        drive4.setDescription("Partner with Fortune 500 enterprises on digital transformation, ERP implementations, and cyber security.");
        drive4.setPackageLpa(11.0);
        drive4.setLocation("Mumbai / Gurugram");
        drive4.setDriveDate(LocalDate.now().plusDays(25));
        drive4.setDeadline(LocalDateTime.now().plusDays(18));
        drive4.setMinCgpa(6.5);
        drive4.setMaxBacklogs(1);
        drive4.setMinTenthPercentage(65.0);
        drive4.setMinTwelfthPercentage(65.0);
        drive4.setEligibleDepartments("ALL");
        drive4.setStatus(DriveStatus.UPCOMING);
        driveRepository.save(drive4);
    }
}
