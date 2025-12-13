INSERT INTO users (username, password_hash, role) VALUES
('admin', 'adminPass', 'ADMIN'),
('user1', 'user1Pass', 'USER');

INSERT INTO players (full_name, country, ranking) VALUES
('Арина Соболенко', 'Беларусь', 10870),
('Ига Швёнтек', 'Польша', 8395),
('Коко Гауфф', 'США', 6763),
('Аманда Анисимова', 'США', 6287),
('Елена Рыбакина', 'Казахстан', 5850),
('Джессика Пегула', 'США', 5583),
('Мэдисон Киз', 'США', 4335),
('Жасмин Паолини', 'Италия', 4325),
('Мирра Андреева', 'Россия', 4319),
('Екатерина Александрова', 'Россия', 3375);

INSERT INTO tournaments (name, location) VALUES
('Итоговый чемпионат WTA', 'Эр-Рияд'),
('Roland Garros', 'Париж'),
('US Open', 'Нью-Йорк');

INSERT INTO matches (tournament_id, player1_id, player2_id, player1_score, player2_score, match_date, status) VALUES
(1, 1, 4, 2, 1, '2025-11-07 20:45:00', 'Completed'),
(3, 2, 6, 0, 2, '2024-09-05 20:00:00', 'Completed'),
(2, 3, 2, 0, 2, '2024-06-06 20:00:00', 'Completed'),
(1, 5, 1, 2, 0, '2025-11-08 19:15:00', 'Completed');

INSERT INTO matches (tournament_id, player1_id, player2_id, player1_score, player2_score, match_date, status) VALUES
(3, 7, 8, NULL, NULL, '2025-08-28 16:00:00', 'Scheduled'),
(2, 9, 10, NULL, NULL, '2025-05-28 14:00:00', 'Scheduled');

INSERT INTO matches (tournament_id, player1_id, player2_id, player1_score, player2_score, match_date, status) VALUES
(1, 2, 3, NULL, NULL, '2025-12-13 21:00:00', 'Live');

INSERT INTO favorite_players (user_id, player_id) VALUES
(1, 1),
(2, 5);