CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    title VARCHAR(255),
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    gender NUMERIC(1) NOT NULL,
    picture_url VARCHAR(255),
    country VARCHAR(255) NOT NULL,
    state_province VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL
);