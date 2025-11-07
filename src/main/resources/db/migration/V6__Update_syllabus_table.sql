-- Update syllabus table to match entity definition
ALTER TABLE syllabus 
ADD COLUMN IF NOT EXISTS title VARCHAR(255),
ADD COLUMN IF NOT EXISTS description TEXT,
ADD COLUMN IF NOT EXISTS department_id BIGINT,
ADD COLUMN IF NOT EXISTS academic_year VARCHAR(10),
ADD COLUMN IF NOT EXISTS semester INTEGER,
ADD COLUMN IF NOT EXISTS status VARCHAR(20) DEFAULT 'ACTIVE',
ADD COLUMN IF NOT EXISTS created_at TIMESTAMP,
ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;

-- Add foreign key constraint for department
ALTER TABLE syllabus 
ADD CONSTRAINT IF NOT EXISTS fk_syllabus_department 
FOREIGN KEY (department_id) REFERENCES department(id) ON DELETE SET NULL;

-- Add check constraints
ALTER TABLE syllabus 
ADD CONSTRAINT IF NOT EXISTS chk_syllabus_semester 
CHECK (semester IS NULL OR semester BETWEEN 1 AND 8);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_syllabus_department_id ON syllabus(department_id);
CREATE INDEX IF NOT EXISTS idx_syllabus_academic_year ON syllabus(academic_year);
CREATE INDEX IF NOT EXISTS idx_syllabus_status ON syllabus(status);
