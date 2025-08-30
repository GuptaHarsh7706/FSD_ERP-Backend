# 📚 College ERP System - Complete Codebase Documentation

## 📋 Table of Contents
1. [Project Overview](#project-overview)
2. [Architecture & Technology Stack](#architecture--technology-stack)
3. [Project Structure](#project-structure)
4. [Database Schema & Entities](#database-schema--entities)
5. [Security & Authentication](#security--authentication)
6. [API Controllers & Endpoints](#api-controllers--endpoints)
7. [Services & Business Logic](#services--business-logic)
8. [Configuration](#configuration)
9. [Development Setup](#development-setup)
10. [Role-Based Access Control](#role-based-access-control)
11. [Testing Guide](#testing-guide)

---

## 🎯 Project Overview

The **College ERP System** is a comprehensive backend application built with Spring Boot that manages all aspects of college administration including user management, academic records, departments, courses, attendance, fees, and more.

### Key Features:
- **User Management** - Multi-role user system (Admin, Principal, Faculty, Staff, Student)
- **Academic Management** - Departments, courses, subjects, exams, marks
- **Student Services** - Enrollment, attendance, fees, homework
- **Administrative Tools** - Logs, backups, timetables, syllabus
- **Security** - JWT-based authentication with role hierarchy
- **Database** - PostgreSQL with JPA/Hibernate ORM

---

## 🏗️ Architecture & Technology Stack

### **Backend Framework:**
- **Spring Boot 3.5.5** - Main application framework
- **Spring Security 6.2.10** - Authentication & authorization
- **Spring Data JPA** - Data persistence layer
- **Hibernate** - ORM implementation

### **Database:**
- **PostgreSQL** - Primary database
- **Flyway** - Database migration management

### **Security:**
- **JWT (JSON Web Tokens)** - Stateless authentication
- **BCrypt** - Password encryption
- **Role-based access control** - Hierarchical permissions

### **Build & Dependencies:**
- **Maven** - Dependency management
- **Lombok** - Boilerplate code reduction
- **Jakarta Validation** - Input validation

---

## 📁 Project Structure

```
src/main/java/com/example/erp/
├── ErpApplication.java              # Main Spring Boot application
├── controller/                      # REST API controllers
│   ├── AttendanceController.java
│   ├── AuthController.java
│   ├── DepartmentController.java
│   ├── ExamController.java
│   ├── FacultyController.java
│   ├── FeesController.java
│   ├── MarksController.java
│   ├── StudentController.java
│   ├── SubjectController.java
│   ├── TestController.java
│   └── UserController.java
├── dto/                            # Data Transfer Objects
│   ├── LoginRequest.java
│   └── LoginResponse.java
├── entity/                         # JPA entities
│   ├── Accreditation.java
│   ├── Attendance.java
│   ├── Backup.java
│   ├── Department.java
│   ├── Exam.java
│   ├── Faculty.java
│   ├── Fees.java
│   ├── Homework.java
│   ├── Log.java
│   ├── Marks.java
│   ├── Student.java
│   ├── Subject.java
│   ├── Syllabus.java
│   ├── Timetable.java
│   └── User.java
├── repository/                     # JPA repositories
│   ├── [Entity]Repository.java (15 repositories)
├── security/                       # Security configuration
│   ├── CustomUserDetailsService.java
│   ├── JwtAuthenticationFilter.java
│   ├── JwtUtil.java
│   ├── SecurityConfig.java
│   └── UserPrincipal.java
└── service/                        # Business logic services
    ├── [Entity]Service.java (10 services)

src/main/resources/
├── application.yml                 # Main configuration
├── application.properties          # Additional properties
└── db/migration/                   # Flyway migration scripts
```

---

## 🗄️ Database Schema & Entities

### **Core Entities:**

#### **User Entity** (`users` table)
- **Primary Key:** `user_id` (Long)
- **Fields:** name, email, phoneNumber, password, role, departmentId
- **Roles:** ADMIN, PRINCIPAL, FACULTY, STAFF, STUDENT
- **Relationships:** 
  - ManyToOne with Department
  - OneToOne with Student/Faculty
  - OneToMany with Logs

#### **Student Entity** (`students` table)
- **Primary Key:** `student_id` (Long)
- **Fields:** userId, prnNumber, batch, semester, academicYear
- **Relationships:** OneToOne with User, ManyToOne with Department

#### **Faculty Entity** (`faculty` table)
- **Primary Key:** `faculty_id` (Long)
- **Fields:** userId, designation, qualification, experience
- **Relationships:** OneToOne with User, ManyToOne with Department

#### **Department Entity** (`departments` table)
- **Primary Key:** `department_id` (Long)
- **Fields:** name, code, description, hodId
- **Relationships:** OneToMany with Users, Students, Faculty

#### **Subject Entity** (`subjects` table)
- **Primary Key:** `subject_id` (Long)
- **Fields:** name, code, credits, semester, departmentId
- **Relationships:** ManyToOne with Department

#### **Attendance Entity** (`attendance` table)
- **Primary Key:** `attendance_id` (Long)
- **Fields:** studentId, subjectId, date, status, remarks
- **Relationships:** ManyToOne with Student and Subject

#### **Marks Entity** (`marks` table)
- **Primary Key:** `marks_id` (Long)
- **Fields:** studentId, subjectId, examId, marksObtained, totalMarks
- **Relationships:** ManyToOne with Student, Subject, Exam

#### **Exam Entity** (`exams` table)
- **Primary Key:** `exam_id` (Long)
- **Fields:** name, type, date, duration, totalMarks, subjectId
- **Relationships:** ManyToOne with Subject

#### **Fees Entity** (`fees` table)
- **Primary Key:** `fees_id` (Long)
- **Fields:** studentId, amount, dueDate, paidDate, status, type
- **Relationships:** ManyToOne with Student

#### **Log Entity** (`logs` table)
- **Primary Key:** `log_id` (Long)
- **Fields:** userId, action, entityType, entityId, timestamp, details
- **Relationships:** ManyToOne with User

### **Additional Entities:**
- **Homework** - Assignment management
- **Timetable** - Class scheduling
- **Syllabus** - Course curriculum
- **Backup** - System backups
- **Accreditation** - College accreditations

---

## 🔐 Security & Authentication

### **Role Hierarchy:**
```
ADMIN > PRINCIPAL > FACULTY > STAFF > STUDENT
```

### **Authentication Flow:**
1. **Login:** POST `/api/auth/login` with email/password
2. **JWT Token:** Receive JWT token in response
3. **Authorization:** Include `Authorization: Bearer <token>` in headers
4. **Role Check:** Each endpoint validates user role permissions

### **Security Configuration:**
- **JWT Expiration:** Configurable token lifetime
- **Password Encryption:** BCrypt with strength 12
- **CORS:** Configured for development and production
- **Method Security:** `@PreAuthorize` annotations on endpoints

### **Public Endpoints:**
- `/api/auth/**` - Authentication endpoints
- `/api/users/register` - User registration (requires ADMIN/PRINCIPAL)
- `/actuator/health` - Health check
- `/swagger-ui/**` - API documentation

---

## 🌐 API Controllers & Endpoints

### **AuthController** (`/api/auth`)
- `POST /login` - User authentication
- `POST /refresh` - Token refresh
- `POST /logout` - User logout

### **UserController** (`/api/users`)
- `GET /` - Get all users (ADMIN/FACULTY)
- `GET /{id}` - Get user by ID
- `GET /email/{email}` - Get user by email
- `GET /role/{role}` - Get users by role
- `POST /register` - Create new user (ADMIN/PRINCIPAL)
- `PUT /{id}` - Update user (ADMIN/PRINCIPAL/Self)
- `DELETE /{id}` - Delete user (ADMIN)

### **StudentController** (`/api/students`)
- `GET /` - Get all students (ADMIN/FACULTY)
- `GET /{id}` - Get student by ID
- `GET /prn/{prnNumber}` - Get student by PRN
- `GET /batch/{batch}` - Get students by batch
- `POST /` - Create student (ADMIN/PRINCIPAL)
- `PUT /{id}` - Update student
- `DELETE /{id}` - Delete student (ADMIN)

### **FacultyController** (`/api/faculty`)
- `GET /` - Get all faculty (ADMIN/FACULTY)
- `GET /{id}` - Get faculty by ID
- `GET /designation/{designation}` - Get faculty by designation
- `POST /` - Create faculty (ADMIN/PRINCIPAL)
- `PUT /{id}` - Update faculty
- `DELETE /{id}` - Delete faculty (ADMIN)

### **DepartmentController** (`/api/departments`)
- `GET /` - Get all departments
- `GET /{id}` - Get department by ID
- `POST /` - Create department (ADMIN/PRINCIPAL)
- `PUT /{id}` - Update department (ADMIN/PRINCIPAL)
- `DELETE /{id}` - Delete department (ADMIN)

### **SubjectController** (`/api/subjects`)
- `GET /` - Get all subjects
- `GET /{id}` - Get subject by ID
- `GET /department/{departmentId}` - Get subjects by department
- `POST /` - Create subject (ADMIN/PRINCIPAL)
- `PUT /{id}` - Update subject (ADMIN/PRINCIPAL/FACULTY)
- `DELETE /{id}` - Delete subject (ADMIN)

### **AttendanceController** (`/api/attendance`)
- `GET /student/{studentId}` - Get student attendance
- `GET /subject/{subjectId}` - Get subject attendance
- `POST /` - Mark attendance (FACULTY)
- `PUT /{id}` - Update attendance (FACULTY)

### **MarksController** (`/api/marks`)
- `GET /student/{studentId}` - Get student marks
- `GET /exam/{examId}` - Get exam marks
- `POST /` - Add marks (FACULTY)
- `PUT /{id}` - Update marks (FACULTY)

### **ExamController** (`/api/exams`)
- `GET /` - Get all exams
- `GET /{id}` - Get exam by ID
- `POST /` - Create exam (ADMIN/PRINCIPAL/FACULTY)
- `PUT /{id}` - Update exam
- `DELETE /{id}` - Delete exam (ADMIN)

### **FeesController** (`/api/fees`)
- `GET /student/{studentId}` - Get student fees
- `POST /` - Create fee record (ADMIN/STAFF)
- `PUT /{id}/pay` - Mark fee as paid (STAFF)

---

## ⚙️ Services & Business Logic

### **Service Layer Architecture:**
Each entity has a corresponding service class that handles:
- **CRUD Operations** - Create, Read, Update, Delete
- **Business Logic** - Validation, calculations, workflows
- **Data Transformation** - Entity to DTO mapping
- **Transaction Management** - Database transaction handling

### **Key Services:**

#### **UserService**
- User creation with role validation
- Password encryption
- Email uniqueness validation
- Role-based user management

#### **StudentService**
- Student profile management
- PRN number generation
- Batch and semester management
- Academic record tracking

#### **FacultyService**
- Faculty profile management
- Designation and qualification tracking
- Department assignment

#### **AttendanceService**
- Attendance marking and tracking
- Attendance percentage calculations
- Bulk attendance operations

#### **MarksService**
- Grade entry and management
- Grade calculations and analytics
- Result generation

#### **LogService**
- System activity logging
- Audit trail maintenance
- User action tracking

---

## ⚙️ Configuration

### **application.yml**
```yaml
spring:
  application:
    name: erp
  
  datasource:
    url: jdbc:postgresql://localhost:5432/your_database_url
    username: YOUR_USERNAME
    password: YOUR_PASSWORD
    driver-class-name: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
  
  flyway:
    enabled: true
    baseline-on-migrate: true
    validate-on-migrate: false

server:
  port: 8090

jwt:
  secret: your-secret-key
  expiration: 86400000
```

### **Security Configuration:**
- JWT token validation
- Role hierarchy setup
- CORS configuration
- Method-level security

---

## 🚀 Development Setup

### **Prerequisites:**
- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+
- IDE (IntelliJ IDEA/Eclipse)

### **Setup Steps:**

1. **Clone Repository:**
   ```bash
   git clone <repository-url>
   cd erp
   ```

2. **Database Setup:**
   ```sql
   CREATE DATABASE college_erp;
   CREATE USER postgres WITH PASSWORD 'newpassword';
   GRANT ALL PRIVILEGES ON DATABASE college_erp TO postgres;
   ```

3. **Configure Application:**
   - Update `application.yml` with your database credentials
   - Set JWT secret key

4. **Run Application:**
   ```bash
   mvn spring-boot:run
   ```

5. **Verify Setup:**
   - Access: `http://localhost:8090`
   - Health check: `http://localhost:8090/actuator/health`

### **Development Tools:**
- **Postman/Insomnia** - API testing
- **pgAdmin** - Database management
- **Lombok Plugin** - IDE support for Lombok

---

## 🔑 Role-Based Access Control

### **Permission Matrix:**

| Feature | ADMIN | PRINCIPAL | FACULTY | STAFF | STUDENT |
|---------|-------|-----------|---------|-------|---------|
| **User Creation** | Full CRUD | Create FACULTY/STAFF/STUDENT | View Only | View Only | View Only |
| **User Management** | Full CRUD | Update FACULTY/STAFF/STUDENT | Own Profile Only | Limited Update | Own Profile Only |
| **Department Management** | Full CRUD | Full CRUD | View Only | View Only | View Only |
| **Course Management** | Full CRUD | Full CRUD | View Assigned | View Only | View Only |
| **Enrollment Management** | Full CRUD | Full CRUD | View Only | Administrative CRUD | View Own Only |
| **Grade Management** | Full CRUD | Full Access | Own Courses CRUD | View Only | Own Grades View |
| **Attendance Management** | Full CRUD | Full Access | Own Courses CRUD | View Only | Own Attendance View |
| **Fees Management** | Full CRUD | Full Access | View Only | Full CRUD | Own Fees View |
| **System Logs** | Full Access | Full Access | Own Actions | Own Actions | Own Actions |

### **Role Restrictions:**
- **PRINCIPAL** cannot create ADMIN or PRINCIPAL users
- **FACULTY** can only manage their assigned courses
- **STAFF** has administrative privileges for student services
- **STUDENT** can only view their own academic records

---



## 📝 API Documentation

### **Swagger UI:**
- **URL:** `http://localhost:8090/swagger-ui.html`
- **API Docs:** `http://localhost:8090/v3/api-docs`

### **Authentication Required:**
Most endpoints require JWT token in header:
```
Authorization: Bearer <your-jwt-token>
```

### **Common Response Codes:**
- **200** - Success
- **201** - Created
- **400** - Bad Request
- **401** - Unauthorized
- **403** - Forbidden
- **404** - Not Found
- **500** - Internal Server Error

---

## 🔧 Troubleshooting

### **Common Issues:**

1. **Database Connection Failed:**
   - Check PostgreSQL is running
   - Verify connection credentials
   - Ensure database exists

2. **JWT Token Invalid:**
   - Check token expiration
   - Verify JWT secret configuration
   - Ensure proper Authorization header format

3. **403 Forbidden Errors:**
   - Verify user role permissions
   - Check @PreAuthorize annotations
   - Confirm role hierarchy configuration

4. **Application Won't Start:**
   - Check Java version compatibility
   - Verify all dependencies are resolved
   - Review application logs for specific errors

### **Logging:**
- Application logs: `logs/application.log`
- Security events: Check console output
- Database queries: Enable `show-sql: true`

---

## 📞 Support & Contribution

### **Development Guidelines:**
- Follow Spring Boot best practices
- Use Lombok for boilerplate reduction
- Implement proper error handling
- Add comprehensive logging
- Write unit and integration tests

### **Code Style:**
- Use meaningful variable names
- Add JavaDoc for public methods
- Follow REST API conventions
- Implement proper validation

---

**Last Updated:** August 2025  
**Version:** 1.0.0  
**Principal Maintainer:** Harsh S Gupta
