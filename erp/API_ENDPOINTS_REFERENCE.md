# College ERP - Complete API Endpoints Reference

## Table of Contents
- [Authentication](#authentication)
- [Users](#users)
- [Departments](#departments)
- [Subjects](#subjects)
- [Students](#students)
- [Faculty](#faculty)
- [Attendance](#attendance)
- [Exams & Grades](#exams--grades)
- [Timetable](#timetable)
- [Syllabus](#syllabus)
- [Fees](#fees)
- [Academic Calendar](#academic-calendar)
- [Accreditation](#accreditation)
- [Backup & Logs](#backup--logs)
- [System](#system)

## Authentication

### Login
- **URL:** `POST /api/auth/login`
- **Auth:** None
- **Request Body:**
  ```json
  {
    "email": "user@college.edu",
    "password": "password123"
  }
  ```
- **Roles:** None (Public)

### Refresh Token
- **URL:** `POST /api/auth/refresh-token`
- **Auth:** Bearer Token
- **Request Body:**
  ```json
  {
    "refreshToken": "refresh-token-here"
  }
  ```
- **Roles:** Any authenticated user

## Users

### Register User
- **URL:** `POST /api/users/register`
- **Auth:** None (first user) / Bearer Token (subsequent)
- **Request Body:**
  ```json
  {
    "name": "User Name",
    "email": "user@college.edu",
    "phoneNumber": "1234567890",
    "password": "password123",
    "role": "STUDENT"
  }
  ```
- **Roles:** ADMIN, PRINCIPAL (for creating new users)

### Get All Users
- **URL:** `GET /api/users`
- **Auth:** Bearer Token
- **Roles:** ADMIN, FACULTY

### Get User by ID
- **URL:** `GET /api/users/{id}`
- **Auth:** Bearer Token
- **Roles:** ADMIN, FACULTY, or self

### Update User
- **URL:** `PUT /api/users/{id}`
- **Auth:** Bearer Token
- **Roles:** ADMIN, PRINCIPAL, or self

### Delete User
- **URL:** `DELETE /api/users/{id}`
- **Auth:** Bearer Token
- **Roles:** ADMIN

## Departments

### Get All Departments
- **URL:** `GET /api/departments`
- **Auth:** Bearer Token
- **Roles:** Any authenticated user

### Get Department by ID
- **URL:** `GET /api/departments/{id}`
- **Auth:** Bearer Token
- **Roles:** Any authenticated user

### Create Department
- **URL:** `POST /api/departments`
- **Auth:** Bearer Token
- **Roles:** ADMIN, PRINCIPAL

### Update Department
- **URL:** `PUT /api/departments/{id}`
- **Auth:** Bearer Token
- **Roles:** ADMIN, PRINCIPAL

## Subjects

### Get All Subjects
- **URL:** `GET /api/subjects`
- **Auth:** Bearer Token
- **Roles:** Any authenticated user

### Get Subject by ID
- **URL:** `GET /api/subjects/{id}`
- **Auth:** Bearer Token
- **Roles:** Any authenticated user

### Create Subject
- **URL:** `POST /api/subjects`
- **Auth:** Bearer Token
- **Roles:** ADMIN, PRINCIPAL

## Students

### Get All Students
- **URL:** `GET /api/students`
- **Auth:** Bearer Token
- **Roles:** ADMIN, FACULTY, STAFF

### Get Student by ID
- **URL:** `GET /api/students/{id}`
- **Auth:** Bearer Token
- **Roles:** ADMIN, FACULTY, or self

## Faculty

### Get All Faculty
- **URL:** `GET /api/faculty`
- **Auth:** Bearer Token
- **Roles:** ADMIN, PRINCIPAL, FACULTY

### Get Faculty by ID
- **URL:** `GET /api/faculty/{id}`
- **Auth:** Bearer Token
- **Roles:** ADMIN, PRINCIPAL, or self

## Attendance

### Mark Attendance
- **URL:** `POST /api/attendance`
- **Auth:** Bearer Token
- **Roles:** FACULTY

### Get Attendance
- **URL:** `GET /api/attendance/{studentId}`
- **Auth:** Bearer Token
- **Roles:** FACULTY, or self (for own attendance)

## Exams & Grades

### Schedule Exam
- **URL:** `POST /api/exams`
- **Auth:** Bearer Token
- **Roles:** ADMIN, PRINCIPAL, FACULTY

### Submit Grades
- **URL:** `POST /api/grades`
- **Auth:** Bearer Token
- **Roles:** FACULTY

## Timetable

### Get Timetable
- **URL:** `GET /api/timetable`
- **Auth:** Bearer Token
- **Roles:** Any authenticated user

### Update Timetable
- **URL:** `PUT /api/timetable/{id}`
- **Auth:** Bearer Token
- **Roles:** ADMIN, PRINCIPAL, FACULTY

## Syllabus

### Get Syllabus
- **URL:** `GET /api/syllabus/{subjectId}`
- **Auth:** Bearer Token
- **Roles:** Any authenticated user

### Update Syllabus
- **URL:** `PUT /api/syllabus/{id}`
- **Auth:** Bearer Token
- **Roles:** FACULTY (for their subjects), ADMIN

## Fees

### Pay Fees
- **URL:** `POST /api/fees/pay`
- **Auth:** Bearer Token
- **Roles:** STUDENT (for self), ADMIN, STAFF

### Get Fee Details
- **URL:** `GET /api/fees/student/{studentId}`
- **Auth:** Bearer Token
- **Roles:** ADMIN, STAFF, or self

## Academic Calendar

### Get Calendar
- **URL:** `GET /api/calendar`
- **Auth:** Bearer Token
- **Roles:** Any authenticated user

### Update Calendar
- **URL:** `PUT /api/calendar/{id}`
- **Auth:** Bearer Token
- **Roles:** ADMIN, PRINCIPAL

## Accreditation

### Get Accreditation Details
- **URL:** `GET /api/accreditation`
- **Auth:** Bearer Token
- **Roles:** Any authenticated user

## Backup & Logs

### Create Backup
- **URL:** `POST /api/backup`
- **Auth:** Bearer Token
- **Roles:** ADMIN

### View Logs
- **URL:** `GET /api/logs`
- **Auth:** Bearer Token
- **Roles:** ADMIN

## System

### Health Check
- **URL:** `GET /actuator/health`
- **Auth:** None
- **Roles:** Public

### API Documentation
- **URL:** `GET /swagger-ui.html`
- **Auth:** None
- **Roles:** Public
