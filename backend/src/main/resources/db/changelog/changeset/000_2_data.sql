--liquibase formatted sql
--changeset dmitry_petrov:000_2_data

--add role ADMIN
INSERT INTO public.role (created_at, updated_at, title) VALUES ('2023-01-16 00:02:30.783655', '2023-01-16 00:02:30.783655', 'ADMIN');

--add role USER
INSERT INTO public.role (created_at, updated_at, title) VALUES ('2023-01-16 00:02:30.783655', '2023-01-16 00:02:30.783655', 'USER');

-- add default user (role:admin) username:admin password:admin
INSERT INTO public."app-user" (created_at, updated_at, username, password, role_id) VALUES ('2023-01-16 00:02:30.783655', '2023-01-16 00:02:30.783655', 'admin', '$2a$10$wMGg.JxeBG.Ju3ynkYIICuxiY9T3lktWL3z.ZWLL4p64/.YnIdobm', 1);
