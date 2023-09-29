-- liquibase formatted sql
-- changeset k_shvetsova:1 context:create_table
CREATE TABLE IF NOT EXISTS friendship
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    follower_id VARCHAR(36)                 NOT NULL,
    user_id     VARCHAR(36)                 NOT NULL,
    state       VARCHAR                     NOT NULL,
    created_on  TIMESTAMP WITHOUT TIME ZONE NOT NULL
);