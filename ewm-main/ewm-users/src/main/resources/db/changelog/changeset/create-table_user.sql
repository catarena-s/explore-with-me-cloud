-- liquibase formatted sql
-- changeset k_shvetsova:1 context:create_table
CREATE TABLE IF NOT EXISTS users
(
    id             BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name           VARCHAR(250) NOT NULL,
    email          VARCHAR(254) NOT NULL CONSTRAINT UQ_USER_EMAIL UNIQUE,
    auto_subscribe BOOLEAN DEFAULT false
);