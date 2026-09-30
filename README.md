# College Placement Management System

A full-stack web application for managing the college placement process — student profiles, company management, placement drives, eligibility evaluation, job applications, and recruitment rounds.

## Tech Stack

### Backend
- Java 17
- Spring Boot 3.2.x
- Spring Web, Spring Data JPA, Spring Security
- Hibernate + MySQL
- JWT Authentication
- Bean Validation
- Maven

### Frontend
- React 19 + TypeScript
- Vite
- Axios
- React Router

### Testing
- JUnit 5
- Mockito
- Spring Boot Test

## Project Structure

```
college-placement-management/
├── backend/                  # Spring Boot backend
│   ├── src/main/java/com/placement/
│   │   ├── config/           # CORS, Security, etc.
│   │   ├── controller/       # REST controllers
│   │   ├── dto/              # Request/response models
│   │   ├── entity/           # JPA entities
│   │   ├── exception/        # Custom exceptions + global handler
│   │   ├── repository/       # Spring Data repositories
│   │   ├── security/         # JWT, auth filters
│   │   ├── service/          # Business logic
│   │   └── util/             # Utility classes
│   └── src/main/resources/
│       └── application.properties
├── frontend/                 # React + Vite frontend
│   └── src/
│       ├── api/              # Axios instance
│       ├── components/       # Reusable components
│       ├── pages/            # Page components
│       └── types/            # TypeScript types
└── README.md
```

## Prerequisites

- Java 17+
- Node.js 18+
- MySQL 8.0+
- Maven 3.8+

## Setup

### Database

Create a MySQL database (or let Hibernate create it automatically):

```sql
CREATE DATABASE college_placement;
```

Update credentials in `backend/src/main/resources/application.properties` if needed.

### Backend

```bash
cd backend
mvn spring-boot:run
```

The backend starts on `http://localhost:8080`.

### Frontend

```bash
cd frontend
npm install
npm run dev
```

The frontend starts on `http://localhost:5173`.

## API

| Method | Endpoint                          | Description                                             | Access                  |
|--------|-----------------------------------|---------------------------------------------------------|-------------------------|
| GET    | /api/health                       | Health check                                            | Public                  |
| POST   | /api/auth/register                | Register student/company/admin                          | Public                  |
| POST   | /api/auth/login                   | Authenticate & get JWT token                            | Public                  |
| GET    | /api/auth/me                      | Get current authenticated user                          | Authenticated           |
| GET    | /api/companies                    | List all partner companies                              | Public                  |
| GET    | /api/companies/{id}               | Get company details                                     | Public                  |
| POST   | /api/companies                    | Register new company                                    | ADMIN, COMPANY          |
| PUT    | /api/companies/{id}               | Update company profile                                  | ADMIN, COMPANY          |
| DELETE | /api/companies/{id}               | Remove company                                          | ADMIN                   |
| GET    | /api/students/profile             | Get current student profile                             | Authenticated           |
| PUT    | /api/students/profile             | Create or update student profile                        | Authenticated           |
| GET    | /api/students                     | List all registered student profiles                    | ADMIN, COMPANY          |
| GET    | /api/students/{id}                | Get student profile by ID                               | ADMIN, COMPANY          |
| GET    | /api/drives                       | List all placement drives with eligibility status       | Public / Authenticated  |
| GET    | /api/drives/{id}                  | Get placement drive details                             | Public / Authenticated  |
| POST   | /api/drives                       | Create placement drive                                  | ADMIN, COMPANY          |
| PUT    | /api/drives/{id}                  | Update placement drive details                          | ADMIN, COMPANY          |
| DELETE | /api/drives/{id}                  | Delete placement drive                                  | ADMIN                   |
| GET    | /api/drives/{id}/eligibility      | Check student eligibility against drive criteria        | STUDENT                 |
| GET    | /api/drives/{id}/rounds           | Get recruitment rounds for drive                        | Public / Authenticated  |
| POST   | /api/drives/{id}/rounds          | Add recruitment round to drive                          | ADMIN, COMPANY          |
| DELETE | /api/rounds/{id}                  | Remove recruitment round                                | ADMIN, COMPANY          |
| POST   | /api/applications/apply/{driveId} | Student applies for drive (automated eligibility check) | STUDENT                 |
| GET    | /api/applications/my-applications | Get current student's applications & status             | STUDENT                 |
| GET    | /api/applications/drive/{driveId} | List student applications for a drive                   | ADMIN, COMPANY          |
| PATCH  | /api/applications/{id}/status     | Update round, status & feedback (auto-places student)   | ADMIN, COMPANY          |
| GET    | /api/dashboard/stats              | Get system-wide placement metrics & KPI analytics       | Public / Authenticated  |

## Development Phases

- [x] Phase 1: Foundation (project setup, MySQL, CORS, basic API)
- [x] Phase 2: Users & Authentication (JWT auth, role-based access control, profile management)
- [x] Phase 3: Placement Management (Company registration, drive postings, search/filters)
- [x] Phase 4: Eligibility & Applications (Automated 5-point eligibility evaluation, application workflow)
- [x] Phase 5: Recruitment Process (Multi-round interview tracking, application status lifecycle, auto-placed status)
- [x] Phase 6: Dashboard, Testing & Polish (Analytics dashboard, unit/integration test suites, frontend verification)

