--liquibase formatted sql
--changeset dmitry_petrov:002


ALTER TABLE IF EXISTS course
    ADD COLUMN IF NOT EXISTS paused bool DEFAULT false;