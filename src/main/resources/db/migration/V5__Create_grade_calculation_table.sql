-- Create grade calculation table for comprehensive grade management
CREATE TABLE grade_calculation (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    academic_year VARCHAR(10) NOT NULL,
    semester INTEGER NOT NULL,
    internal_marks DECIMAL(5,2),
    external_marks DECIMAL(5,2),
    assignment_marks DECIMAL(5,2),
    attendance_percentage DECIMAL(5,2),
    total_marks DECIMAL(5,2),
    grade VARCHAR(5),
    grade_points DECIMAL(3,2),
    credits INTEGER,
    status VARCHAR(20) DEFAULT 'PENDING',
    calculated_at TIMESTAMP,
    calculated_by BIGINT,
    
    CONSTRAINT fk_grade_calculation_student FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE,
    CONSTRAINT fk_grade_calculation_subject FOREIGN KEY (subject_id) REFERENCES subject(id) ON DELETE CASCADE,
    CONSTRAINT fk_grade_calculation_calculated_by FOREIGN KEY (calculated_by) REFERENCES users(id) ON DELETE SET NULL,
    CONSTRAINT uk_grade_calculation_student_subject UNIQUE (student_id, subject_id, academic_year, semester),
    CONSTRAINT chk_grade_calculation_status CHECK (status IN ('PENDING', 'CALCULATED', 'APPROVED', 'PUBLISHED')),
    CONSTRAINT chk_grade_calculation_semester CHECK (semester BETWEEN 1 AND 8),
    CONSTRAINT chk_grade_calculation_marks CHECK (
        internal_marks >= 0 AND internal_marks <= 100 AND
        external_marks >= 0 AND external_marks <= 100 AND
        assignment_marks >= 0 AND assignment_marks <= 100 AND
        total_marks >= 0 AND total_marks <= 100
    ),
    CONSTRAINT chk_grade_calculation_attendance CHECK (attendance_percentage >= 0 AND attendance_percentage <= 100),
    CONSTRAINT chk_grade_calculation_grade_points CHECK (grade_points >= 0 AND grade_points <= 10),
    CONSTRAINT chk_grade_calculation_credits CHECK (credits > 0)
);

-- Create indexes for better query performance
CREATE INDEX idx_grade_calculation_student_id ON grade_calculation(student_id);
CREATE INDEX idx_grade_calculation_subject_id ON grade_calculation(subject_id);
CREATE INDEX idx_grade_calculation_academic_year ON grade_calculation(academic_year);
CREATE INDEX idx_grade_calculation_status ON grade_calculation(status);
CREATE INDEX idx_grade_calculation_semester ON grade_calculation(semester);
