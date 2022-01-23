CREATE TABLE post (
    id SERIAL PRIMARY KEY,
    name TEXT,
    description TEXT,
    create_date timestamp without time zone
);

CREATE TABLE candidate (
    id SERIAL PRIMARY KEY,
    name TEXT
    cityName TEXT,
    create_date timestamp without time zone
);

CREATE TABLE userWEB (
    id SERIAL PRIMARY KEY,
    name TEXT,
    email TEXT,
    password TEXT
);

CREATE TABLE city (
     id SERIAL PRIMARY KEY,
     name TEXT
);