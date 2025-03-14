package org.example.data.mysql;

public class CreateTableQueries {
    public final static String PLAYERS = """
            CREATE TABLE IF NOT EXISTS players (
            	player_id INT PRIMARY KEY AUTO_INCREMENT,
                username VARCHAR(255) UNIQUE NOT NULL,
                email VARCHAR(255) UNIQUE NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            );
            """;

    public final static String MATCHES = """
            CREATE TABLE IF NOT EXISTS matches (
            	match_id INT PRIMARY KEY AUTO_INCREMENT,
                game_mode VARCHAR(255) NOT NULL,
                start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                end_time TIMESTAMP
            );
            """;

    public final static String SCORES = """
            CREATE TABLE IF NOT EXISTS scores (
            	score_id INT PRIMARY KEY AUTO_INCREMENT,
                player_id INT NOT NULL,
                match_id INT NOT NULL,
                points INT NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (player_id) REFERENCES players(player_id),
                FOREIGN KEY (match_id) REFERENCES matches(match_id)
            );
            """;
}
