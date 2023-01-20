--liquibase formatted sql
--changeset dmitry_petrov:6.2

ALTER TABLE public.day ADD CONSTRAINT day_course_id_date_uq UNIQUE (course_id, date);