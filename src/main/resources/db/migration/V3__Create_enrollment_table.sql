-- Create enrollment table for student-subject enrollment management
CREATE TABLE enrollment (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    academic_year VARCHAR(10) NOT NULL,
    semester INTEGER NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    enrolled_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    grade VARCHAR(5),
    credits INTEGER,
    
    CONSTRAINT fk_enrollment_student FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE,
    CONSTRAINT fk_enrollment_subject FOREIGN KEY (subject_id) REFERENCES subject(id) ON DELETE CASCADE,
    CONSTRAINT uk_enrollment_student_subject UNIQUE (student_id, subject_id, academic_year, semester),
    CONSTRAINT chk_enrollment_status CHECK (status IN ('ACTIVE', 'COMPLETED', 'DROPPED', 'FAILED')),
    CONSTRAINT chk_enrollment_semester CHECK (semester BETWEEN 1 AND 8),
    CONSTRAINT chk_enrollment_credits CHECK (credits > 0)
);

-- Create indexes for better query performance
CREATE INDEX idx_enrollment_student_id ON enrollment(student_id);
CREATE INDEX idx_enrollment_subject_id ON enrollment(subject_id);
CREATE INDEX idx_enrollment_academic_year ON enrollment(academic_year);
CREATE INDEX idx_enrollment_status ON enrollment(status);
