-- Create academic calendar table for managing academic events and holidays
CREATE TABLE academic_calendar (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    event_date DATE NOT NULL,
    end_date DATE,
    event_type VARCHAR(50) NOT NULL,
    academic_year VARCHAR(10) NOT NULL,
    semester INTEGER,
    department_id BIGINT,
    is_holiday BOOLEAN DEFAULT FALSE,
    created_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    
    CONSTRAINT fk_academic_calendar_department FOREIGN KEY (department_id) REFERENCES department(id) ON DELETE SET NULL,
    CONSTRAINT fk_academic_calendar_created_by FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL,
    CONSTRAINT chk_academic_calendar_event_type CHECK (event_type IN (
        'EXAM_START', 'EXAM_END', 'SEMESTER_START', 'SEMESTER_END', 
        'HOLIDAY', 'ASSIGNMENT_DEADLINE', 'PROJECT_SUBMISSION',
        'REGISTRATION_START', 'REGISTRATION_END', 'RESULT_DECLARATION',
        'CONVOCATION', 'ORIENTATION', 'WORKSHOP', 'SEMINAR'
    )),
    CONSTRAINT chk_academic_calendar_semester CHECK (semester BETWEEN 1 AND 8),
    CONSTRAINT chk_academic_calendar_dates CHECK (end_date IS NULL OR end_date >= event_date)
);

-- Create indexes for better query performance
CREATE INDEX idx_academic_calendar_event_date ON academic_calendar(event_date);
CREATE INDEX idx_academic_calendar_academic_year ON academic_calendar(academic_year);
CREATE INDEX idx_academic_calendar_event_type ON academic_calendar(event_type);
CREATE INDEX idx_academic_calendar_department_id ON academic_calendar(department_id);
CREATE INDEX idx_academic_calendar_is_holiday ON academic_calendar(is_holiday);
