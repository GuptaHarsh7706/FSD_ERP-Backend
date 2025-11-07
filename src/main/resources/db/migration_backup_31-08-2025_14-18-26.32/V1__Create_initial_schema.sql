-- Create initial schema for College ERP System
-- Based on the provided ER diagram

-- Create user table
CREATE TABLE users (
    user_id BIGSERIAL PRIMARY KEY,
    department_id BIGINT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone_number VARCHAR(20),
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create faculty table
CREATE TABLE faculty (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    designation VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Create department table
CREATE TABLE department (
    id BIGSERIAL PRIMARY KEY,
    head_id BIGINT,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (head_id) REFERENCES faculty(id)
);

-- Add foreign key constraint to users table for department
ALTER TABLE users ADD CONSTRAINT fk_user_department 
    FOREIGN KEY (department_id) REFERENCES department(id);

-- Create student table
CREATE TABLE student (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    prn_number VARCHAR(50) UNIQUE NOT NULL,
    batch VARCHAR(10),
    semester INTEGER,
    division VARCHAR(10),
    roll_number VARCHAR(20),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Create subject table
CREATE TABLE subject (
    id BIGSERIAL PRIMARY KEY,
    department_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    semester INTEGER,
    code VARCHAR(20) UNIQUE,
    FOREIGN KEY (department_id) REFERENCES department(id)
);

-- Create timetable table
CREATE TABLE timetable (
    id BIGSERIAL PRIMARY KEY,
    subject_id BIGINT NOT NULL,
    faculty_id BIGINT NOT NULL,
    department_id BIGINT NOT NULL,
    semester INTEGER,
    day_of_week VARCHAR(20),
    time_slot VARCHAR(50),
    FOREIGN KEY (subject_id) REFERENCES subject(id),
    FOREIGN KEY (faculty_id) REFERENCES faculty(id),
    FOREIGN KEY (department_id) REFERENCES department(id)
);

-- Create syllabus table
CREATE TABLE syllabus (
    id BIGSERIAL PRIMARY KEY,
    updated_by BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    content TEXT,
    FOREIGN KEY (updated_by) REFERENCES faculty(id),
    FOREIGN KEY (subject_id) REFERENCES subject(id)
);

-- Create homework table
CREATE TABLE homework (
    id BIGSERIAL PRIMARY KEY,
    subject_id BIGINT NOT NULL,
    faculty_id BIGINT NOT NULL,
    description TEXT,
    due_date DATE,
    FOREIGN KEY (subject_id) REFERENCES subject(id),
    FOREIGN KEY (faculty_id) REFERENCES faculty(id)
);

-- Create exam table
CREATE TABLE exam (
    id BIGSERIAL PRIMARY KEY,
    subject_id BIGINT NOT NULL,
    department_id BIGINT NOT NULL,
    date DATE,
    name VARCHAR(255),
    FOREIGN KEY (subject_id) REFERENCES subject(id),
    FOREIGN KEY (department_id) REFERENCES department(id)
);

-- Create marks table
CREATE TABLE marks (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL,
    exam_id BIGINT NOT NULL,
    marks_obtained DECIMAL(5,2),
    total_marks DECIMAL(5,2),
    FOREIGN KEY (student_id) REFERENCES student(id),
    FOREIGN KEY (exam_id) REFERENCES exam(id)
);

-- Create attendance table
CREATE TABLE attendance (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    date DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'PRESENT',
    FOREIGN KEY (student_id) REFERENCES student(id),
    FOREIGN KEY (subject_id) REFERENCES subject(id)
);

-- Create fees table
CREATE TABLE fees (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    paid_amount DECIMAL(10,2) DEFAULT 0,
    due_date DATE,
    payment_status VARCHAR(20) DEFAULT 'PENDING',
    FOREIGN KEY (student_id) REFERENCES student(id)
);

-- Create accreditation table
CREATE TABLE accreditation (
    id BIGSERIAL PRIMARY KEY,
    department_id BIGINT NOT NULL,
    body_name VARCHAR(255),
    score DECIMAL(5,2),
    remarks TEXT,
    FOREIGN KEY (department_id) REFERENCES department(id)
);

-- Create backup table
CREATE TABLE backup (
    id BIGSERIAL PRIMARY KEY,
    file_path VARCHAR(500) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create log table
CREATE TABLE log (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    action VARCHAR(255),
    entity_type VARCHAR(100),
    entity_id BIGINT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Create indexes for better performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_department ON users(department_id);
CREATE INDEX idx_student_prn ON student(prn_number);
CREATE INDEX idx_student_user ON student(user_id);
CREATE INDEX idx_faculty_user ON faculty(user_id);
CREATE INDEX idx_subject_department ON subject(department_id);
CREATE INDEX idx_attendance_student_date ON attendance(student_id, date);
CREATE INDEX idx_marks_student ON marks(student_id);
CREATE INDEX idx_timetable_faculty_day ON timetable(faculty_id, day_of_week);
