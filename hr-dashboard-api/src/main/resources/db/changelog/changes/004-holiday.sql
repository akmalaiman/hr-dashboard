CREATE TABLE IF NOT EXISTS holiday (
    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(255) NOT NULL,
    holiday_date DATE NOT NULL,
    status VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    created_by INT NOT NULL,
    updated_at TIMESTAMP,
    updated_by INT
);

CREATE INDEX IF NOT EXISTS idx_holiday_date ON holiday(holiday_date, status);