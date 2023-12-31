-- liquibase formatted sql
-- changeset k_shvetsova:1 context:create_table
CREATE TABLE IF NOT EXISTS notification
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    sender_type VARCHAR(255)                NOT NULL,
    consumer_id     VARCHAR(36)                 NOT NULL,
    sender_id   VARCHAR(36)                 NOT NULL,
    text        varchar,
    created     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    read        BOOLEAN                     NOT NULL DEFAULT false
);
