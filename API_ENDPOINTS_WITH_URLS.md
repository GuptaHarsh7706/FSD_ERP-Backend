# College ERP - Complete API Endpoints with Full URLs

Base URL: `http://localhost:8090`

## Table of Contents
- [1. Public Access](#1-public-access)
- [2. Authentication](#2-authentication)
- [3. User Management](#3-user-management)
- [4. Department Management](#4-department-management)
- [5. Subject Management](#5-subject-management)
- [6. Student Management](#6-student-management)
- [7. Faculty Management](#7-faculty-management)
- [8. Attendance](#8-attendance)
- [9. Exams & Grades](#9-exams--grades)
- [10. Timetable](#10-timetable)
- [11. Syllabus](#11-syllabus)
- [12. Fees](#12-fees)
- [13. Academic Calendar](#13-academic-calendar)
- [14. System & Admin](#14-system--admin)

## 1. Public Access

### API Documentation
- **Swagger UI:** http://localhost:8090/swagger-ui.html
- **API Docs (JSON):** http://localhost:8090/v3/api-docs

### Health Check
- **GET** http://localhost:8090/actuator/health

### Public Test Endpoint
- **GET** http://localhost:8090/api/test/public

## 2. Authentication

### Login
- **POST** http://localhost:8090/api/auth/login
  ```json
  {
    "email": "user@college.edu",
    "password": "password123"
  }
  ```

### Refresh Token
- **POST** http://localhost:8090/api/auth/refresh-token
  ```json
  {
    "refreshToken": "your-refresh-token"
  }
  ```

### Logout
- **POST** http://localhost:8090/api/auth/logout

## 3. User Management

### Register User
- **POST** http://localhost:8090/api/users/register
  ```json
  {
    "name": "User Name",
    "email": "user@college.edu",
    "phoneNumber": "1234567890",
    "password": "password123",
    "role": "STUDENT"
  }
  ```

### Get All Users
- **GET** http://localhost:8090/api/users
  - Requires: ADMIN, FACULTY role

### Get User by ID
- **GET** http://localhost:8090/api/users/{id}
  - Requires: ADMIN, FACULTY, or self

### Update User
- **PUT** http://localhost:8090/api/users/{id}
  - Requires: ADMIN, PRINCIPAL, or self

### Delete User
- **DELETE** http://localhost:8090/api/users/{id}
  - Requires: ADMIN role

## 4. Department Management

### Get All Departments
- **GET** http://localhost:8090/api/departments

### Get Department by ID
- **GET** http://localhost:8090/api/departments/{id}

### Create Department
- **POST** http://localhost:8090/api/departments
  - Requires: ADMIN, PRINCIPAL role

### Update Department
- **PUT** http://localhost:8090/api/departments/{id}
  - Requires: ADMIN, PRINCIPAL role

## 5. Subject Management

### Get All Subjects
- **GET** http://localhost:8090/api/subjects

### Get Subject by ID
- **GET** http://localhost:8090/api/subjects/{id}

### Create Subject
- **POST** http://localhost:8090/api/subjects
  - Requires: ADMIN, PRINCIPAL role

## 6. Student Management

### Get All Students
- **GET** http://localhost:8090/api/students
  - Requires: ADMIN, FACULTY, STAFF role

### Get Student by ID
- **GET** http://localhost:8090/api/students/{id}
  - Requires: ADMIN, FACULTY, or self

## 7. Faculty Management

### Get All Faculty
- **GET** http://localhost:8090/api/faculty
  - Requires: ADMIN, PRINCIPAL, FACULTY role

### Get Faculty by ID
- **GET** http://localhost:8090/api/faculty/{id}
  - Requires: ADMIN, PRINCIPAL, or self

## 8. Attendance

### Mark Attendance
- **POST** http://localhost:8090/api/attendance
  - Requires: FACULTY role

### Get Attendance
- **GET** http://localhost:8090/api/attendance/{studentId}
  - Requires: FACULTY, or self

## 9. Exams & Grades

### Schedule Exam
- **POST** http://localhost:8090/api/exams
  - Requires: ADMIN, PRINCIPAL, FACULTY role

### Submit Grades
- **POST** http://localhost:8090/api/grades
  - Requires: FACULTY role

## 10. Timetable

### Get Timetable
- **GET** http://localhost:8090/api/timetable

### Update Timetable
- **PUT** http://localhost:8090/api/timetable/{id}
  - Requires: ADMIN, PRINCIPAL, FACULTY role

## 11. Syllabus

### Get Syllabus
- **GET** http://localhost:8090/api/syllabus/{subjectId}

### Update Syllabus
- **PUT** http://localhost:8090/api/syllabus/{id}
  - Requires: FACULTY (for their subjects), ADMIN

## 12. Fees

### Pay Fees
- **POST** http://localhost:8090/api/fees/pay
  - Requires: STUDENT (self), ADMIN, STAFF

### Get Fee Details
- **GET** http://localhost:8090/api/fees/student/{studentId}
  - Requires: ADMIN, STAFF, or self

## 13. Academic Calendar

### Get Calendar
- **GET** http://localhost:8090/api/calendar

### Update Calendar
- **PUT** http://localhost:8090/api/calendar/{id}
  - Requires: ADMIN, PRINCIPAL role

## 14. System & Admin

### Create Backup
- **POST** http://localhost:8090/api/backup
  - Requires: ADMIN role

### View Logs
- **GET** http://localhost:8090/api/logs
  - Requires: ADMIN role

### System Info
- **GET** http://localhost:8090/api/system/info
  - Requires: ADMIN role
