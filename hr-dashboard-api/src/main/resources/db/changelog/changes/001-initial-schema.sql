CREATE TABLE IF NOT EXISTS job_position (
    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    created_by INT,
    updated_at TIMESTAMP,
    updated_by INT
);

CREATE INDEX IF NOT EXISTS idx_job_position_name ON job_position(name, status);

CREATE TABLE IF NOT EXISTS "user" (
    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    city VARCHAR(255),
    state VARCHAR(255),
    postal_code INT,
    country VARCHAR(255),
    job_position_id INT NOT NULL,
    status VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    created_by INT,
    updated_at TIMESTAMP,
    updated_by INT,
    FOREIGN KEY (job_position_id) REFERENCES job_position(id)
);

CREATE INDEX IF NOT EXISTS idx_user_details_username ON "user"(username, password, status);

CREATE TABLE IF NOT EXISTS role (
    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY ,
    name VARCHAR(255) NOT NULL
);

INSERT INTO role (name) VALUES ('ADMIN');