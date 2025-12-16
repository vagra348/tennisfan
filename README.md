# TennisFan

--------
Проект для дисциплин “Введение в системы баз данных” и “Технологии Интернет и WEB-программирования”. Проект является
индивидуальным.

Для запуска проекта необходимо:

- установить vue, npm, а затем зависимости проектa tf-frontend с помощью команды `npm install`
- развернуть базу данных PostgreSQL (данные вводить не нужно - накатываются автоматически при запуске приложения)
- установить Java и зависимости проекта tf-backend из pom.xml
- запустить приложение tf-backend `mvn spring-boot:run`
- запустить приложение tf-frontend `npm install`  и `npm run dev`
- для просмотра документации в Swagger запустить tf-backend и открыть http://localhost:8081/swagger-ui/index.html#/

По умолчанию приложение будет запущено на порте `8081`, фронт - на порте `5173`

--------

# 1. Описание

## Наименование проекта: "TennisFan".

### Предметная область:

Сервис предназначен для теннисных фанатов. Обеспечивает ручную агрегацию информации о теннисистах, турнирах и матчах.
Пользователи могут отслеживать своих любимых игроков и получать персонализированную ленту матчей.

В качестве примера используются некоторые последние (на 17.12.2025) данные о мировых турнирных матчах и теннисистах, но
может найти применение в небольших теннисных клубах для хранения информации о клубных игроках.

--------

# 2. Данные

![Image](schema.jpg)

### Таблица users:

| Ключ            | Атрибуты                                                              |
|-----------------|-----------------------------------------------------------------------|
| `id`            | SERIAL PRIMARY KEY                                                    |
| `username`      | VARCHAR(15) UNIQUE NOT NULL                                           |
| `password_hash` | VARCHAR(19) NOT NULL                                                  |
| `role`          | VARCHAR(10) NOT NULL CHECK (role IN ('USER', 'ADMIN')) DEFAULT 'USER' |

### Таблица players:

| Ключ        | Атрибуты                                   |
|-------------|--------------------------------------------|
| `id`        | SERIAL PRIMARY KEY                         |
| `full_name` | VARCHAR(100) NOT NULL                      |
| `country`   | VARCHAR(50)                                |
| `ranking`   | INT CHECK (ranking > 0 OR ranking IS NULL) |

### Таблица tournaments:

| Ключ       | Атрибуты              |
|------------|-----------------------|
| `id`       | SERIAL PRIMARY KEY    |
| `name`     | VARCHAR(100) NOT NULL |
| `location` | VARCHAR(100)          |

### Таблица matches:

| Ключ            | Атрибуты                                                                  |
|-----------------|---------------------------------------------------------------------------|
| `id`            | SERIAL PRIMARY KEY                                                        |
| `tournament_id` | INT REFERENCES tournaments(id) ON DELETE CASCADE                          |
| `player1_id`    | INT REFERENCES players(id) ON DELETE CASCADE                              |
| `player2_id`    | INT REFERENCES players(id) ON DELETE CASCADE                              |
| `player1_score` | INT                                                                       |
| `player2_score` | INT                                                                       |
| `match_date`    | TIMESTAMP NOT NULL                                                        |
| `status`        | VARCHAR(20) NOT NULL CHECK (status IN ('Scheduled', 'Live', 'Completed')) |
|                 | CONSTRAINT different_players CHECK (player1_id != player2_id)             |

### Таблица favorite_players:

| Ключ        | Атрибуты                                     |
|-------------|----------------------------------------------|
| `user_id`   | INT REFERENCES users(id) ON DELETE CASCADE   |
| `player_id` | INT REFERENCES players(id) ON DELETE CASCADE |
|             | PRIMARY KEY (user_id, player_id)             |

## Общие ограничения целостности:

-player1_id и player2_id в таблице matches не могут ссылаться на одного и того же игрока  
-Ранг игрока должен быть положительным числом или NULL  
-Уникальность пары user_id и player_id в таблице favorite_players

--------

# 3. Пользовательские роли

## USER (Пользователь)

#### Ответственность:

Регистрация, аутентификация, просмотр данных, управление своим списком избранных игроков

#### Количество:

Неограниченно (~95-99% от общей массы пользователей)

## ADMIN (Администратор)

#### Ответственность:

Все права USER + полное управление данными (CRUD для игроков и матчей), назначение других пользователей
администраторами и удаление пользователей

#### Количество:

Минимально (регулируется пользователями с такой же ролью)

--------

# 4. UI / API

### UI:

Веб-приложение со страницами: Лента матчей, Список игроков, Админ-панель.

### Backend:

REST API. Полностью - на http://localhost:8081/swagger-ui/index.html#/ (см. начало документа)

| Контроллер                                              | Эндпоинты                                                                                                                                                                                                                                                                                                                                                                            |
|---------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| main-controller <br> (для проверки доступности сервера) | GET /                                                                                                                                                                                                                                                                                                                                                                                |
| admin-controller                                        | PUT /api/admin/users/{userId}/role <br> PUT /api/admin/players/{playerId} <br> DELETE /api/admin/players/{playerId} <br> PUT /api/admin/matches/{matchId} <br> DELETE /api/admin/matches/{matchId} <br> GET /api/admin/players <br> POST /api/admin/players <br> GET /api/admin/matches <br> POST /api/admin/matches <br> GET /api/admin/users <br> DELETE /api/admin/users/{userId} |
| favorite-controller                                     | POST /api/favorites/players/{playerId} <br> DELETE /api/favorites/players/{playerId}                                                                                                                                                                                                                                                                                                 |
| auth-controller                                         | POST /api/auth/register <br> POST /api/auth/login                                                                                                                                                                                                                                                                                                                                    |
| tournament-controller                                   | GET /api/tournaments                                                                                                                                                                                                                                                                                                                                                                 |
| player-controller                                       | GET /api/players                                                                                                                                                                                                                                                                                                                                                                     |
| match-controller                                        | GET /api/matches <br> GET /api/matches/favorites                                                                                                                                                                                                                                                                                                                                     |

--------

# 5. Технологии разработки

#### Фронтенд: `Vue 3`

#### Бэкенд: `Java 21` + `Spring Boot`

#### СУБД: `PostgreSQL`

--------
