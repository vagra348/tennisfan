DROP TABLE IF EXISTS favorite_players CASCADE;
DROP TABLE IF EXISTS tournament_participants CASCADE;
DROP TABLE IF EXISTS matches CASCADE;
DROP TABLE IF EXISTS tournaments CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(15) UNIQUE NOT NULL,
    password_hash VARCHAR(19) NOT NULL,
    role VARCHAR(10) NOT NULL CHECK (role IN ('USER', 'MEMBER', 'ADMIN')) DEFAULT 'USER',
    full_name VARCHAR(100) NOT NULL
);

CREATE TABLE tournaments (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    registration_deadline TIMESTAMP,
    status VARCHAR(20) NOT NULL CHECK (status IN ('UPCOMING', 'REGISTRATION_OPEN', 'ONGOING', 'COMPLETED', 'CANCELLED')) DEFAULT 'UPCOMING'
);

CREATE TABLE tournament_participants (
    tournament_id INT REFERENCES tournaments(id) ON DELETE CASCADE,
    user_id INT REFERENCES users(id) ON DELETE CASCADE,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (tournament_id, user_id)
);

CREATE TABLE matches (
    id SERIAL PRIMARY KEY,
    tournament_id INT REFERENCES tournaments(id) ON DELETE CASCADE,
    player1_id INT REFERENCES users(id) ON DELETE CASCADE,
    player2_id INT REFERENCES users(id) ON DELETE CASCADE,
    player1_score INT,
    player2_score INT,
    match_date TIMESTAMP,
    status VARCHAR(20) NOT NULL CHECK (status IN ('SCHEDULED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED')) DEFAULT 'SCHEDULED',

    CONSTRAINT different_players CHECK (player1_id != player2_id)
);

CREATE TABLE favorite_players (
    user_id INT REFERENCES users(id) ON DELETE CASCADE,
    player_id INT REFERENCES users(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, player_id)
);