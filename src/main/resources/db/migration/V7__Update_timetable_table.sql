-- Update timetable table to match entity definition
ALTER TABLE timetable 
ADD COLUMN IF NOT EXISTS start_time TIME,
ADD COLUMN IF NOT EXISTS end_time TIME,
ADD COLUMN IF NOT EXISTS classroom VARCHAR(100),
ADD COLUMN IF NOT EXISTS academic_year VARCHAR(10),
ADD COLUMN IF NOT EXISTS created_at TIMESTAMP,
ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;

-- Add check constraints
ALTER TABLE timetable 
ADD CONSTRAINT IF NOT EXISTS chk_timetable_semester 
CHECK (semester IS NULL OR semester BETWEEN 1 AND 8);

ALTER TABLE timetable 
ADD CONSTRAINT IF NOT EXISTS chk_timetable_times 
CHECK (start_time IS NULL OR end_time IS NULL OR end_time > start_time);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_timetable_academic_year ON timetable(academic_year);
CREATE INDEX IF NOT EXISTS idx_timetable_day_of_week ON timetable(day_of_week);
CREATE INDEX IF NOT EXISTS idx_timetable_start_time ON timetable(start_time);
