CREATE TABLE IF NOT EXISTS department (
    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    created_by INT,
    updated_at TIMESTAMP,
    updated_by INT
);

CREATE INDEX IF NOT EXISTS idx_department_name ON department(name, status);

ALTER table "user"
ADD COLUMN department_id INT;

ALTER table "user"
ADD CONSTRAINT fk_user_department_id FOREIGN KEY (department_id) REFERENCES department(id);