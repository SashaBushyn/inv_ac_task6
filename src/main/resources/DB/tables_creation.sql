CREATE  TABLE IF NOT EXISTS users
(
    id         SERIAL PRIMARY KEY,
    first_name VARCHAR(50),
    last_name  VARCHAR(50),
    email      VARCHAR(100) UNIQUE
);
CREATE TABLE IF NOT EXISTS organizations
(
    id      SERIAL PRIMARY KEY,
    name    VARCHAR(50),
    address VARCHAR(255)
);