INSERT INTO users (username, password_hash, role, full_name) VALUES
('admin', 'adminPass', 'ADMIN', 'Администратор'),
('user1', 'user1Pass', 'USER', 'Обычный Пользователь'),
('alex', 'alexPass', 'MEMBER', 'Алексей Иванов'),
('maria', 'mariaPass', 'MEMBER', 'Мария Петрова'),
('sergey', 'sergeyPass', 'MEMBER', 'Сергей Смирнов'),
('olga', 'olgaPass', 'MEMBER', 'Ольга Гащенко'),
('dmitry', 'dmitryPass', 'MEMBER', 'Дмитрий Новиков'),
('ekaterina', 'ekaterinaPass', 'MEMBER', 'Екатерина Морозова')
ON CONFLICT (username) DO NOTHING;

INSERT INTO tournaments (name, registration_deadline, status) VALUES
('Клубный чемпионат Весна 2025', '2024-05-15 23:59:59', 'COMPLETED'),
('Летний кубок 2025', '2025-08-10 23:59:59', 'COMPLETED'),
('Осенний турнир 2025', '2025-10-31 23:59:59', 'UPCOMING'),
('Новогодний турнир 2025', '2025-12-20 23:59:59', 'REGISTRATION_OPEN'),
('Никто не записался на этот турнир :(', '2025-01-01 23:59:59', 'CANCELLED')
ON CONFLICT DO NOTHING;

INSERT INTO tournament_participants (tournament_id, user_id)
SELECT 1, id FROM users
WHERE username IN ('alex', 'maria', 'sergey', 'olga', 'dmitry')
ON CONFLICT (tournament_id, user_id) DO NOTHING;

INSERT INTO tournament_participants (tournament_id, user_id)
SELECT 2, id FROM users
WHERE username IN ('alex', 'maria', 'olga', 'dmitry', 'ekaterina')
ON CONFLICT (tournament_id, user_id) DO NOTHING;

INSERT INTO tournament_participants (tournament_id, user_id)
SELECT 4, id FROM users
WHERE username IN ('alex', 'maria', 'sergey')
ON CONFLICT (tournament_id, user_id) DO NOTHING;

INSERT INTO matches (tournament_id, player1_id, player2_id, player1_score, player2_score, match_date, status)
VALUES
(1,
 (SELECT id FROM users WHERE username = 'alex'),
 (SELECT id FROM users WHERE username = 'maria'),
 2, 0, '2025-05-20 10:00:00', 'COMPLETED'),
(1,
 (SELECT id FROM users WHERE username = 'sergey'),
 (SELECT id FROM users WHERE username = 'olga'),
 2, 1, '2025-05-20 11:30:00', 'COMPLETED'),
(1,
 (SELECT id FROM users WHERE username = 'alex'),
 (SELECT id FROM users WHERE username = 'olga'),
 2, 0, '2025-05-21 10:00:00', 'COMPLETED'),
(1,
 (SELECT id FROM users WHERE username = 'sergey'),
 (SELECT id FROM users WHERE username = 'maria'),
 2, 0, '2025-05-21 11:30:00', 'COMPLETED'),
(1,
 (SELECT id FROM users WHERE username = 'alex'),
 (SELECT id FROM users WHERE username = 'sergey'),
 2, 1, '2025-05-22 15:00:00', 'COMPLETED'),
(1,
 (SELECT id FROM users WHERE username = 'olga'),
 (SELECT id FROM users WHERE username = 'maria'),
 2, 0, '2025-05-22 13:30:00', 'COMPLETED')
ON CONFLICT DO NOTHING;

INSERT INTO matches (tournament_id, player1_id, player2_id, player1_score, player2_score, match_date, status)
VALUES
(2,
 (SELECT id FROM users WHERE username = 'alex'),
 (SELECT id FROM users WHERE username = 'dmitry'),
 2, 0, '2025-08-15 10:00:00', 'COMPLETED'),
(2,
 (SELECT id FROM users WHERE username = 'sergey'),
 (SELECT id FROM users WHERE username = 'ekaterina'),
 2, 1, '2025-08-15 11:30:00', 'COMPLETED'),
(2,
 (SELECT id FROM users WHERE username = 'alex'),
 (SELECT id FROM users WHERE username = 'ekaterina'),
 2, 0, '2025-08-16 10:00:00', 'COMPLETED'),
(2,
 (SELECT id FROM users WHERE username = 'sergey'),
 (SELECT id FROM users WHERE username = 'dmitry'),
 2, 1, '2025-08-16 11:30:00', 'COMPLETED'),
(2,
 (SELECT id FROM users WHERE username = 'sergey'),
 (SELECT id FROM users WHERE username = 'alex'),
 2, 1, '2025-08-17 15:00:00', 'COMPLETED'),
(2,
 (SELECT id FROM users WHERE username = 'dmitry'),
 (SELECT id FROM users WHERE username = 'ekaterina'),
 2, 0, '2025-08-17 13:30:00', 'COMPLETED')
ON CONFLICT DO NOTHING;

INSERT INTO matches (tournament_id, player1_id, player2_id, match_date, status)
VALUES
(4,
 (SELECT id FROM users WHERE username = 'alex'),
 (SELECT id FROM users WHERE username = 'maria'),
 '2025-12-27 10:00:00', 'SCHEDULED'),
(4,
 (SELECT id FROM users WHERE username = 'sergey'),
 (SELECT id FROM users WHERE username = 'alex'),
 '2025-12-28 10:00:00', 'SCHEDULED'),
(4,
 (SELECT id FROM users WHERE username = 'sergey'),
 (SELECT id FROM users WHERE username = 'maria'),
 '2025-12-29 10:00:00', 'SCHEDULED')
ON CONFLICT DO NOTHING;

INSERT INTO favorite_players (user_id, player_id)
VALUES
((SELECT id FROM users WHERE username = 'admin'),
 (SELECT id FROM users WHERE username = 'alex')),
((SELECT id FROM users WHERE username = 'user1'),
 (SELECT id FROM users WHERE username = 'sergey')),
((SELECT id FROM users WHERE username = 'user1'),
 (SELECT id FROM users WHERE username = 'alex'))
ON CONFLICT (user_id, player_id) DO NOTHING;