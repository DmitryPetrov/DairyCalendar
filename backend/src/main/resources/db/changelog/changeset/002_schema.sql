--liquibase formatted sql
--changeset dmitry_petrov:002

ALTER TABLE "course" ADD COLUMN position INTEGER DEFAULT 0;