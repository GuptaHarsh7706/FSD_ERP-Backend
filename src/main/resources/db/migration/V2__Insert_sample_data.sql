-- Comprehensive Sample Data for College ERP System
-- COMMENTED OUT - User prefers to create data manually via API
-- To enable sample data, uncomment all sections below

/*
-- All 18 tables populated with realistic test data
-- Password for all users: 'password123'

-- 1. DEPARTMENTS (4 departments)
INSERT INTO department (name, created_at) VALUES 
('Computer Science', CURRENT_TIMESTAMP),
('Electronics Engineering', CURRENT_TIMESTAMP),
('Mechanical Engineering', CURRENT_TIMESTAMP),
('Information Technology', CURRENT_TIMESTAMP);

-- 2. USERS (10 users: 1 Admin, 1 Principal, 4 Faculty, 2 Staff, 2 Students)
-- Password: 'password123' -> BCrypt hash
INSERT INTO users (name, email, phone_number, password, role, department_id, created_at, updated_at) VALUES 
('Admin User', 'admin@college.edu', '9876543210', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Dr. Principal Kumar', 'principal@college.edu', '9876543211', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'PRINCIPAL', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Dr. John Smith', 'john.smith@college.edu', '9876543212', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'FACULTY', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Prof. Jane Doe', 'jane.doe@college.edu', '9876543213', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'FACULTY', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Dr. Robert Brown', 'robert.brown@college.edu', '9876543214', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'FACULTY', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Prof. Sarah Wilson', 'sarah.wilson@college.edu', '9876543215', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'FACULTY', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Staff Member 1', 'staff1@college.edu', '9876543216', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'STAFF', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Staff Member 2', 'staff2@college.edu', '9876543217', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'STAFF', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Alice Johnson', 'alice.johnson@student.college.edu', '9876543218', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'STUDENT', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Bob Wilson', 'bob.wilson@student.college.edu', '9876543219', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'STUDENT', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 3. FACULTY (4 faculty members)
INSERT INTO faculty (user_id, designation) VALUES 
(3, 'Professor & HOD'),
(4, 'Associate Professor'),
(5, 'Assistant Professor'),
(6, 'Assistant Professor');

-- 4. Update DEPARTMENT heads
UPDATE department SET head_id = 1 WHERE id = 1;
UPDATE department SET head_id = 2 WHERE id = 2;
UPDATE department SET head_id = 3 WHERE id = 3;
UPDATE department SET head_id = 4 WHERE id = 4;

-- 5. STUDENTS (2 students)
INSERT INTO student (user_id, prn_number, batch, semester, division, roll_number) VALUES 
(9, 'PRN2024001', '2024', 1, 'A', '001'),
(10, 'PRN2024002', '2024', 1, 'A', '002');

-- 6. SUBJECTS
(2, 'Digital Electronics', 1, 'EC101'),
(2, 'Microprocessors', 2, 'EC201'),
(3, 'Engineering Mechanics', 1, 'ME101'),
(4, 'Web Technologies', 3, 'IT301');

-- 7. ENROLLMENT (Students enrolled in subjects)
INSERT INTO enrollment (student_id, subject_id, academic_year, semester, status, enrolled_at, credits) VALUES 
(1, 1, '2024-25', 1, 'ACTIVE', CURRENT_TIMESTAMP, 4),
(1, 5, '2024-25', 1, 'ACTIVE', CURRENT_TIMESTAMP, 3),
(2, 1, '2024-25', 1, 'ACTIVE', CURRENT_TIMESTAMP, 4),
(2, 5, '2024-25', 1, 'ACTIVE', CURRENT_TIMESTAMP, 3);

-- 8. TIMETABLE (Class schedule)
INSERT INTO timetable (subject_id, faculty_id, department_id, semester, day_of_week, start_time, end_time, classroom, academic_year, time_slot, created_at, updated_at) VALUES 
(1, 1, 1, 1, 'MONDAY', '09:00:00', '10:00:00', 'Room 101', '2024-25', '09:00-10:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 1, 1, 1, 'WEDNESDAY', '09:00:00', '10:00:00', 'Room 101', '2024-25', '09:00-10:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 1, 1, 1, 'FRIDAY', '09:00:00', '10:00:00', 'Room 101', '2024-25', '09:00-10:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 2, 2, 1, 'TUESDAY', '10:00:00', '11:00:00', 'Lab 201', '2024-25', '10:00-11:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 2, 2, 1, 'THURSDAY', '10:00:00', '11:00:00', 'Lab 201', '2024-25', '10:00-11:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 9. SYLLABUS (Course content)
INSERT INTO syllabus (title, description, updated_by, subject_id, department_id, content, academic_year, semester, status, created_at, updated_at) VALUES 
('Programming Fundamentals Syllabus', 'Complete syllabus for CS101', 1, 1, 1, 'Unit 1: Introduction to Programming\nUnit 2: Variables and Data Types\nUnit 3: Control Structures\nUnit 4: Functions and Arrays\nUnit 5: Object-Oriented Programming', '2024-25', 1, 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Digital Electronics Syllabus', 'Complete syllabus for EC101', 2, 5, 2, 'Unit 1: Number Systems\nUnit 2: Boolean Algebra\nUnit 3: Logic Gates\nUnit 4: Combinational Circuits\nUnit 5: Sequential Circuits', '2024-25', 1, 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 10. HOMEWORK (Assignments)
INSERT INTO homework (title, subject_id, faculty_id, student_id, description, due_date, max_marks, status, assigned_at) VALUES 
('Assignment 1: Variables', 1, 1, NULL, 'Complete exercises 1-10 from Chapter 2: Variables and Data Types', CURRENT_DATE + INTERVAL '7 days', 10, 'ASSIGNED', CURRENT_TIMESTAMP),
('Lab Assignment 1', 5, 2, NULL, 'Design a 4-bit binary counter using JK flip-flops', CURRENT_DATE + INTERVAL '14 days', 20, 'ASSIGNED', CURRENT_TIMESTAMP),
('Programming Project', 1, 1, 1, 'Create a simple calculator program', CURRENT_DATE + INTERVAL '21 days', 50, 'ASSIGNED', CURRENT_TIMESTAMP);

-- 11. EXAMS (Scheduled exams)
INSERT INTO exam (subject_id, department_id, date, name) VALUES 
(1, 1, CURRENT_DATE + INTERVAL '30 days', 'Mid Term Exam - Programming'),
(1, 1, CURRENT_DATE + INTERVAL '90 days', 'Final Exam - Programming'),
(5, 2, CURRENT_DATE + INTERVAL '35 days', 'Unit Test 1 - Digital Electronics'),
(2, 1, CURRENT_DATE + INTERVAL '60 days', 'Mid Term - Data Structures');

-- 12. MARKS (Student grades)
INSERT INTO marks (student_id, exam_id, marks_obtained, total_marks) VALUES 
(1, 1, 85.50, 100.00),
(2, 1, 78.00, 100.00),
(1, 3, 92.00, 100.00),
(2, 3, 88.50, 100.00);

-- 13. ATTENDANCE (Daily attendance)
INSERT INTO attendance (student_id, subject_id, date, status) VALUES 
(1, 1, CURRENT_DATE - INTERVAL '5 days', 'PRESENT'),
(1, 1, CURRENT_DATE - INTERVAL '4 days', 'PRESENT'),
(1, 1, CURRENT_DATE - INTERVAL '3 days', 'ABSENT'),
(1, 1, CURRENT_DATE - INTERVAL '2 days', 'PRESENT'),
(1, 1, CURRENT_DATE - INTERVAL '1 day', 'PRESENT'),
(2, 1, CURRENT_DATE - INTERVAL '5 days', 'PRESENT'),
(2, 1, CURRENT_DATE - INTERVAL '4 days', 'LATE'),
(2, 1, CURRENT_DATE - INTERVAL '3 days', 'PRESENT'),
(2, 1, CURRENT_DATE - INTERVAL '2 days', 'PRESENT'),
(2, 1, CURRENT_DATE - INTERVAL '1 day', 'ABSENT');

-- 14. FEES (Student fee records)
INSERT INTO fees (student_id, amount, paid_amount, due_date, payment_status) VALUES 
(1, 50000.00, 25000.00, CURRENT_DATE + INTERVAL '60 days', 'PARTIAL'),
(2, 50000.00, 50000.00, CURRENT_DATE + INTERVAL '60 days', 'PAID'),
(1, 5000.00, 0.00, CURRENT_DATE + INTERVAL '30 days', 'PENDING'),
(2, 5000.00, 2500.00, CURRENT_DATE + INTERVAL '30 days', 'PARTIAL');

-- 15. GRADE_CALCULATION (Comprehensive grade records)
INSERT INTO grade_calculation (student_id, subject_id, academic_year, semester, internal_marks, external_marks, assignment_marks, attendance_percentage, total_marks, grade, grade_points, credits, status, calculated_at, calculated_by) VALUES 
(1, 1, '2024-25', 1, 28.50, 85.00, 9.50, 80.00, 123.00, 'A', 9.00, 4, 'CALCULATED', CURRENT_TIMESTAMP, 1),
(2, 1, '2024-25', 1, 25.00, 78.00, 8.00, 75.00, 111.00, 'B+', 8.50, 4, 'CALCULATED', CURRENT_TIMESTAMP, 1);

-- 16. ACADEMIC_CALENDAR (Important dates)
INSERT INTO academic_calendar (title, description, event_date, end_date, event_type, academic_year, semester, department_id, is_holiday, created_by, created_at, updated_at) VALUES 
('Semester Start', 'First day of Semester 1', CURRENT_DATE - INTERVAL '30 days', NULL, 'SEMESTER_START', '2024-25', 1, NULL, false, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Mid Term Exams', 'Mid semester examinations', CURRENT_DATE + INTERVAL '30 days', CURRENT_DATE + INTERVAL '40 days', 'EXAM_START', '2024-25', 1, NULL, false, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Diwali Holiday', 'Festival holiday', CURRENT_DATE + INTERVAL '15 days', CURRENT_DATE + INTERVAL '17 days', 'HOLIDAY', '2024-25', 1, NULL, true, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Final Exams', 'End semester examinations', CURRENT_DATE + INTERVAL '90 days', CURRENT_DATE + INTERVAL '100 days', 'EXAM_START', '2024-25', 1, NULL, false, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Result Declaration', 'Semester results published', CURRENT_DATE + INTERVAL '110 days', NULL, 'RESULT_DECLARATION', '2024-25', 1, NULL, false, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 17. ACCREDITATION (Department accreditations)
INSERT INTO accreditation (name, description, department_id, accrediting_body, valid_from, valid_to, status, score, remarks) VALUES 
('NAAC Accreditation', 'National Assessment and Accreditation Council', 1, 'NAAC', '2023-01-01', '2028-01-01', 'ACTIVE', 3.5, 'Good academic performance with scope for improvement in research'),
('NBA Accreditation', 'National Board of Accreditation', 2, 'NBA', '2022-06-01', '2027-06-01', 'ACTIVE', 4.0, 'Excellent infrastructure and faculty qualifications'),
('NAAC Accreditation', 'National Assessment and Accreditation Council', 3, 'NAAC', '2023-01-01', '2028-01-01', 'ACTIVE', 3.2, 'Satisfactory performance'),
('NBA Accreditation', 'National Board of Accreditation', 4, 'NBA', '2023-03-01', '2028-03-01', 'ACTIVE', 3.8, 'Good overall performance');

-- 18. BACKUP (System backup records)
INSERT INTO backup (name, description, file_path, size, status, created_at) VALUES 
('Daily Backup - Aug 01', 'Automated daily backup', '/backups/db_backup_2024_08_01.sql', 52428800, 'COMPLETED', CURRENT_TIMESTAMP - INTERVAL '30 days'),
('Daily Backup - Aug 15', 'Automated daily backup', '/backups/db_backup_2024_08_15.sql', 54525952, 'COMPLETED', CURRENT_TIMESTAMP - INTERVAL '15 days'),
('Weekly Backup', 'Weekly full backup', '/backups/db_backup_weekly_2024_09_01.sql', 104857600, 'COMPLETED', CURRENT_TIMESTAMP - INTERVAL '5 days'),
('Monthly Backup', 'Monthly archive backup', '/backups/db_backup_monthly_2024_09.sql', 157286400, 'COMPLETED', CURRENT_TIMESTAMP - INTERVAL '1 day');

-- 19. LOG (System activity logs)
INSERT INTO log (user_id, action, entity_type, entity_id, timestamp) VALUES 
(1, 'CREATE', 'Department', 1, CURRENT_TIMESTAMP - INTERVAL '30 days'),
(1, 'CREATE', 'User', 3, CURRENT_TIMESTAMP - INTERVAL '29 days'),
(1, 'CREATE', 'Student', 1, CURRENT_TIMESTAMP - INTERVAL '28 days'),
(1, 'CREATE', 'Student', 2, CURRENT_TIMESTAMP - INTERVAL '28 days'),
(3, 'CREATE', 'Subject', 1, CURRENT_TIMESTAMP - INTERVAL '27 days'),
(3, 'CREATE', 'Homework', 1, CURRENT_TIMESTAMP - INTERVAL '7 days'),
(3, 'UPDATE', 'Marks', 1, CURRENT_TIMESTAMP - INTERVAL '5 days'),
(4, 'CREATE', 'Exam', 3, CURRENT_TIMESTAMP - INTERVAL '10 days'),
(7, 'CREATE', 'Fees', 1, CURRENT_TIMESTAMP - INTERVAL '20 days'),
(7, 'UPDATE', 'Fees', 1, CURRENT_TIMESTAMP - INTERVAL '10 days');

-- Summary: Sample data inserted successfully
-- Total records:
-- 4 Departments
-- 10 Users (1 Admin, 1 Principal, 4 Faculty, 2 Staff, 2 Students)
-- 4 Faculty records
-- 2 Students
-- 8 Subjects
-- 4 Enrollments
-- 5 Timetable entries
-- 2 Syllabus records
-- 3 Homework assignments
-- 4 Exams
-- 4 Marks entries
-- 10 Attendance records
-- 4 Fee records
-- 2 Grade calculations
-- 5 Academic calendar events
-- 4 Accreditations
-- 4 Backup records
-- 10 Log entries
*/
