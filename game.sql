-- ==============================================
-- DATABASE CREATION
-- ==============================================
CREATE DATABASE IF NOT EXISTS ready_set_game;
USE ready_set_game;

-- ==============================================
-- TABLE DROPS (to avoid conflicts when re-running the script)
-- ==============================================
DROP TABLE IF EXISTS scores;
DROP TABLE IF EXISTS matches;
DROP TABLE IF EXISTS players;

-- ==============================================
-- TABLE CREATION
-- ==============================================

-- Players Table
CREATE TABLE players (
                         player_id INT PRIMARY KEY AUTO_INCREMENT,
                         username VARCHAR(255) UNIQUE NOT NULL,
                         email VARCHAR(255) UNIQUE NOT NULL,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Matches Table
CREATE TABLE matches (
                         match_id INT PRIMARY KEY AUTO_INCREMENT,
                         game_mode VARCHAR(255) NOT NULL,
                         start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         end_time TIMESTAMP
);

-- Scores Table (Many-to-Many relationship between players and matches)
CREATE TABLE scores (
                        score_id INT PRIMARY KEY AUTO_INCREMENT,
                        player_id INT NOT NULL,
                        match_id INT NOT NULL,
                        points INT NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (player_id) REFERENCES players(player_id) ON DELETE CASCADE,
                        FOREIGN KEY (match_id) REFERENCES matches(match_id) ON DELETE CASCADE
);

-- ==============================================
-- DATA INSERTION
-- ==============================================

-- Insert Players
INSERT INTO players (username, email)
VALUES
    ('Dora the Explorer', 'Dora@jensen.com'),
    ('Benny Huang', 'Behu@jensen.com'),
    ('Arnold Schwarzenegger', 'ArSc@jensen.com');

-- Insert Matches
INSERT INTO matches (start_time, end_time, game_mode)
VALUES
    ('2025-02-15 14:00:00', '2025-02-15 14:30:00', 'Deathmatch'),
    ('2025-02-15 15:00:00', '2025-02-15 15:25:00', 'Team Battle'),
    ('2025-02-15 16:00:00', '2025-02-15 16:40:00', 'Capture the Flag'),
    ('2025-02-15 17:00:00', '2025-02-15 17:50:00', 'Survival'),
    ('2025-02-15 18:00:00', '2025-02-15 18:35:00', 'Free for All');

-- Insert Scores
INSERT INTO scores (player_id, match_id, points)
VALUES
    (1, 1, 200), (2, 1, 250), (3, 1, 180),
    (1, 2, 300), (2, 2, 270), (3, 2, 320),
    (1, 3, 150), (2, 3, 200), (3, 3, 100),
    (1, 4, 400), (2, 4, 450), (3, 4, 420),
    (1, 5, 180), (2, 5, 220), (3, 5, 210);

-- ==============================================
-- QUERIES
-- ==============================================

-- View all data
SELECT * FROM players;
SELECT * FROM matches;
SELECT * FROM scores;

-- Get scores for a specific match
SELECT p.username, s.points
FROM scores s
         JOIN players p ON s.player_id = p.player_id
WHERE s.match_id = 1
ORDER BY s.points DESC;

-- Get a player's match history
SELECT m.match_id, m.start_time, m.game_mode, s.points
FROM matches m
         JOIN scores s ON m.match_id = s.match_id
WHERE s.player_id = 1
ORDER BY m.start_time ASC;

-- Get top 10 players by total points
SELECT p.username, SUM(s.points) AS total_points
FROM scores s
         JOIN players p ON s.player_id = p.player_id
GROUP BY s.player_id, p.username
ORDER BY total_points DESC
    LIMIT 10;

-- ==============================================
-- UPDATES
-- ==============================================

-- Update player's username
UPDATE players
SET username = 'Magnus Uggla'
WHERE player_id = 2;

-- Update player's email
UPDATE players
SET email = 'Maug@jensen.com'
WHERE player_id = 2;

-- ==============================================
-- DELETIONS
-- ==============================================

-- Delete specific match
DELETE FROM matches WHERE match_id = 1;

-- Delete specific player and their scores
DELETE FROM players WHERE player_id = 2;
