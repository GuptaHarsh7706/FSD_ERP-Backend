-- Insert sample data for testing the College ERP System
-- COMMENTED OUT - User prefers to create data manually via API

/*
-- Insert sample departments
INSERT INTO department (name, created_at) VALUES 
('Computer Science', CURRENT_TIMESTAMP),
('Electronics Engineering', CURRENT_TIMESTAMP),
('Mechanical Engineering', CURRENT_TIMESTAMP),
('Information Technology', CURRENT_TIMESTAMP);

-- Insert sample users (passwords are 'password123' encoded with BCrypt)
INSERT INTO users (name, email, phone_number, password, role, department_id, created_at, updated_at) VALUES 
('Admin User', 'admin@college.edu', '9876543210', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'ADMIN', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Dr. John Smith', 'john.smith@college.edu', '9876543211', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'FACULTY', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Prof. Jane Doe', 'jane.doe@college.edu', '9876543212', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'FACULTY', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Alice Johnson', 'alice.johnson@student.college.edu', '9876543213', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'STUDENT', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Bob Wilson', 'bob.wilson@student.college.edu', '9876543214', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'STUDENT', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert faculty records
INSERT INTO faculty (user_id, designation) VALUES 
(2, 'Professor'),
(3, 'Associate Professor');

-- Update department heads
UPDATE department SET head_id = 1 WHERE id = 1;
UPDATE department SET head_id = 2 WHERE id = 2;

-- Insert student records
INSERT INTO student (user_id, prn_number, batch, semester, division, roll_number) VALUES 
(4, 'PRN2024001', '2024', 1, 'A', 'CS001'),
(5, 'PRN2024002', '2024', 1, 'A', 'CS002');

-- Insert subjects
INSERT INTO subject (department_id, name, semester, code) VALUES 
(1, 'Programming Fundamentals', 1, 'CS101'),
(1, 'Data Structures', 2, 'CS201'),
(1, 'Database Management Systems', 3, 'CS301'),
(2, 'Digital Electronics', 1, 'EC101'),
(2, 'Microprocessors', 2, 'EC201');

-- Insert sample fees
INSERT INTO fees (student_id, amount, paid_amount, due_date, payment_status) VALUES 
(1, 50000.00, 25000.00, '2024-12-31', 'PARTIAL'),
(2, 50000.00, 0.00, '2024-12-31', 'PENDING');

-- Insert sample exams
INSERT INTO exam (subject_id, department_id, date, name) VALUES 
(1, 1, '2024-12-15', 'Mid Term Exam'),
(2, 1, '2024-12-20', 'Final Exam'),
(4, 2, '2024-12-18', 'Unit Test 1');

-- Insert sample attendance
INSERT INTO attendance (student_id, subject_id, date, status) VALUES 
(1, 1, '2024-08-01', 'PRESENT'),
(1, 1, '2024-08-02', 'PRESENT'),
(1, 1, '2024-08-03', 'ABSENT'),
(2, 1, '2024-08-01', 'PRESENT'),
(2, 1, '2024-08-02', 'LATE'),
(2, 1, '2024-08-03', 'PRESENT');

-- Insert sample marks
INSERT INTO marks (student_id, exam_id, marks_obtained, total_marks) VALUES 
(1, 1, 85.50, 100.00),
(2, 1, 78.00, 100.00),
(1, 2, 92.00, 100.00),
(2, 2, 88.50, 100.00);

-- Insert sample timetable
INSERT INTO timetable (subject_id, faculty_id, department_id, semester, day_of_week, time_slot) VALUES 
(1, 1, 1, 1, 'MONDAY', '09:00-10:00'),
(1, 1, 1, 1, 'WEDNESDAY', '09:00-10:00'),
(1, 1, 1, 1, 'FRIDAY', '09:00-10:00'),
(4, 2, 2, 1, 'TUESDAY', '10:00-11:00'),
(4, 2, 2, 1, 'THURSDAY', '10:00-11:00');

-- Insert sample syllabus
INSERT INTO syllabus (updated_by, subject_id, content) VALUES 
(1, 1, 'Introduction to Programming: Variables, Data Types, Control Structures, Functions, Arrays, Object-Oriented Programming Basics'),
(2, 4, 'Digital Electronics: Boolean Algebra, Logic Gates, Combinational Circuits, Sequential Circuits, Memory Systems');

-- Insert sample homework
INSERT INTO homework (subject_id, faculty_id, description, due_date) VALUES 
(1, 1, 'Complete exercises 1-10 from Chapter 3: Control Structures', '2024-09-15'),
(4, 2, 'Design a 4-bit binary counter using JK flip-flops', '2024-09-20');

-- Insert sample accreditation
INSERT INTO accreditation (department_id, body_name, score, remarks) VALUES 
(1, 'NAAC', 3.5, 'Good academic performance with scope for improvement in research'),
(2, 'NBA', 4.0, 'Excellent infrastructure and faculty qualifications');

-- Insert sample backup records
INSERT INTO backup (file_path, timestamp, created_at) VALUES 
('/backups/db_backup_2024_08_01.sql', '2024-08-01 02:00:00', CURRENT_TIMESTAMP),
('/backups/db_backup_2024_08_15.sql', '2024-08-15 02:00:00', CURRENT_TIMESTAMP);

-- Insert sample log entries
INSERT INTO log (user_id, action, entity_type, entity_id, timestamp) VALUES 
(1, 'CREATE', 'Student', 1, CURRENT_TIMESTAMP),
(1, 'CREATE', 'Student', 2, CURRENT_TIMESTAMP),
(2, 'UPDATE', 'Marks', 1, CURRENT_TIMESTAMP),
(3, 'CREATE', 'Homework', 1, CURRENT_TIMESTAMP);
*/
