CREATE TABLE post (
    id SERIAL PRIMARY KEY,
    name TEXT,
    description TEXT,
    created TEXT
);

CREATE TABLE candidate (
    id SERIAL PRIMARY KEY,
    name TEXT
);

CREATE TABLE user (
    id SERIAL PRIMARY KEY,
    name TEXT,
    email TEXT,
    password TEXT
);