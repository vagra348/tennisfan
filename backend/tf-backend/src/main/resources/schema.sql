DROP TABLE IF EXISTS favorite_players;
DROP TABLE IF EXISTS matches;
DROP TABLE IF EXISTS players;
DROP TABLE IF EXISTS tournaments;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(15) UNIQUE NOT NULL,
    password_hash VARCHAR(19) NOT NULL,
    role VARCHAR(10) NOT NULL CHECK (role IN ('USER', 'ADMIN')) DEFAULT 'USER'
);

CREATE TABLE players (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    country VARCHAR(50),
    ranking INT CHECK (ranking > 0 OR ranking IS NULL)
);

CREATE TABLE tournaments (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(100)
);

CREATE TABLE matches (
    id SERIAL PRIMARY KEY,
    tournament_id INT REFERENCES tournaments(id) ON DELETE CASCADE,
    player1_id INT REFERENCES players(id) ON DELETE CASCADE,
    player2_id INT REFERENCES players(id) ON DELETE CASCADE,
    player1_score INT,
    player2_score INT,
    match_date TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('Scheduled', 'Live', 'Completed')),

    CONSTRAINT different_players CHECK (player1_id != player2_id)
);

CREATE TABLE favorite_players (
    user_id INT REFERENCES users(id) ON DELETE CASCADE,
    player_id INT REFERENCES players(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, player_id)
);