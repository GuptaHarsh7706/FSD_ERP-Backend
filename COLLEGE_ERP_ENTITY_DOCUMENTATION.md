# College ERP Backend - Entity Relationship Documentation

## Project Overview
**System Name:** College ERP (Enterprise Resource Planning) Backend  
**Technology Stack:** Spring Boot, JPA/Hibernate, MySQL  
**Architecture:** RESTful API with JWT Authentication  
**Purpose:** Comprehensive management system for educational institutions

---

## Table of Contents
1. [Core Entities](#core-entities)
2. [Entity Relationships](#entity-relationships)
3. [Detailed Entity Specifications](#detailed-entity-specifications)
4. [Database Schema](#database-schema)
5. [Business Rules & Constraints](#business-rules--constraints)
6. [Entity Relationship Diagram](#entity-relationship-diagram)

---

## Core Entities

### 1. User Management
- **User** - Central authentication and authorization entity
- **Student** - Student-specific profile and academic information
- **Faculty** - Faculty member profiles and designations

### 2. Academic Management
- **Department** - Academic departments and organizational units
- **Subject** - Course/subject definitions
- **Enrollment** - Student course registrations
- **Timetable** - Class scheduling and time management

### 3. Assessment & Evaluation
- **Exam** - Examination definitions
- **Marks** - Student examination scores
- **Attendance** - Student attendance tracking
- **Homework** - Assignment management

### 4. Administrative
- **Fees** - Fee management and payment tracking
- **Syllabus** - Course syllabus and curriculum
- **Log** - System audit trail
- **AcademicCalendar** - Academic year planning
- **Accreditation** - Department accreditation records
- **Backup** - System backup management
- **GradeCalculation** - Grade computation rules

---

## Entity Relationships

### Primary Relationships

#### User-Centric Relationships
```
User (1) ←→ (0..1) Student
User (1) ←→ (0..1) Faculty
User (N) ←→ (1) Department
User (1) ←→ (N) Log
```

#### Academic Relationships
```
Department (1) ←→ (N) Subject
Department (1) ←→ (N) User
Department (1) ←→ (N) Timetable
Department (1) ←→ (N) Exam
Department (1) ←→ (1) Faculty (as Head)

Subject (1) ←→ (N) Enrollment
Subject (1) ←→ (N) Timetable
Subject (1) ←→ (N) Attendance
Subject (1) ←→ (N) Exam
Subject (1) ←→ (N) Syllabus
Subject (1) ←→ (N) Homework
```

#### Student Relationships
```
Student (1) ←→ (N) Enrollment
Student (1) ←→ (N) Attendance
Student (1) ←→ (N) Marks
Student (1) ←→ (N) Fees
Student (1) ←→ (N) Homework
```

#### Faculty Relationships
```
Faculty (1) ←→ (N) Department (as Head)
Faculty (1) ←→ (N) Timetable
Faculty (1) ←→ (N) Syllabus (as Updater)
Faculty (1) ←→ (N) Homework
```

#### Assessment Relationships
```
Exam (1) ←→ (N) Marks
Exam (N) ←→ (1) Subject
Exam (N) ←→ (1) Department
```

---

## Detailed Entity Specifications

### 1. User Entity

**Table Name:** `users`

**Purpose:** Central authentication and user management entity supporting multiple roles

**Attributes:**
| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| userId | Long | PK, Auto-increment | Unique user identifier |
| departmentId | Long | FK | Reference to department |
| name | String(255) | NOT NULL | Full name of user |
| email | String | UNIQUE, NOT NULL, Email format | User email address |
| phoneNumber | String(20) | Optional | Contact number |
| password | String | NOT NULL, Min 8 chars | Encrypted password |
| role | Enum | NOT NULL | User role (ADMIN, FACULTY, STUDENT, STAFF, PRINCIPAL) |
| createdAt | LocalDateTime | Auto-generated | Account creation timestamp |
| updatedAt | LocalDateTime | Auto-updated | Last modification timestamp |

**Relationships:**
- One-to-One with Student (optional)
- One-to-One with Faculty (optional)
- Many-to-One with Department
- One-to-Many with Log

**Business Rules:**
- Email must be unique across the system
- Password must be at least 8 characters
- Role determines access permissions
- Soft delete recommended for data integrity

---

### 2. Student Entity

**Table Name:** `student`

**Purpose:** Store student-specific academic information

**Attributes:**
| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | Long | PK, Auto-increment | Student record ID |
| userId | Long | FK, NOT NULL, UNIQUE | Reference to User |
| prnNumber | String(50) | UNIQUE, NOT NULL | Permanent Registration Number |
| batch | String(10) | Optional | Academic batch/year |
| semester | Integer | Optional | Current semester |
| division | String(10) | Optional | Class division |
| rollNumber | String(20) | Optional | Roll number |

**Relationships:**
- One-to-One with User
- One-to-Many with Attendance
- One-to-Many with Marks
- One-to-Many with Fees
- One-to-Many with Enrollment
- One-to-Many with Homework

**Business Rules:**
- PRN number must be unique
- One user can have only one student profile
- Semester must be between 1-8 (typical engineering course)

---

### 3. Faculty Entity

**Table Name:** `faculty`

**Purpose:** Store faculty member information and designations

**Attributes:**
| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | Long | PK, Auto-increment | Faculty record ID |
| userId | Long | FK, NOT NULL, UNIQUE | Reference to User |
| designation | String(255) | Optional | Job title/position |

**Relationships:**
- One-to-One with User
- One-to-Many with Department (as head)
- One-to-Many with Timetable
- One-to-Many with Syllabus (as updater)
- One-to-Many with Homework

**Business Rules:**
- One user can have only one faculty profile
- Faculty can be head of multiple departments
- Designation determines academic hierarchy

---

### 4. Department Entity

**Table Name:** `department`

**Purpose:** Organizational units for academic management

**Attributes:**
| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | Long | PK, Auto-increment | Department ID |
| headId | Long | FK, Optional | Reference to Faculty (head) |
| name | String(255) | NOT NULL | Department name |
| createdAt | LocalDateTime | Auto-generated | Creation timestamp |

**Relationships:**
- Many-to-One with Faculty (as head)
- One-to-Many with User
- One-to-Many with Subject
- One-to-Many with Timetable
- One-to-Many with Exam
- One-to-Many with Accreditation

**Business Rules:**
- Department name should be unique
- Head must be a faculty member
- Cannot delete department with active students

---

### 5. Subject Entity

**Table Name:** `subject`

**Purpose:** Course/subject definitions and curriculum

**Attributes:**
| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | Long | PK, Auto-increment | Subject ID |
| departmentId | Long | FK, NOT NULL | Reference to Department |
| name | String(255) | NOT NULL | Subject name |
| semester | Integer | Optional | Applicable semester |
| code | String(20) | UNIQUE | Subject code |

**Relationships:**
- Many-to-One with Department
- One-to-Many with Timetable
- One-to-Many with Syllabus
- One-to-Many with Homework
- One-to-Many with Exam
- One-to-Many with Attendance

**Business Rules:**
- Subject code must be unique
- Subject belongs to one department
- Semester must be valid (1-8)

---

### 6. Enrollment Entity

**Table Name:** `enrollment`

**Purpose:** Track student course registrations

**Attributes:**
| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | Long | PK, Auto-increment | Enrollment ID |
| studentId | Long | FK, NOT NULL | Reference to Student |
| subjectId | Long | FK, NOT NULL | Reference to Subject |
| academicYear | String | NOT NULL | Academic year (e.g., "2024-25") |
| semester | Integer | NOT NULL | Semester number |
| status | Enum | NOT NULL | ACTIVE, COMPLETED, DROPPED, FAILED |
| enrolledAt | LocalDateTime | NOT NULL | Enrollment timestamp |
| completedAt | LocalDateTime | Optional | Completion timestamp |
| grade | String | Optional | Final grade |
| credits | Integer | Optional | Course credits |

**Relationships:**
- Many-to-One with Student
- Many-to-One with Subject

**Business Rules:**
- Student cannot enroll in same subject twice in same semester
- Status must transition logically (ACTIVE → COMPLETED/DROPPED/FAILED)
- Grade assigned only when status is COMPLETED

---

### 7. Attendance Entity

**Table Name:** `attendance`

**Purpose:** Track daily student attendance

**Attributes:**
| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | Long | PK, Auto-increment | Attendance ID |
| studentId | Long | FK, NOT NULL | Reference to Student |
| subjectId | Long | FK, NOT NULL | Reference to Subject |
| date | LocalDate | NOT NULL | Attendance date |
| status | Enum | NOT NULL | PRESENT, ABSENT, LATE |

**Relationships:**
- Many-to-One with Student
- Many-to-One with Subject

**Business Rules:**
- One attendance record per student per subject per date
- Date cannot be in future
- Default status is PRESENT

---

### 8. Exam Entity

**Table Name:** `exam`

**Purpose:** Define examinations and assessments

**Attributes:**
| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | Long | PK, Auto-increment | Exam ID |
| subjectId | Long | FK, NOT NULL | Reference to Subject |
| departmentId | Long | FK, NOT NULL | Reference to Department |
| date | LocalDate | Optional | Exam date |
| name | String(255) | Optional | Exam name/type |

**Relationships:**
- Many-to-One with Subject
- Many-to-One with Department
- One-to-Many with Marks

**Business Rules:**
- Exam date should not conflict with other exams
- Exam belongs to one subject and department

---

### 9. Marks Entity

**Table Name:** `marks`

**Purpose:** Store student examination scores

**Attributes:**
| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | Long | PK, Auto-increment | Marks ID |
| studentId | Long | FK, NOT NULL | Reference to Student |
| examId | Long | FK, NOT NULL | Reference to Exam |
| marksObtained | BigDecimal(5,2) | Optional | Marks scored |
| totalMarks | BigDecimal(5,2) | Optional | Maximum marks |

**Relationships:**
- Many-to-One with Student
- Many-to-One with Exam

**Business Rules:**
- marksObtained ≤ totalMarks
- Cannot have duplicate marks for same student-exam combination
- Marks must be non-negative

---

### 10. Fees Entity

**Table Name:** `fees`

**Purpose:** Manage student fee payments

**Attributes:**
| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | Long | PK, Auto-increment | Fee ID |
| studentId | Long | FK, NOT NULL | Reference to Student |
| amount | BigDecimal(10,2) | NOT NULL | Total fee amount |
| paidAmount | BigDecimal(10,2) | Default 0 | Amount paid |
| dueDate | LocalDate | Optional | Payment due date |
| paymentStatus | Enum | NOT NULL | PENDING, PARTIAL, PAID, OVERDUE |

**Relationships:**
- Many-to-One with Student

**Business Rules:**
- paidAmount ≤ amount
- Status PAID when paidAmount = amount
- Status PARTIAL when 0 < paidAmount < amount
- Status OVERDUE when dueDate passed and not PAID

---

### 11. Timetable Entity

**Table Name:** `timetable`

**Purpose:** Class scheduling and time management

**Attributes:**
| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | Long | PK, Auto-increment | Timetable ID |
| subjectId | Long | FK, NOT NULL | Reference to Subject |
| facultyId | Long | FK, NOT NULL | Reference to Faculty |
| departmentId | Long | FK, NOT NULL | Reference to Department |
| semester | Integer | Optional | Semester number |
| dayOfWeek | String(20) | Optional | Day (MONDAY, TUESDAY, etc.) |
| startTime | LocalTime | Optional | Class start time |
| endTime | LocalTime | Optional | Class end time |
| classroom | String | Optional | Room/venue |
| academicYear | String | Optional | Academic year |
| timeSlot | String(50) | Optional | Time slot identifier |
| createdAt | LocalDateTime | Auto-generated | Creation timestamp |
| updatedAt | LocalDateTime | Auto-updated | Update timestamp |

**Relationships:**
- Many-to-One with Subject
- Many-to-One with Faculty
- Many-to-One with Department

**Business Rules:**
- startTime < endTime
- No overlapping classes for same faculty/classroom
- Valid day of week

---

### 12. Syllabus Entity

**Table Name:** `syllabus`

**Purpose:** Course syllabus and curriculum management

**Attributes:**
| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | Long | PK, Auto-increment | Syllabus ID |
| title | String | NOT NULL | Syllabus title |
| description | String | Optional | Brief description |
| updatedBy | Long | FK, NOT NULL | Reference to Faculty |
| subjectId | Long | FK, NOT NULL | Reference to Subject |
| departmentId | Long | FK, Optional | Reference to Department |
| content | Text | Optional | Detailed syllabus content |
| academicYear | String | Optional | Academic year |
| semester | Integer | Optional | Semester number |
| status | String | Default "ACTIVE" | Status (ACTIVE, ARCHIVED) |
| createdAt | LocalDateTime | Auto-generated | Creation timestamp |
| updatedAt | LocalDateTime | Auto-updated | Update timestamp |

**Relationships:**
- Many-to-One with Faculty (updater)
- Many-to-One with Subject
- Many-to-One with Department

**Business Rules:**
- Only faculty can update syllabus
- One active syllabus per subject per semester

---

### 13. Homework Entity

**Table Name:** `homework`

**Purpose:** Assignment and homework management

**Attributes:**
| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | Long | PK, Auto-increment | Homework ID |
| title | String | NOT NULL | Assignment title |
| subjectId | Long | FK, NOT NULL | Reference to Subject |
| facultyId | Long | FK, NOT NULL | Reference to Faculty |
| studentId | Long | FK, Optional | Reference to Student (for submission) |
| description | Text | Optional | Assignment details |
| dueDate | LocalDate | Optional | Submission deadline |
| maxMarks | Integer | Optional | Maximum marks |
| status | String | Default "ASSIGNED" | ASSIGNED, SUBMITTED, GRADED |
| assignedAt | LocalDateTime | Optional | Assignment date |
| submittedAt | LocalDateTime | Optional | Submission date |
| submission | Text | Optional | Student submission content |

**Relationships:**
- Many-to-One with Subject
- Many-to-One with Faculty
- Many-to-One with Student

**Business Rules:**
- Faculty assigns homework to subject
- Students submit homework before dueDate
- Status transitions: ASSIGNED → SUBMITTED → GRADED

---

## Database Schema

### Key Constraints

#### Primary Keys
All entities use auto-incrementing Long type primary keys for optimal performance and scalability.

#### Foreign Keys
- Maintain referential integrity
- Cascade operations defined per relationship
- Lazy loading for performance optimization

#### Unique Constraints
- User.email
- Student.prnNumber
- Subject.code

#### Check Constraints
- Marks: marksObtained ≤ totalMarks
- Fees: paidAmount ≤ amount
- Timetable: startTime < endTime
- User: password length ≥ 8

---

## Business Rules & Constraints

### User Management
1. Email must be unique and valid format
2. Password minimum 8 characters with encryption
3. Role-based access control (RBAC)
4. One user can be either Student OR Faculty, not both
5. Soft delete for audit trail

### Academic Management
6. Student cannot enroll in same subject twice in same semester
7. Subject code must be unique across system
8. Department head must be a faculty member
9. Semester values: 1-8 (standard engineering course)

### Assessment Rules
10. Marks obtained cannot exceed total marks
11. Attendance date cannot be in future
12. One attendance record per student per subject per day
13. Exam dates should not conflict

### Fee Management
14. Paid amount cannot exceed total amount
15. Payment status auto-updates based on paid amount
16. Status becomes OVERDUE after due date if not PAID

### Scheduling Rules
17. Class start time must be before end time
18. No overlapping timetable for same faculty
19. No overlapping timetable for same classroom
20. Valid day of week (MONDAY-SUNDAY)

### Homework Rules
21. Due date should be future date when assigned
22. Status transitions must be logical
23. Submission timestamp must be before or on due date for on-time submission

---

## Entity Relationship Diagram

### High-Level ER Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                        USER (Central Entity)                     │
│  - userId (PK)                                                   │
│  - email (UNIQUE)                                                │
│  - password                                                      │
│  - role (ENUM)                                                   │
│  - departmentId (FK)                                             │
└────────┬────────────────────────────────────┬───────────────────┘
         │                                    │
         │ 1:1                                │ 1:1
         ▼                                    ▼
┌─────────────────┐                  ┌─────────────────┐
│    STUDENT      │                  │     FACULTY     │
│  - id (PK)      │                  │  - id (PK)      │
│  - userId (FK)  │                  │  - userId (FK)  │
│  - prnNumber    │                  │  - designation  │
└────────┬────────┘                  └────────┬────────┘
         │                                    │
         │ 1:N                                │ 1:N
         ▼                                    ▼
┌─────────────────┐                  ┌─────────────────┐
│   ENROLLMENT    │                  │   TIMETABLE     │
│  - id (PK)      │                  │  - id (PK)      │
│  - studentId    │                  │  - facultyId    │
│  - subjectId    │                  │  - subjectId    │
│  - status       │                  │  - dayOfWeek    │
└─────────────────┘                  └─────────────────┘
         │                                    │
         │ N:1                                │ N:1
         ▼                                    ▼
┌──────────────────────────────────────────────────────┐
│                      SUBJECT                          │
│  - id (PK)                                            │
│  - code (UNIQUE)                                      │
│  - name                                               │
│  - departmentId (FK)                                  │
└────────┬─────────────────────────────────────────────┘
         │ N:1
         ▼
┌─────────────────┐
│   DEPARTMENT    │
│  - id (PK)      │
│  - name         │
│  - headId (FK)  │──────┐
└─────────────────┘       │
         ▲                │
         │                │
         └────────────────┘ (Faculty as Head)

┌─────────────────┐      ┌─────────────────┐      ┌─────────────────┐
│   ATTENDANCE    │      │      EXAM       │      │      MARKS      │
│  - studentId    │      │  - subjectId    │      │  - studentId    │
│  - subjectId    │      │  - departmentId │      │  - examId       │
│  - date         │      │  - date         │      │  - marksObtained│
│  - status       │      │  - name         │      │  - totalMarks   │
└─────────────────┘      └─────────────────┘      └─────────────────┘
         │                        │                        │
         └────────────────────────┴────────────────────────┘
                    (All connected to Student/Subject/Exam)

┌─────────────────┐      ┌─────────────────┐      ┌─────────────────┐
│      FEES       │      │    SYLLABUS     │      │    HOMEWORK     │
│  - studentId    │      │  - subjectId    │      │  - subjectId    │
│  - amount       │      │  - updatedBy    │      │  - facultyId    │
│  - paidAmount   │      │  - content      │      │  - studentId    │
│  - status       │      │  - status       │      │  - status       │
└─────────────────┘      └─────────────────┘      └─────────────────┘
```

### Cardinality Summary

| Relationship | Cardinality | Description |
|--------------|-------------|-------------|
| User ↔ Student | 1:0..1 | One user can be one student |
| User ↔ Faculty | 1:0..1 | One user can be one faculty |
| User ↔ Department | N:1 | Many users belong to one department |
| Department ↔ Faculty (head) | 1:0..1 | Department has optional faculty head |
| Department ↔ Subject | 1:N | Department has many subjects |
| Subject ↔ Enrollment | 1:N | Subject has many enrollments |
| Student ↔ Enrollment | 1:N | Student has many enrollments |
| Student ↔ Attendance | 1:N | Student has many attendance records |
| Student ↔ Marks | 1:N | Student has many marks |
| Student ↔ Fees | 1:N | Student has many fee records |
| Subject ↔ Exam | 1:N | Subject has many exams |
| Exam ↔ Marks | 1:N | Exam has many marks |
| Faculty ↔ Timetable | 1:N | Faculty teaches many classes |
| Subject ↔ Timetable | 1:N | Subject has many scheduled classes |
| Faculty ↔ Syllabus | 1:N | Faculty updates many syllabi |
| Subject ↔ Syllabus | 1:N | Subject has many syllabus versions |
| Faculty ↔ Homework | 1:N | Faculty assigns many homework |
| Student ↔ Homework | 1:N | Student submits many homework |

---

## Implementation Notes

### JPA Annotations Used
- `@Entity` - Mark class as JPA entity
- `@Table` - Specify table name
- `@Id` - Primary key
- `@GeneratedValue` - Auto-increment strategy
- `@Column` - Column specifications
- `@ManyToOne`, `@OneToMany`, `@OneToOne` - Relationships
- `@JoinColumn` - Foreign key specification
- `@Enumerated` - Enum type mapping
- `@JsonIgnore` - Prevent circular serialization
- `@CreationTimestamp`, `@UpdateTimestamp` - Automatic timestamps

### Fetch Strategies
- **LAZY**: Default for collections and relationships (performance)
- **EAGER**: Used selectively for frequently accessed data

### Cascade Types
- **PERSIST**: Save child entities with parent
- **MERGE**: Update child entities with parent
- **ALL**: Full cascade (use cautiously)

### Validation Annotations
- `@NotBlank` - Required string fields
- `@Email` - Email format validation
- `@Size` - Length constraints
- `@Min`, `@Max` - Numeric range validation

---

## Security Considerations

1. **Password Storage**: Encrypted using BCrypt
2. **Role-Based Access**: Enforced at service layer
3. **Audit Trail**: Log entity tracks all operations
4. **Data Privacy**: Sensitive fields marked with @JsonIgnore
5. **Soft Delete**: Recommended for critical entities

---

## Performance Optimization

1. **Indexing**: Applied on foreign keys and unique fields
2. **Lazy Loading**: Default fetch strategy for relationships
3. **Query Optimization**: Use JOIN FETCH for N+1 problem
4. **Caching**: Consider second-level cache for static data
5. **Pagination**: Implemented for large result sets

---

## Future Enhancements

1. **Audit Fields**: Add createdBy, updatedBy to all entities
2. **Soft Delete**: Implement deleted flag and deletedAt timestamp
3. **Versioning**: Add version field for optimistic locking
4. **Multi-tenancy**: Support for multiple institutions
5. **File Attachments**: Support for document uploads
6. **Notifications**: Integration with notification system
7. **Analytics**: Add reporting and analytics entities

---

## Document Version
- **Version**: 1.0
- **Last Updated**: 2024
- **Author**: College ERP Development Team
- **Status**: Active Development

---

## References
- Spring Data JPA Documentation
- Hibernate ORM Documentation
- Database Design Best Practices
- RESTful API Design Guidelines

---

**End of Document**
