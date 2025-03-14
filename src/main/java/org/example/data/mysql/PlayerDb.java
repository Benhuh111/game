package org.example.data.mysql;

import org.example.models.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlayerDb {

    public static boolean insert(Player player) throws SQLException {
        String query = "INSERT INTO players (username, email, created_at) VALUES (?, ?, NOW())";

        try (Connection conn = MysqlConnector.getConnection();
        PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, player.getUsername());
            ps.setString(2, player.getEmail());

            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0;
        }
    }

    public static List<Player> findAll() throws SQLException {
        String query = "SELECT * FROM players";
        List<Player> players = new ArrayList<>();

        try (Connection conn = MysqlConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Player player = new Player();
                player.setPlayerId(rs.getInt("player_id"));
                player.setUsername(rs.getString("username"));
                player.setEmail(rs.getString("email"));
                player.setCreatedAt(rs.getString("created_at"));

                players.add(player);
            }
        }
        return players;
    }


    public static boolean updatePlayerEmail(int playerId, String newEmail) throws SQLException {
        String query = "UPDATE players SET email = ? WHERE player_id = ?";
        try (PreparedStatement ps = MysqlConnector.getConnection().prepareStatement(query)) {
            ps.setString(1, newEmail);
            ps.setInt(2, playerId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public static boolean deletePlayer(int playerId) throws SQLException {
        String query = "DELETE FROM players WHERE player_id = ?";
        try(PreparedStatement ps = MysqlConnector.getConnection().prepareStatement(query)) {
            ps.setInt(1, playerId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
