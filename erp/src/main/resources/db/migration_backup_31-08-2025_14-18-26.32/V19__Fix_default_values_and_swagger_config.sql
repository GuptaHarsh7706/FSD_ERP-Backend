-- Fix default values for enum columns
ALTER TABLE attendance 
    ALTER COLUMN status SET DEFAULT 'PRESENT';

ALTER TABLE fees
    ALTER COLUMN paid_amount SET DEFAULT 0,
    ALTER COLUMN payment_status SET DEFAULT 'PENDING';

ALTER TABLE log
    ALTER COLUMN timestamp SET DEFAULT CURRENT_TIMESTAMP;

-- Add comments for documentation
COMMENT ON COLUMN attendance.status IS 'Possible values: PRESENT, ABSENT, LATE';
COMMENT ON COLUMN fees.payment_status IS 'Possible values: PENDING, PARTIAL, PAID, OVERDUE';
