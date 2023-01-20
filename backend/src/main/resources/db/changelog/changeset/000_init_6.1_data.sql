--liquibase formatted sql
--changeset dmitry_petrov:6.1

DELETE FROM day WHERE id IN (
    SELECT d1.id
    FROM public.day AS d1, public.day AS d2
    WHERE d1.course_id = d2.course_id
      AND d1.date = d2.date
      AND d1.id != d2.id
      AND d1.created_at < d2.created_at
    )