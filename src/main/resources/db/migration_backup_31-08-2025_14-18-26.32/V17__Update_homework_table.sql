-- Update homework table to match entity definition
ALTER TABLE homework 
ADD COLUMN IF NOT EXISTS title VARCHAR(255),
ADD COLUMN IF NOT EXISTS student_id BIGINT,
ADD COLUMN IF NOT EXISTS max_marks INTEGER,
ADD COLUMN IF NOT EXISTS status VARCHAR(20) DEFAULT 'ASSIGNED',
ADD COLUMN IF NOT EXISTS assigned_at TIMESTAMP,
ADD COLUMN IF NOT EXISTS submitted_at TIMESTAMP,
ADD COLUMN IF NOT EXISTS submission TEXT;

-- Add foreign key constraint for student
ALTER TABLE homework 
ADD CONSTRAINT IF NOT EXISTS fk_homework_student 
FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE SET NULL;

-- Add check constraints
ALTER TABLE homework 
ADD CONSTRAINT IF NOT EXISTS chk_homework_status 
CHECK (status IN ('ASSIGNED', 'SUBMITTED', 'GRADED', 'OVERDUE'));

ALTER TABLE homework 
ADD CONSTRAINT IF NOT EXISTS chk_homework_max_marks 
CHECK (max_marks IS NULL OR max_marks > 0);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_homework_student_id ON homework(student_id);
CREATE INDEX IF NOT EXISTS idx_homework_status ON homework(status);
CREATE INDEX IF NOT EXISTS idx_homework_due_date ON homework(due_date);
