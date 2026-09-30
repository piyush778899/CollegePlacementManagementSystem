# COLLEGE PLACEMENT MANAGEMENT SYSTEM (CPMS)
## Comprehensive Technical Project Report

---

### **Document Information**
- **Project Title:** College Placement Management System
- **Domain:** Enterprise Academic & Recruitment Automation
- **Architecture:** Decoupled Full-Stack Architecture (REST API + Single Page Application)
- **Backend Stack:** Java 17, Spring Boot 3.2.6, Spring Data JPA, Hibernate, Spring Security, JWT, MySQL 8.0
- **Frontend Stack:** React 19, TypeScript, Vite 8, React Router v7, Axios, Vanilla CSS Design System
- **Testing Frameworks:** JUnit 5, Mockito, Spring Boot Test
- **Document Version:** 1.0.0
- **Date:** September 2026

---

## Table of Contents
1. [Executive Summary / Abstract](#1-executive-summary--abstract)
2. [Introduction & Problem Statement](#2-introduction--problem-statement)
   - 2.1 Background
   - 2.2 Challenges in Traditional Placement Management
   - 2.3 Proposed Solution & Project Objectives
   - 2.4 Scope of the Project
3. [System Requirements Specification (SRS)](#3-system-requirements-specification-srs)
   - 3.1 User Roles & Personas
   - 3.2 Functional Requirements
   - 3.3 Non-Functional Requirements
   - 3.4 Hardware & Software Environment
4. [System Architecture & System Design](#4-system-architecture--system-design)
   - 4.1 High-Level Architectural Pattern
   - 4.2 Module Decomposition
   - 4.3 Entity-Relationship (ER) Model & Database Schema
   - 4.4 Data Flow & Sequence Diagrams
5. [Core Algorithms & Business Logic](#5-core-algorithms--business-logic)
   - 5.1 Automated 5-Point Eligibility Evaluation Engine
   - 5.2 Application Lifecycle & Auto-Placement State Machine
   - 5.3 Stateless JWT Authentication & RBAC Security Filter Chain
6. [RESTful API Specifications](#6-restful-api-specifications)
7. [Frontend Architecture & UI Modules](#7-frontend-architecture--ui-modules)
   - 7.1 Single Page Application Structure
   - 7.2 UI Design System & Component Library
   - 7.3 Page-Level Modules & User Workflows
8. [Testing & Quality Assurance](#8-testing--quality-assurance)
   - 8.1 Testing Strategy
   - 8.2 Unit Testing & Service Mocking (JUnit 5 + Mockito)
   - 8.3 Test Case Execution Matrix
9. [Deployment & Configuration Guide](#9-deployment--configuration-guide)
   - 9.1 Database Initialization
   - 9.2 Backend Configuration & Execution
   - 9.3 Frontend Build & Execution
10. [Conclusion & Future Enhancements](#10-conclusion--future-enhancements)
    - 10.1 Project Summary & Achievements
    - 10.2 Roadmap for Future Enhancements

---

## 1. Executive Summary / Abstract

The **College Placement Management System (CPMS)** is an enterprise-grade full-stack web application designed to digitize, streamline, and accelerate the campus recruitment lifecycle in higher education institutions. The placement process involves rigorous coordination among training and placement officers (TPOs), corporate recruiters, and graduating students. Traditional campus recruitment workflows depend heavily on manual spreadsheets, paper records, disconnected email chains, and manual eligibility checks, leading to data redundancy, clerical delays, missed deadlines, and ineligible candidate submissions.

CPMS addresses these pain points by offering a centralized, secure, role-based platform. Key capabilities include:
- **Role-Based Access Control (RBAC)** across `STUDENT`, `COMPANY`, and `ADMIN` profiles.
- **Automated 5-Point Eligibility Evaluation Engine** that instantaneously verifies student credentials (CGPA, active backlogs, 10th percentage, 12th percentage, and academic department) against placement drive criteria.
- **Multi-Round Recruitment Pipeline Tracking** with structured progress tracking through aptitude, technical interviews, and HR discussions.
- **Automated Placement State Transitions**, ensuring that when a student accepts or secures an offer, their profile status is automatically synchronized across the institutional registry.
- **Institutional Analytics Dashboard**, offering real-time KPI metrics such as placement percentage, department-wise clearance rates, average salary packages, and active company drives.

---

## 2. Introduction & Problem Statement

### 2.1 Background
In university ecosystems, the Training and Placement Cell acts as the critical bridge connecting student talent with corporate organizations. With thousands of students graduating across diverse disciplines and dozens of companies conducting recruitment drives simultaneously, managing student resumes, company criteria, drive schedules, student shortlists, and interview feedback poses significant operational complexity.

### 2.2 Challenges in Traditional Placement Management
1. **Manual Eligibility Verification:** Filtering hundreds of student applications against specific criteria (e.g., minimum 7.5 CGPA, no active backlogs, specific engineering disciplines) using spreadsheets is slow and prone to human error.
2. **Lack of Real-Time Visibility:** Students often lack timely visibility into drive schedules, eligibility rules, and interview statuses, leading to confusion and missed interview calls.
3. **Data Inconsistency & Redundancy:** Disconnected spreadsheets create discrepancies regarding whether a student has already been placed or remains eligible for subsequent drives under institutional placement policies.
4. **Administrative Overhead:** Placement officers spend disproportionate amounts of time aggregating records, sending circulars, and collating recruitment round outcomes instead of focusing on corporate relations.

### 2.3 Proposed Solution & Project Objectives
The objective of this project is to engineer an integrated, automated, end-to-end placement portal that achieves:
- **Zero-Friction Candidate Screening:** Automated algorithmic validation before a student can register for any drive.
- **Transparent Recruitment Lifecycle:** Granular status tracking (`APPLIED`, `SHORTLISTED`, `IN_PROGRESS`, `SELECTED`, `REJECTED`) with interview round updates.
- **Data Integrity & Security:** Industry-standard password encryption (BCrypt) and cryptographically signed JSON Web Tokens (JWT) enforcing RBAC.
- **Actionable Business Intelligence:** Visual dashboards displaying recruitment statistics for administrators and department heads.

### 2.4 Scope of the Project
- Student registration, profile compilation, academic history tracking, and resume URL management.
- Company registration, tier classifications (Tier-1, Tier-2, Core, IT), and verification workflow.
- Placement drive authoring, schedule coordination, criteria parameterization, and round definitions.
- Application submission, live candidate eligibility checking, round progression, and result publication.
- Aggregate statistical reporting for institutional analysis.

---

## 3. System Requirements Specification (SRS)

### 3.1 User Roles & Personas

| Role | Target Persona | Key Responsibilities & Permissions |
|---|---|---|
| **STUDENT** | Graduating candidate | Register/login, complete academic profile, view active placement drives, perform instant self-eligibility checks, apply for eligible drives, monitor personal applications and interview round stages. |
| **COMPANY** | Corporate Recruiter | Manage organization profile, create and edit placement drives, define multi-round interview stages, view applicant pools, advance candidates through rounds, publish hiring results. |
| **ADMIN** | Placement Officer / TPO | Full administrative governance: verify companies, manage/cancel drives, monitor all student records and application statuses, access institutional KPI analytics. |

### 3.2 Functional Requirements

#### Module 1: Authentication & Authorization
- **FR-1.1:** Secure user registration with email, password, and assigned role.
- **FR-1.2:** Authentication using BCrypt password hashing and issuance of stateless JWT tokens containing subject, roles, and expiration claims.
- **FR-1.3:** Protected API routes gated by role authorization filters.

#### Module 2: Student Profile Management
- **FR-2.1:** Maintain profile data including full name, contact number, roll number, academic department, CGPA, 10th percentage, 12th percentage, count of active backlogs, resume link, and placement status (`isPlaced`).
- **FR-2.2:** Real-time updates to academic parameters with immediate impact on drive eligibility.

#### Module 3: Company & Placement Drive Management
- **FR-3.1:** Maintain company directory including industry domain, company tier, location, contact information, and verification status.
- **FR-3.2:** Create drives specifying job title, description, package (CTC in LPA), work location, minimum CGPA, maximum backlogs, minimum 10th and 12th percentages, eligible departments, drive date, and registration deadline.
- **FR-3.3:** Configure dynamic recruitment rounds (e.g., Round 1: Online Assessment, Round 2: Technical Interview, Round 3: HR Discussion).

#### Module 4: Eligibility & Application Processing
- **FR-4.1:** Automated validation preventing students from applying to drives for which they fail criteria.
- **FR-4.2:** Detailed diagnostic feedback indicating which specific criteria were unmet (e.g., "Active backlogs 2 exceed allowed maximum 0").
- **FR-4.3:** Prevent duplicate applications for the same drive.

#### Module 5: Recruitment Tracking & Offer Finalization
- **FR-5.1:** Step-by-step round updates (`currentRound` increment) with recruiter feedback comments.
- **FR-5.2:** Status transitions across `APPLIED`, `SHORTLISTED`, `IN_PROGRESS`, `SELECTED`, and `REJECTED`.
- **FR-5.3:** Automatically mark the candidate's `isPlaced` attribute to `true` when status changes to `SELECTED`.

#### Module 6: Analytics & Dashboard
- **FR-6.1:** Calculate real-time placement percentage (`(placedStudents / totalStudents) * 100`).
- **FR-6.2:** Aggregate total companies, active drives, total applications, and successful offers.
- **FR-6.3:** Breakdown of placement performance by department.

### 3.3 Non-Functional Requirements
- **Security:** Stateless session handling via standard HTTP Bearer JWT; passwords salted and hashed using BCrypt (cost factor 10); strict CORS origin validation.
- **Performance:** Sub-100ms response time on eligibility queries using indexed foreign keys and JPA relational mappings.
- **Maintainability:** Layered architectural pattern (Controller, Service, Repository, DTO, Entity).
- **Usability:** Responsive, modern CSS-driven user interface with clear visual hierarchy, accessible contrast ratios, and instant user feedback.

### 3.4 Hardware & Software Environment

#### Development & Target Environment
- **Operating System:** Cross-platform (Windows 10/11, Linux Ubuntu 22.04+, macOS)
- **Runtime Environment:** Java Development Kit (JDK) 17 LTS, Node.js v18.0+
- **Database Engine:** MySQL 8.0 Community / Enterprise Server
- **Build Tools:** Apache Maven 3.8+, Vite 8.3
- **Primary IDEs:** Eclipse IDE / Spring Tool Suite / IntelliJ IDEA / VS Code

---

## 4. System Architecture & System Design

### 4.1 High-Level Architectural Pattern
The system is built on a modern **Decoupled Client-Server (Tiered) Architecture**:
1. **Presentation Tier (Frontend):** Built with React 19 and TypeScript, bundled by Vite. Communicates with the backend exclusively via asynchronous REST calls over HTTPS using Axios.
2. **Application Tier (Backend):** Built with Spring Boot 3.2.x. Implements an API gateway and security filter chain, routing requests to specialized services containing business logic.
3. **Persistence Tier (Database):** MySQL 8.0 relational database, abstracted through Hibernate ORM and Spring Data JPA repositories.

```
+-----------------------------------------------------------+
|                   Client Browser (User)                   |
|   React 19 + TypeScript SPA (Vite Dev Server / Build)     |
+-----------------------------+-----------------------------+
                              | HTTP / JSON (Axios)
                              v
+-----------------------------------------------------------+
|               Spring Boot 3.2.6 Backend Core              |
|                                                           |
|  +-----------------------------------------------------+  |
|  |     SecurityFilterChain & JwtAuthenticationFilter   |  |
|  +--------------------------+--------------------------+  |
|                             |                             |
|  +--------------------------v--------------------------+  |
|  |                   REST Controllers                  |  |
|  |  (AuthController, StudentController, DriveController,  |
|  |   CompanyController, ApplicationController, etc.)   |  |
|  +--------------------------+--------------------------+  |
|                             |                             |
|  +--------------------------v--------------------------+  |
|  |                    Service Layer                    |  |
|  |  (PlacementDriveService, JobApplicationService,    |  |
|  |   AuthService, StudentProfileService, etc.)        |  |
|  +--------------------------+--------------------------+  |
|                             |                             |
|  +--------------------------v--------------------------+  |
|  |               Spring Data JPA Repositories          |  |
|  +--------------------------+--------------------------+  |
+-----------------------------+-----------------------------+
                              | Hibernate ORM / JDBC
                              v
+-----------------------------------------------------------+
|                     MySQL 8.0 Database                    |
|   (users, student_profiles, companies, placement_drives,  |
|    job_applications, recruitment_rounds)                  |
+-----------------------------------------------------------+
```

### 4.2 Module Decomposition

```
CollegePlacement/
├── backend/
│   ├── src/main/java/com/placement/
│   │   ├── config/            # CORS configuration, SecurityFilterChain beans
│   │   ├── controller/        # REST endpoints exposing JSON APIs
│   │   ├── dto/               # Data Transfer Objects (Requests & Responses)
│   │   ├── entity/            # Hibernate JPA Entities & Enumerations
│   │   ├── exception/         # Custom exceptions & GlobalExceptionHandler
│   │   ├── repository/        # Spring Data JPA CRUD & custom query interfaces
│   │   ├── security/          # JWT Token Provider, UserDetailsService, AuthFilter
│   │   ├── service/           # Transactional domain logic & validation rules
│   │   └── util/              # Constant definitions and utility helpers
│   └── src/main/resources/    # application.properties & database configuration
└── frontend/
    ├── src/
    │   ├── api/               # Axios client instance with Bearer interceptors
    │   ├── components/        # Reusable UI elements (Navbar, Footer, ProtectedRoute)
    │   ├── pages/             # Route views (Home, Login, Dashboard, Drives, Profile)
    │   ├── types/             # Domain TypeScript interfaces & enums
    │   └── App.tsx            # Route registration & global auth state provider
```

### 4.3 Entity-Relationship (ER) Model & Database Schema

The database design adheres to Third Normal Form (3NF) principles:

```
 +--------------------+             +------------------------+
 |       USERS        | 1         1 |    STUDENT_PROFILES    |
 +--------------------+-------------+------------------------+
 | id (PK)            |             | id (PK)                |
 | email (UNIQUE)     |             | user_id (FK)           |
 | password           |             | roll_number (UNIQUE)   |
 | role (ENUM)        |             | department             |
 | full_name          |             | cgpa                   |
 | phone              |             | tenth_percentage       |
 | created_at         |             | twelfth_percentage     |
 +--------------------+             | backlogs               |
                                    | resume_url             |
                                    | is_placed              |
                                    +-----------+------------+
                                                | 1
                                                |
                                                | has many
                                                v *
 +--------------------+ 1         * +------------------------+
 |     COMPANIES      |-------------|    JOB_APPLICATIONS    |
 +--------------------+             +------------------------+
 | id (PK)            |             | id (PK)                |
 | name               |             | student_profile_id (FK)|
 | email              |             | placement_drive_id (FK)|
 | website            |             | status (ENUM)          |
 | location           |             | current_round          |
 | tier               |             | feedback               |
 | is_verified        |             | applied_at             |
 +---------+----------+             +-----------^------------+
           | 1                                  | *
           | hosts                              | belongs to
           v *                                  |
 +--------------------+ 1         *             |
 |  PLACEMENT_DRIVES  |-------------------------+
 +--------------------+
 | id (PK)            |
 | company_id (FK)    | 1
 | job_title          |
 | package_lpa        | has many
 | location           |
 | min_cgpa           |
 | max_backlogs       |
 | min_tenth_pct      |
 | min_twelfth_pct    |
 | eligible_dept      |
 | status (ENUM)      |
 +---------+----------+
           | 1
           | has many
           v *
 +--------------------+
 | RECRUITMENT_ROUNDS |
 +--------------------+
 | id (PK)            |
 | drive_id (FK)      |
 | round_number       |
 | round_name         |
 | description        |
 +--------------------+
```

#### Detailed Database Table Definitions

1. **`users`**
   - `id`: BIGINT (Auto Increment, Primary Key)
   - `email`: VARCHAR(100) (Unique, Not Null)
   - `password`: VARCHAR(255) (Not Null, BCrypt Encrypted)
   - `role`: ENUM ('STUDENT', 'COMPANY', 'ADMIN') (Not Null)
   - `full_name`: VARCHAR(100) (Not Null)
   - `phone`: VARCHAR(20)

2. **`student_profiles`**
   - `id`: BIGINT (Auto Increment, Primary Key)
   - `user_id`: BIGINT (Foreign Key referencing `users(id)`, Unique)
   - `roll_number`: VARCHAR(50) (Unique)
   - `department`: VARCHAR(100)
   - `cgpa`: DOUBLE PRECISION (e.g., 8.75)
   - `tenth_percentage`: DOUBLE PRECISION
   - `twelfth_percentage`: DOUBLE PRECISION
   - `backlogs`: INT (Default: 0)
   - `resume_url`: VARCHAR(255)
   - `is_placed`: BOOLEAN (Default: false)

3. **`companies`**
   - `id`: BIGINT (Auto Increment, Primary Key)
   - `name`: VARCHAR(100) (Not Null)
   - `email`: VARCHAR(100)
   - `website`: VARCHAR(255)
   - `location`: VARCHAR(100)
   - `tier`: VARCHAR(50) (e.g., Tier-1, Tier-2, Core)
   - `is_verified`: BOOLEAN (Default: true)

4. **`placement_drives`**
   - `id`: BIGINT (Auto Increment, Primary Key)
   - `company_id`: BIGINT (Foreign Key referencing `companies(id)`)
   - `job_title`: VARCHAR(100) (Not Null)
   - `description`: TEXT
   - `package_lpa`: DOUBLE PRECISION (e.g., 12.5)
   - `location`: VARCHAR(100)
   - `min_cgpa`: DOUBLE PRECISION (Default: 0.0)
   - `max_backlogs`: INT (Default: 0)
   - `min_tenth_percentage`: DOUBLE PRECISION (Default: 0.0)
   - `min_twelfth_percentage`: DOUBLE PRECISION (Default: 0.0)
   - `eligible_departments`: VARCHAR(255) (Comma-separated or 'ALL')
   - `drive_date`: DATE
   - `deadline`: DATE
   - `status`: ENUM ('UPCOMING', 'ACTIVE', 'COMPLETED', 'CANCELLED')

5. **`recruitment_rounds`**
   - `id`: BIGINT (Auto Increment, Primary Key)
   - `drive_id`: BIGINT (Foreign Key referencing `placement_drives(id)`)
   - `round_number`: INT (e.g., 1, 2, 3)
   - `round_name`: VARCHAR(100) (e.g., 'Aptitude Test', 'Technical Round')
   - `description`: VARCHAR(255)

6. **`job_applications`**
   - `id`: BIGINT (Auto Increment, Primary Key)
   - `student_profile_id`: BIGINT (Foreign Key referencing `student_profiles(id)`)
   - `placement_drive_id`: BIGINT (Foreign Key referencing `placement_drives(id)`)
   - `status`: ENUM ('APPLIED', 'SHORTLISTED', 'IN_PROGRESS', 'SELECTED', 'REJECTED')
   - `current_round`: INT (Default: 1)
   - `feedback`: TEXT
   - `applied_at`: TIMESTAMP (Default: CURRENT_TIMESTAMP)

---

## 5. Core Algorithms & Business Logic

### 5.1 Automated 5-Point Eligibility Evaluation Engine
Rather than relying on manual gatekeeping, the system encapsulates an automated 5-point verification algorithm in [`PlacementDriveService.java`](file:///d:/Piyush/eclipse-workspace/CollegePlacement/backend/src/main/java/com/placement/service/PlacementDriveService.java). 

When a student requests eligibility verification or attempts to apply, the engine executes five deterministic checks:
1. **Academic Merit Check:** Candidate's current CGPA $\ge$ Drive minimum CGPA.
2. **Backlog Clearance Check:** Candidate's active backlogs $\le$ Drive maximum backlogs allowed.
3. **Secondary Education Check:** Candidate's 10th grade percentage $\ge$ Drive minimum 10th percentage.
4. **Higher Secondary Check:** Candidate's 12th grade percentage $\ge$ Drive minimum 12th percentage.
5. **Discipline / Branch Compatibility:** Drive `eligibleDepartments` contains the candidate's department (or is marked `ALL`).

```
                    +---------------------------+
                    |  Eligibility Check Start  |
                    +-------------+-------------+
                                  |
                                  v
                    +---------------------------+
                    |  Is Profile Completed?    |
                    +-------------+-------------+
                           /              \
                     NO   /                \  YES
                         v                  v
       +-----------------------+     +-------------------------------+
       | Return Ineligible:    |     | 1. CGPA >= Min CGPA?          |
       | "Profile Incomplete"  |     | 2. Backlogs <= Max Backlogs?  |
       +-----------------------+     | 3. 10th % >= Min 10th %?      |
                                     | 4. 12th % >= Min 12th %?      |
                                     | 5. Dept in Eligible Depts?    |
                                     +---------------+---------------+
                                                     |
                                            +--------+--------+
                                            |                 |
                                      All Passed?         Any Failed?
                                            |                 |
                                            v                 v
                               +----------------+   +-------------------+
                               | Eligible = True|   | Eligible = False  |
                               | "You meet all  |   | Compile list of   |
                               |  requirements" |   | failed criteria   |
                               +----------------+   +-------------------+
```

Each criterion is evaluated and packaged into a detailed diagnostic payload (`EligibilityResponse.CriterionCheck`), providing transparency directly to the student on the UI.

### 5.2 Application Lifecycle & Auto-Placement State Machine
The application lifecycle transitions through strict states managed by [`JobApplicationService.java`](file:///d:/Piyush/eclipse-workspace/CollegePlacement/backend/src/main/java/com/placement/service/JobApplicationService.java):

```
 [Student Applies] ---> (APPLIED)
                            |
                   (SHORTLISTED)
                            |
                   (IN_PROGRESS) <--- [Advance Recruitment Round: 1 -> 2 -> 3...]
                          /     \
                         /       \
            [Rejected]  /         \ [Recruiter Marks Selected]
                       v           v
                  (REJECTED)    (SELECTED)
                                     |
                                     v
                        [Trigger Side-Effect:
                         Update student_profiles.is_placed = TRUE]
```

**Key Business Rule:** When a recruiter updates an application's status to `SELECTED`, the system executes an atomic transaction that sets `studentProfile.setIsPlaced(true)` and commits it to the institutional student registry.

### 5.3 Stateless JWT Authentication & RBAC Security Filter Chain
Security is orchestrated via Spring Security 6 / Spring Boot 3 filters:
- **`JwtTokenProvider`:** Generates HMAC-SHA256 signatures with configured expiration durations. Tokens package the user's principal and authorities (`ROLE_STUDENT`, `ROLE_COMPANY`, `ROLE_ADMIN`).
- **`JwtAuthenticationFilter`:** Intercepts incoming HTTP requests, extracts the `Authorization: Bearer <token>` header, validates token integrity and expiration, and hydrates the `SecurityContextHolder`.
- **CORS Configuration:** Enables secure cross-origin communication with frontend dev and production servers (`http://localhost:5173`) while denying unauthorized origins.

---

## 6. RESTful API Specifications

The backend exposes a structured, REST-compliant API:

| HTTP Verb | Resource Route | Target Functionality | Security / Role Requirement |
|---|---|---|---|
| `GET` | `/api/health` | Service health & uptime probe | Public |
| `POST` | `/api/auth/register` | Register new user account | Public |
| `POST` | `/api/auth/login` | Authenticate and obtain JWT token | Public |
| `GET` | `/api/auth/me` | Fetch active user credentials & role | Authenticated |
| `GET` | `/api/students/profile` | Retrieve authenticated student profile | Authenticated (`STUDENT`) |
| `PUT` | `/api/students/profile` | Update or initialize student profile | Authenticated (`STUDENT`) |
| `GET` | `/api/students` | List all registered student profiles | `ADMIN`, `COMPANY` |
| `GET` | `/api/students/{id}` | Retrieve specific student profile | `ADMIN`, `COMPANY` |
| `GET` | `/api/companies` | Browse list of registered companies | Public |
| `GET` | `/api/companies/{id}` | View detailed company profile | Public |
| `POST` | `/api/companies` | Register new partner company | `ADMIN`, `COMPANY` |
| `PUT` | `/api/companies/{id}` | Update company credentials/tier | `ADMIN`, `COMPANY` |
| `DELETE`| `/api/companies/{id}` | Deregister partner company | `ADMIN` |
| `GET` | `/api/drives` | Fetch placement drives with eligibility | Public / Authenticated |
| `GET` | `/api/drives/{id}` | Get placement drive details | Public / Authenticated |
| `POST` | `/api/drives` | Author new placement drive | `ADMIN`, `COMPANY` |
| `PUT` | `/api/drives/{id}` | Edit existing placement drive | `ADMIN`, `COMPANY` |
| `DELETE`| `/api/drives/{id}` | Cancel/delete placement drive | `ADMIN` |
| `GET` | `/api/drives/{id}/eligibility` | Evaluate 5-point student eligibility | Authenticated (`STUDENT`) |
| `GET` | `/api/drives/{id}/rounds` | Fetch recruitment rounds for drive | Public / Authenticated |
| `POST` | `/api/drives/{id}/rounds` | Add round stage to recruitment drive | `ADMIN`, `COMPANY` |
| `DELETE`| `/api/rounds/{id}` | Delete recruitment round stage | `ADMIN`, `COMPANY` |
| `POST` | `/api/applications/apply/{driveId}` | Apply to drive (with eligibility gate) | Authenticated (`STUDENT`) |
| `GET` | `/api/applications/my-applications` | Track personal application history | Authenticated (`STUDENT`) |
| `GET` | `/api/applications/drive/{driveId}` | View candidate applications for a drive | `ADMIN`, `COMPANY` |
| `PATCH` | `/api/applications/{id}/status` | Update status, round & feedback | `ADMIN`, `COMPANY` |
| `GET` | `/api/dashboard/stats` | Aggregate campus placement KPI metrics | Public / Authenticated |

---

## 7. Frontend Architecture & UI Modules

### 7.1 Single Page Application Structure
The frontend is engineered as a modern Single Page Application (SPA) utilizing React 19, TypeScript, and React Router v7. State persistence of authentication tokens is maintained in browser `localStorage`, with global authentication context shared via React Context.

### 7.2 UI Design System & Component Library
The user interface follows a curated design system built with custom CSS:
- **Typography:** Modern typography stack with optimized letter-spacing and hierarchy.
- **Color Palette:** Deep dark and high-contrast light surfaces accented with professional blues, emerald greens for success states, and amber for in-progress stages.
- **Glassmorphism & Depth:** Soft borders, subtle box shadows, and translucent backdrop filters provide visual clarity.
- **Micro-Interactions:** Smooth CSS hover transitions on cards, buttons, and status badges.

### 7.3 Page-Level Modules & User Workflows

1. **Home View (`Home.tsx`):**
   - High-impact landing page highlighting institutional placement records, top recruiting companies, workflow summaries, and direct call-to-actions.
2. **Authentication (`Login.tsx`):**
   - Dynamic tabbed interface toggling between Login and Registration. Supports role selection (`STUDENT`, `COMPANY`, `ADMIN`).
3. **Student Profile (`Profile.tsx`):**
   - Complete academic dashboard permitting students to manage their cumulative GPA, 10th and 12th marks, active backlogs, department, and resume hyperlinks.
4. **Placement Drives Directory (`Drives.tsx`):**
   - Filterable cards displaying active drives with compensation packages (LPA), locations, department requirements, and dates. Includes a live **Eligibility Modal** rendering criteria diagnostics.
5. **Drive & Recruitment Manager (`ManageDrives.tsx`):**
   - Recruiter and administrator workspace for creating new job postings, scheduling recruitment rounds, reviewing applicants, updating interview feedback, and marking offers.
6. **Student Applications Portal (`MyApplications.tsx`):**
   - Personal tracker for students showing current round status, recruiter feedback, and outcome badges (`SELECTED`, `IN_PROGRESS`, etc.).
7. **Partner Companies Directory (`Companies.tsx`):**
   - Corporate directory detailing partner companies, tier ratings, locations, and direct links to active drives.
8. **Institutional Dashboard (`Dashboard.tsx`):**
   - Comprehensive analytical control center displaying key performance indicators (Total Students, Placement Rate %, Average Package, Total Offers, and Departmental placement breakdowns).

---

## 8. Testing & Quality Assurance

### 8.1 Testing Strategy
The system underwent rigorous quality assurance across multiple test boundaries:
- **Unit Testing:** Validating individual service methods using JUnit 5 and Mockito.
- **Business Logic Verification:** Boundary testing of the 5-point eligibility evaluation algorithm under varied student profiles.
- **Integration Testing:** Verification of Spring Boot context initialization, data persistence, and security filters.

### 8.2 Unit Testing & Service Mocking (JUnit 5 + Mockito)
Core test suites are located under [`backend/src/test/java/com/placement/service/`](file:///d:/Piyush/eclipse-workspace/CollegePlacement/backend/src/test/java/com/placement/service/):

1. **`PlacementDriveServiceTest.java`:**
   - Evaluates eligibility when CGPA is above, equal to, and below the threshold.
   - Evaluates department mismatch behavior and backlogs exceeding drive thresholds.
   - Validates multi-department and `ALL` department wildcard support.
2. **`JobApplicationServiceTest.java`:**
   - Verifies that eligible students successfully create applications with status `APPLIED`.
   - Asserts that ineligible students are rejected before application creation.
   - Verifies duplicate application prevention.
   - Validates that setting status to `SELECTED` triggers the student profile update to `isPlaced = true`.
3. **`DashboardServiceTest.java`:**
   - Tests KPI metric calculations, placement percentage formulas, and empty database edge cases.

### 8.3 Test Case Execution Matrix

| Test ID | Module / Component | Test Description | Input Data | Expected Result | Status |
|---|---|---|---|---|---|
| **TC-01** | Auth Service | Register user with new email | Valid user DTO, role = STUDENT | User created, encrypted password | PASS |
| **TC-02** | Auth Service | Login with invalid credentials | Correct email, incorrect password | BadCredentialsException thrown | PASS |
| **TC-03** | Eligibility Engine | Student meets all 5 criteria | CGPA: 8.5 (Min 7.0), Backlogs: 0 | `isEligible = true`, all checks passed | PASS |
| **TC-04** | Eligibility Engine | Student has backlogs over limit | Backlogs: 2, Max allowed: 0 | `isEligible = false`, specific backlog reason | PASS |
| **TC-05** | Eligibility Engine | Department not eligible | Dept: Civil, Eligible: CSE, IT | `isEligible = false`, department mismatch | PASS |
| **TC-06** | Application Service | Apply to drive while eligible | Valid Student, Valid Drive | Application saved with status `APPLIED` | PASS |
| **TC-07** | Application Service | Duplicate application attempt | Same student & drive ID | IllegalArgumentException thrown | PASS |
| **TC-08** | Recruitment Service | Recruiter marks candidate SELECTED | Status = `SELECTED` | Application updated & `isPlaced = true` | PASS |
| **TC-09** | Dashboard Service | Placement calculation accuracy | 10 students, 8 placed | Placement Rate = 80.0% | PASS |

---

## 9. Deployment & Configuration Guide

### 9.1 Database Initialization
1. Ensure MySQL Server 8.0+ is running on `localhost:3306`.
2. Connect to MySQL client and create the database schema:
   ```sql
   CREATE DATABASE college_placement CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```
3. Verify connection configuration in [`backend/src/main/resources/application.properties`](file:///d:/Piyush/eclipse-workspace/CollegePlacement/backend/src/main/resources/application.properties):
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/college_placement?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   spring.datasource.username=root
   spring.datasource.password=root
   spring.jpa.hibernate.ddl-auto=update
   ```

### 9.2 Backend Configuration & Execution
1. Navigate to the backend directory:
   ```bash
   cd backend
   ```
2. Build and run unit tests:
   ```bash
   mvn clean test
   ```
3. Launch the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```
   *The backend will initialize and listen on `http://localhost:8080`.*

### 9.3 Frontend Build & Execution
1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```
2. Install node dependencies:
   ```bash
   npm install
   ```
3. Start the Vite development server:
   ```bash
   npm run dev
   ```
   *The client interface will become accessible at `http://localhost:5173`.*

---

## 10. Conclusion & Future Enhancements

### 10.1 Project Summary & Achievements
The **College Placement Management System** successfully meets all its architectural and operational objectives:
- It eliminates the clerical burden of manual spreadsheet management through an intuitive, centralized portal.
- Its automated 5-point eligibility verification ensures fairness and transparency for candidates while providing recruiters with qualified applicant pools.
- The multi-stage recruitment pipeline gives Training and Placement Officers full operational visibility over ongoing drives.
- The role-based architecture, backed by modern Spring Boot security and React 19 standards, provides a scalable foundation for academic institutions.

### 10.2 Roadmap for Future Enhancements
- **Automated Resume Parsing (NLP):** Incorporating machine learning/NLP to parse student resumes (PDF/DOCX) to automatically populate student skill sets.
- **Automated Notification Gateway:** Integrating WhatsApp/SMS and email dispatch (via Twilio or SendGrid) for immediate round schedule notifications.
- **Integrated Video Assessments:** Integration with WebRTC or Zoom APIs for virtual technical interview scheduling and recording directly within the portal.
- **Placement Policy Rules Engine:** Configurable institutional placement rules (e.g., "Dream Option" policy where placed students can only apply for drives offering 1.5x higher CTC).

---

### **References & Bibliographical Citations**
1. Spring Boot Documentation: *Building RESTful Web Services with Spring Boot 3*, Pivotal / VMware Tanzu (2024).
2. React Official Documentation: *React 19 & Client-Side Architecture*, Meta Platforms (2025).
3. Fielding, Roy Thomas: *Architectural Styles and the Design of Network-based Software Architectures*, University of California, Irvine.
4. Martin, Robert C.: *Clean Architecture: A Craftsman's Guide to Software Structure and Design*, Prentice Hall.
