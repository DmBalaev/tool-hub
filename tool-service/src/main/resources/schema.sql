CREATE SEQUENCE IF NOT EXISTS tool_sequence;

CREATE TABLE IF NOT EXISTS tools (
    id BIGINT PRIMARY KEY DEFAULT nextval('tool_sequence'),
    name VARCHAR(255),
    owner_id BIGINT,
    is_available BOOLEAN,
    price NUMERIC,
    category VARCHAR(50)
    );

CREATE SEQUENCE IF NOT EXISTS account_sequence;

CREATE TABLE IF NOT EXISTS account (
    id BIGINT PRIMARY KEY DEFAULT nextval('account_sequence'),
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    role VARCHAR(50)
    );