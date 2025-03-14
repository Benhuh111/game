package org.example.services;

import org.example.data.mysql.PlayerDb;
import org.example.models.Player;

import java.sql.SQLException;
import java.util.List;

public class PlayerService {

    public void addPlayer(String username, String email) throws SQLException {
        Player newPlayer = new Player();
        newPlayer.setUsername(username);
        newPlayer.setEmail(email);
        PlayerDb.insert(newPlayer);
    }
    public List<Player> getAllPlayers() throws SQLException {
        return PlayerDb.findAll();
    }

    public String getPlayerEmails() throws SQLException {
        List<Player> players = PlayerDb.findAll();
        return getEmailsString(players);
    }

    private static String getEmailsString(List<Player> players) {
        StringBuilder emails = new StringBuilder();

        for (Player player : players) {
            emails.append(player.getEmail()).append(", ");
        }

        return emails.substring(0, emails.length() - 2);
    }

}
