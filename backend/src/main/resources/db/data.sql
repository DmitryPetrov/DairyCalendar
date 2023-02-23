
INSERT INTO public."app-user" (created_at, updated_at, username, password, role_id) VALUES ('2023-02-23', '2023-02-23', 'user_1', '$2a$10$.Av4V/gzwRX7GLSzmM0LC.pfwYxCrFyfme6CgJJG8FTI6yBqczZp6', 2);
INSERT INTO public."app-user" (created_at, updated_at, username, password, role_id) VALUES ('2023-02-23', '2023-02-23', 'user_2', '$2a$10$xep/PsfIZYsMPxbqbQBBouSNG5AyTohGAj6wYg9zDCXFYBl/2Zf0q', 2);

INSERT INTO "public"."tag" ("created_at", "updated_at", "tag") VALUES ('2023-02-23 12:45:52.150940', '2023-02-23 12:45:52.150960', 'tag_2');
INSERT INTO "public"."tag" ("created_at", "updated_at", "tag") VALUES ('2023-02-23 12:45:52.151458', '2023-02-23 12:45:52.151467', 'tag_1');
INSERT INTO "public"."tag" ("created_at", "updated_at", "tag") VALUES ('2023-02-23 12:46:27.386056', '2023-02-23 12:46:27.386074', 'tag_3');

INSERT INTO "public"."tag-collection" ("created_at", "updated_at") VALUES ('2023-02-23 12:45:52.147988', '2023-02-23 12:45:52.148019');
INSERT INTO "public"."tag-collection" ("created_at", "updated_at") VALUES ('2023-02-23 12:46:27.384930', '2023-02-23 12:46:27.384968');
INSERT INTO "public"."tag-collection" ("created_at", "updated_at") VALUES ('2023-02-23 12:46:40.251089', '2023-02-23 12:46:40.251150');

INSERT INTO "public"."tag-collection_tag" ("tag-collection_id", "tag_id") VALUES (1, 1);
INSERT INTO "public"."tag-collection_tag" ("tag-collection_id", "tag_id") VALUES (1, 2);
INSERT INTO "public"."tag-collection_tag" ("tag-collection_id", "tag_id") VALUES (2, 3);
INSERT INTO "public"."tag-collection_tag" ("tag-collection_id", "tag_id") VALUES (2, 2);
INSERT INTO "public"."tag-collection_tag" ("tag-collection_id", "tag_id") VALUES (3, 1);
INSERT INTO "public"."tag-collection_tag" ("tag-collection_id", "tag_id") VALUES (3, 3);

INSERT INTO "public"."course" ("created_at", "updated_at", "description", "position", "title", "tag-collection_id", "app-user_id") VALUES ('2023-02-23 12:45:52.151885', '2023-02-23 12:45:52.151894', 'description', 1, 'course_1', 1, 2);
INSERT INTO "public"."course" ("created_at", "updated_at", "description", "position", "title", "tag-collection_id", "app-user_id") VALUES ('2023-02-23 12:46:27.387138', '2023-02-23 12:46:27.387156', 'description', 2, 'course_2', 2, 2);
INSERT INTO "public"."course" ("created_at", "updated_at", "description", "position", "title", "tag-collection_id", "app-user_id") VALUES ('2023-02-23 12:46:40.253452', '2023-02-23 12:46:40.253483', 'description', 3, 'course_3', 3, 2);

INSERT INTO "public"."course-step" ("created_at", "updated_at", "assessment", "date", "course_id") VALUES ('2023-02-23 17:23:47.866199', '2023-02-23 17:23:47.866253', 3, '2023-02-23', 1);
INSERT INTO "public"."course-step" ("created_at", "updated_at", "assessment", "date", "course_id") VALUES ('2023-02-23 17:23:47.872162', '2023-02-23 17:23:47.872180', 3, '2023-02-23', 2);
INSERT INTO "public"."course-step" ("created_at", "updated_at", "assessment", "date", "course_id") VALUES ('2023-02-23 17:23:47.872883', '2023-02-23 17:23:47.872895', 3, '2023-02-23', 3);
INSERT INTO "public"."course-step" ("created_at", "updated_at", "assessment", "date", "course_id") VALUES ('2023-02-23 17:23:54.882282', '2023-02-23 17:23:54.882308', 0, '2023-02-22', 1);
INSERT INTO "public"."course-step" ("created_at", "updated_at", "assessment", "date", "course_id") VALUES ('2023-02-23 17:23:54.883559', '2023-02-23 17:23:54.883576', 3, '2023-02-22', 2);
INSERT INTO "public"."course-step" ("created_at", "updated_at", "assessment", "date", "course_id") VALUES ('2023-02-23 17:23:54.884677', '2023-02-23 17:23:54.884695', 3, '2023-02-22', 3);
INSERT INTO "public"."course-step" ("created_at", "updated_at", "assessment", "date", "course_id") VALUES ('2023-02-23 17:24:00.435278', '2023-02-23 17:24:00.435318', 3, '2023-02-21', 1);
INSERT INTO "public"."course-step" ("created_at", "updated_at", "assessment", "date", "course_id") VALUES ('2023-02-23 17:24:00.437089', '2023-02-23 17:24:00.437124', 0, '2023-02-21', 2);
INSERT INTO "public"."course-step" ("created_at", "updated_at", "assessment", "date", "course_id") VALUES ('2023-02-23 17:24:00.438382', '2023-02-23 17:24:00.438398', 3, '2023-02-21', 3);
INSERT INTO "public"."course-step" ("created_at", "updated_at", "assessment", "date", "course_id") VALUES ('2023-02-23 17:24:07.133234', '2023-02-23 17:24:07.133256', 0, '2023-02-20', 1);
INSERT INTO "public"."course-step" ("created_at", "updated_at", "assessment", "date", "course_id") VALUES ('2023-02-23 17:24:07.134501', '2023-02-23 17:24:07.134517', 0, '2023-02-20', 2);
INSERT INTO "public"."course-step" ("created_at", "updated_at", "assessment", "date", "course_id") VALUES ('2023-02-23 17:24:07.135642', '2023-02-23 17:24:07.135659', 3, '2023-02-20', 3);
INSERT INTO "public"."course-step" ("created_at", "updated_at", "assessment", "date", "course_id") VALUES ('2023-02-23 17:24:27.917918', '2023-02-23 17:24:27.917936', 0, '2023-02-19', 3);
INSERT INTO "public"."course-step" ("created_at", "updated_at", "assessment", "date", "course_id") VALUES ('2023-02-23 17:24:27.919354', '2023-02-23 17:24:27.919381', 3, '2023-02-19', 2);
INSERT INTO "public"."course-step" ("created_at", "updated_at", "assessment", "date", "course_id") VALUES ('2023-02-23 17:24:27.921046', '2023-02-23 17:24:27.921072', 3, '2023-02-19', 1);
INSERT INTO "public"."course-step" ("created_at", "updated_at", "assessment", "date", "course_id") VALUES ('2023-02-23 17:24:34.401535', '2023-02-23 17:24:34.401551', 0, '2023-02-18', 3);
INSERT INTO "public"."course-step" ("created_at", "updated_at", "assessment", "date", "course_id") VALUES ('2023-02-23 17:24:34.402907', '2023-02-23 17:24:34.402923', 3, '2023-02-18', 2);
INSERT INTO "public"."course-step" ("created_at", "updated_at", "assessment", "date", "course_id") VALUES ('2023-02-23 17:24:34.404431', '2023-02-23 17:24:34.404449', 0, '2023-02-18', 1);
INSERT INTO "public"."course-step" ("created_at", "updated_at", "assessment", "date", "course_id") VALUES ('2023-02-23 17:25:26.169406', '2023-02-23 17:25:26.169433', 3, '2023-02-17', 1);
INSERT INTO "public"."course-step" ("created_at", "updated_at", "assessment", "date", "course_id") VALUES ('2023-02-23 17:25:26.171033', '2023-02-23 17:25:26.171052', 0, '2023-02-17', 2);
INSERT INTO "public"."course-step" ("created_at", "updated_at", "assessment", "date", "course_id") VALUES ('2023-02-23 17:25:26.172174', '2023-02-23 17:25:26.172184', 0, '2023-02-17', 3);
INSERT INTO "public"."course-step" ("created_at", "updated_at", "assessment", "date", "course_id") VALUES ('2023-02-23 17:25:31.996852', '2023-02-23 17:25:31.996865', 0, '2023-02-16', 1);
INSERT INTO "public"."course-step" ("created_at", "updated_at", "assessment", "date", "course_id") VALUES ('2023-02-23 17:25:31.997653', '2023-02-23 17:25:31.997660', 0, '2023-02-16', 2);
INSERT INTO "public"."course-step" ("created_at", "updated_at", "assessment", "date", "course_id") VALUES ('2023-02-23 17:25:31.998258', '2023-02-23 17:25:31.998265', 0, '2023-02-16', 3);
