-- liquibase formatted sql
-- changeset k_shvetsova:1 context:create_table
CREATE TABLE IF NOT EXISTS locations
(
    id  BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    lat FLOAT NOT NULL,
    lon FLOAT NOT NULL
);
CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title              VARCHAR(120),
    description        VARCHAR(7000),
    annotation         VARCHAR(2000),
    category_id        BIGINT,
    initiator_id       BIGINT,
    location_id        BIGINT,
    paid               BOOLEAN DEFAULT false,
    participant_limit  INT     DEFAULT 0,
    confirmed_requests INT,
    request_moderation BOOLEAN DEFAULT true,
    state              VARCHAR,
    created_on         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    event_date         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    published_on       TIMESTAMP WITHOUT TIME ZONE
);

ALTER TABLE events ADD FOREIGN KEY (category_id) REFERENCES categories (id);
ALTER TABLE events ADD FOREIGN KEY (location_id) REFERENCES locations (id);