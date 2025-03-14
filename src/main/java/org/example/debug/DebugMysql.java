package org.example.debug;

import org.example.data.mysql.MatchDb;
import org.example.data.mysql.PlayerDb;
import org.example.data.mysql.ScoreDb;

import java.sql.SQLException;

public class DebugMysql {

    public static void main(String[] args) throws SQLException {

        // Show all players
        var players = PlayerDb.findAll();
        System.out.println("======================================");
        System.out.println("           Players List               ");
        System.out.println("======================================");
        players.forEach(System.out::println);

        // Show all matches
        var matches = MatchDb.findAll();
        System.out.println("======================================");
        System.out.println("           Matches List               ");
        System.out.println("======================================");
        matches.forEach(System.out::println);

        // Show all scores
        var scores = ScoreDb.getTopPlayersByTotalPoints();
        System.out.println("======================================");
        System.out.println("           Scores List                ");
        System.out.println("======================================");
        scores.forEach(System.out::println);
    }
}
