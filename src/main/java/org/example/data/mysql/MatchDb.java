package org.example.data.mysql;

import org.example.models.Match;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MatchDb {

    public static boolean insert(Match match) throws SQLException {
        String query = "INSERT INTO matches (game_mode, start_time, end_time) VALUES (?, ?, ?)";

        try (Connection conn = MysqlConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, match.getGameMode());
            ps.setTimestamp(2, convertStringToTimestamp(match.getStartTime()));
            ps.setTimestamp(3, match.getEndTime() != null ? convertStringToTimestamp(match.getEndTime()) : null);

            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0;
        }
    }

    public static List<Match> findAll() throws SQLException {
        String query = "SELECT * FROM matches";
        List<Match> matches = new ArrayList<>();

        try (Connection conn = MysqlConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Match match = new Match();
                match.setMatchId(rs.getInt("match_id"));
                match.setGameMode(rs.getString("game_mode"));
                match.setStartTime(convertTimestampToString(rs.getTimestamp("start_time")));
                match.setEndTime(rs.getTimestamp("end_time") != null ? convertTimestampToString(rs.getTimestamp("end_time")) : null);

                matches.add(match);
            }
        }
        return matches;
    }

    private static Timestamp convertStringToTimestamp(String dateTime) {
        return Timestamp.valueOf(dateTime);
    }


    private static String convertTimestampToString(Timestamp timestamp) {
        return timestamp.toString();
    }

    public static boolean endMatch(int matchId) throws SQLException {
        String query = "UPDATE matches SET end_time = ? WHERE match_id = ?";

        try (Connection conn = MysqlConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            String endTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            ps.setTimestamp(1, Timestamp.valueOf(endTime));
            ps.setInt(2, matchId);

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        }
    }
}

