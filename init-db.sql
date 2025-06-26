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

INSERT INTO
    users (
        username,
        title,
        first_name,
        last_name,
        email,
        gender,
        picture_url,
        country,
        state_province,
        city
    )
VALUES (
        'jose',
        'Mr.',
        'Jose Luis',
        'Contreras',
        'email@dominio.com',
        0,
        'https://randomuser.me/api/portraits',
        'Spain',
        'Alicante',
        'Alicante'
    );

INSERT INTO
    users (
        username,
        title,
        first_name,
        last_name,
        email,
        gender,
        picture_url,
        country,
        state_province,
        city
    )
VALUES (
        'jaydee',
        'Mr.',
        'John',
        'Doe',
        'someemail@dominio.com',
        0,
        'https://randomuser.me/api/portraits',
        'United States',
        'California',
        'Los Angeles'
    );

INSERT INTO
    users (
        username,
        title,
        first_name,
        last_name,
        email,
        gender,
        picture_url,
        country,
        state_province,
        city
    )
VALUES (
        'juanita23',
        'Ms.',
        'Juanita',
        'Velez',
        'juanita@dominio.com',
        1,
        'https://randomuser.me/api/portraits',
        'Mexico',
        'Jalisco',
        'Guadalajara'
    );

INSERT INTO
    users (
        username,
        title,
        first_name,
        last_name,
        email,
        gender,
        picture_url,
        country,
        state_province,
        city
    )
VALUES (
        'primillochico',
        'Mr.',
        'Baity',
        'Bait',
        'el_baito@dominio.com',
        0,
        'https://randomuser.me/api/portraits',
        'Spain',
        'Malaga',
        'Fuengirola'
    );