package org.example.services;

import org.example.data.mysql.ScoreDb;
import org.example.models.Score;

import java.sql.SQLException;
import java.util.List;

public class ScoreService {

    public List<Score> getScoresForMatch(int matchId) throws SQLException {
        return ScoreDb.getScoresForMatch(matchId);
    }

    public List<Score> getMatchHistoryForPlayer(int playerId) throws SQLException {
        return ScoreDb.getMatchHistoryForPlayer(playerId);
    }

    public String getTopPlayersByPoints() throws SQLException {
        List<Score> scores = ScoreDb.getTopPlayersByTotalPoints();
        return formatTopPlayers(scores);
    }

    public boolean recordScore(int matchId, int playerId, int points) throws SQLException {
        return ScoreDb.recordScore(matchId, playerId, points);
    }

    private static String formatTopPlayers(List<Score> scores) {
        StringBuilder result = new StringBuilder("=== Top Players by Points ===\n");

        for (Score score : scores) {
            result.append(score.getUsername()).append(": ")
                    .append(score.getPoints()).append(" points\n");
        }

        return result.toString().trim();
    }
}
