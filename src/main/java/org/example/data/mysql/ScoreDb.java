package org.example.data.mysql;

import org.example.models.Score;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ScoreDb {

    public static List<Score> getScoresForMatch(int matchId) throws SQLException {
        String query = "SELECT s.score_id, s.player_id, s.match_id, s.points, s.created_at, p.username FROM scores s " +
                "JOIN players p ON s.player_id = p.player_id WHERE s.match_id = ? ORDER BY s.points DESC";
        return getScores(matchId, query);
    }

    private static List<Score> getScores(int matchId, String query) throws SQLException {
        PreparedStatement ps = MysqlConnector.getConnection().prepareStatement(query);
        ps.setInt(1, matchId);
        ResultSet rs = ps.executeQuery();

        List<Score> scores = new ArrayList<>();

        while (rs.next()) {
            Score score = new Score();
            score.setScoreId(String.valueOf(rs.getInt("score_id")));
            score.setPlayerId(rs.getInt("player_id"));
            score.setMatchId(rs.getInt("match_id"));
            score.setPoints(rs.getInt("points"));
            score.setCreatedAt(rs.getString("created_at"));
            score.setUsername(rs.getString("username"));
            scores.add(score);
        }

        return scores;
    }

    public static List<Score> getMatchHistoryForPlayer(int playerId) throws SQLException {
        String query = "SELECT s.score_id, s.player_id, s.match_id, s.points, s.created_at, p.username FROM scores s " +
                "JOIN players p ON s.player_id = p.player_id WHERE s.player_id = ? ORDER BY s.points DESC";
        return getScores(playerId, query);
    }

    public static List<Score> getTopPlayersByTotalPoints() throws SQLException {
        String query = "SELECT s.score_id, s.player_id, s.match_id, s.points, s.created_at, p.username, SUM(s.points) " +
                "AS total_points " +
                "FROM scores s JOIN players p ON s.player_id = p.player_id " +
                "GROUP BY s.score_id, s.player_id, s.match_id, s.points, s.created_at, p.username " +
                "ORDER BY total_points DESC LIMIT 10";

        ResultSet rs = MysqlConnector.executeSelect(query);

        List<Score> topScores = new ArrayList<>();

        while (rs.next()) {
            Score score = new Score();
            score.setScoreId(String.valueOf(rs.getInt("score_id")));
            score.setPlayerId(rs.getInt("player_id"));
            score.setMatchId(rs.getInt("match_id"));
            score.setPoints(rs.getInt("total_points"));
            score.setCreatedAt(rs.getString("created_at"));
            score.setUsername(rs.getString("username"));
            topScores.add(score);
        }

        return topScores;
    }

    public static boolean recordScore(int matchId, int playerId, int points) throws SQLException {
        String query = "INSERT INTO scores (match_id, player_id, points, created_at) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = MysqlConnector.getConnection().prepareStatement(query);

        ps.setInt(1, matchId);
        ps.setInt(2, playerId);
        ps.setInt(3, points);
        ps.setString(4, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        int rowsAffected = ps.executeUpdate();

        return rowsAffected > 0;
    }
}