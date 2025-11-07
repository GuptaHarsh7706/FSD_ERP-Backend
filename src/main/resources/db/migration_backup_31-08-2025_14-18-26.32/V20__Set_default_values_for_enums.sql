-- Set default values for enum columns
-- This migration ensures the database has the correct default values for enum columns

-- Set default for attendance.status
ALTER TABLE attendance 
    ALTER COLUMN status SET DEFAULT 'PRESENT';

-- Set default for fees.payment_status
ALTER TABLE fees 
    ALTER COLUMN payment_status SET DEFAULT 'PENDING';

-- Add comments to document the enum values
COMMENT ON COLUMN attendance.status IS 'Possible values: PRESENT, ABSENT, LATE';
COMMENT ON COLUMN fees.payment_status IS 'Possible values: PENDING, PARTIAL, PAID, OVERDUE';
