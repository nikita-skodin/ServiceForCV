-- Вставка в таблицу areas
INSERT INTO areas (title, description)
VALUES ('Area1', 'Description for Area1'),
       ('Area2', 'Description for Area2'),
       ('Area3', 'Description for Area3'),
       ('Area4', 'Description for Area4'),
       ('Area5', 'Description for Area5');

-- Вставка в таблицу tests
INSERT INTO tests (title, description)
VALUES ('Test1', 'Description for Test1'),
       ('Test2', 'Description for Test2'),
       ('Test3', 'Description for Test3'),
       ('Test4', 'Description for Test4'),
       ('Test5', 'Description for Test5');

-- Вставка в таблицу candidates
INSERT INTO candidates (lastname, name, patronymic, description, cv_file, photo)
VALUES ('Lastname1', 'Name1', 'Patronymic1', 'Description for Candidate1', E'\\x0123456789ABCDEF',
        E'\\x0123456789ABCDEF'),
       ('Lastname2', 'Name2', 'Patronymic2', 'Description for Candidate2', E'\\x0123456789ABCDEF',
        E'\\x0123456789ABCDEF'),
       ('Lastname3', 'Name3', 'Patronymic3', 'Description for Candidate3', E'\\x0123456789ABCDEF',
        E'\\x0123456789ABCDEF'),
       ('Lastname4', 'Name4', 'Patronymic4', 'Description for Candidate4', E'\\x0123456789ABCDEF',
        E'\\x0123456789ABCDEF'),
       ('Lastname5', 'Name5', 'Patronymic5', 'Description for Candidate5', E'\\x0123456789ABCDEF',
        E'\\x0123456789ABCDEF');

-- Вставка в таблицу candidates_tests
INSERT INTO candidates_tests (candidate_id, test_id, date_of_passing, score)
VALUES (1, 1, CURRENT_DATE, 90),
       (2, 2, CURRENT_DATE, 85),
       (3, 3, CURRENT_DATE, 75),
       (4, 4, CURRENT_DATE, 80),
       (5, 5, CURRENT_DATE, 95);

-- Вставка в таблицу possible_areas_for_candidates
INSERT INTO possible_areas_for_candidates (area_id, candidate_id)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4),
       (5, 5);

-- Вставка в таблицу compatible_tests_and_areas
INSERT INTO compatible_tests_and_areas (area_id, test_id)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4),
       (5, 5);
