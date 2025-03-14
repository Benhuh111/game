package org.example;

import org.example.data.mysql.MatchDb;
import org.example.data.mysql.PlayerDb;
import org.example.data.mysql.ScoreDb;
import org.example.models.Match;
import org.example.models.Player;
import org.example.models.Score;
import org.example.services.PlayerService;
import org.example.services.ScoreService;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        boolean keepRunning;
        do {
            keepRunning = mainMenu();
        } while (keepRunning);
    }

    private static boolean mainMenu() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        PlayerService playerService = new PlayerService();

        System.out.print("""
            ============================
            ---    Menu              ---
            --- 0. Exit              ---
            --- 1. Show players      ---
            --- 2. Show matches      ---
            --- 3. Show scores       ---
            --- 4. Add Player        ---
            --- 5. Start Match       ---
            --- 6. End Match         ---
            --- 7. Record Score      ---
            ============================
            Input:\s""");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 0:
                return false;
            case 1:
                showPlayers();
                break;
            case 2:
                showMatches();
                break;
            case 3:
                showScores();
                break;
            case 4:
                System.out.println("Enter username: ");
                String username = scanner.nextLine();
                System.out.println("Enter email: ");
                String email = scanner.nextLine();
                playerService.addPlayer(username, email);
                break;
            case 5:
                startMatch();
                break;
            case 6:
                endMatch();
                break;
            case 7:
                recordScore();
                break;
        }

        return true;
    }


    private static void showPlayers() throws SQLException {
        List<Player> players = PlayerDb.findAll();
        players.forEach(player -> System.out.println(player.getUsername()));
    }

    private static void showScores() throws SQLException {
        List<Score> topScores = ScoreDb.getTopPlayersByTotalPoints();

        // Heading
        System.out.println("===============================================");
        System.out.println("            Top 10 Players by Points          ");
        System.out.println("===============================================");
        System.out.printf("%-25s %-15s\n", "Username", "Total Points");
        System.out.println("------------------------------------------------");

        for (Score score : topScores) {
            System.out.printf("%-25s %-15d\n", score.getUsername(), score.getPoints());
        }

        System.out.println("===============================================");
    }

    private static void startMatch() throws SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter game mode: ");
        String gameMode = scanner.nextLine();

        String startTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Match match = new Match(gameMode, startTime, null);

        boolean success = MatchDb.insert(match);

        if (success) {
            System.out.println("Match started successfully!");
        } else {
            System.out.println("Error: Could not start match.");
        }
    }

    private static void displayMatches(boolean showOngoingOnly) throws SQLException {
        List<Match> matches = MatchDb.findAll();

        System.out.println("===============================================");
        System.out.println("                Match List                     ");
        System.out.println("===============================================");
        System.out.printf("%-10s %-20s %-20s %-20s\n", "Match ID", "Game Mode", "Start Time", "End Time");
        System.out.println("------------------------------------------------");

        for (Match match : matches) {
            if (showOngoingOnly && match.getEndTime() == null) {

                System.out.printf("%-10d %-20s %-20s %-20s\n",
                        match.getMatchId(),
                        match.getGameMode(),
                        match.getStartTime(),
                        "Ongoing"
                );
            } else if (!showOngoingOnly) {
                // Show all matches
                System.out.printf("%-10d %-20s %-20s %-20s\n",
                        match.getMatchId(),
                        match.getGameMode(),
                        match.getStartTime(),
                        (match.getEndTime() == null ? "Ongoing" : match.getEndTime())
                );
            }
        }

        System.out.println("===============================================");
    }

    private static void showMatches() throws SQLException {
        displayMatches(false);  // Show all matches
    }

    private static void endMatch() throws SQLException {
        Scanner scanner = new Scanner(System.in);

        displayMatches(true);

        System.out.print("Enter Match ID to end: ");
        int matchId = scanner.nextInt();

        boolean success = MatchDb.endMatch(matchId);

        if (success) {
            System.out.println("Match ended successfully!");
        } else {
            System.out.println("Error: Could not end match.");
        }
    }

    private static void recordScore() throws SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter match ID: ");
        int matchId = scanner.nextInt();

        System.out.print("Enter player ID: ");
        int playerId = scanner.nextInt();

        System.out.print("Enter points: ");
        int points = scanner.nextInt();

        ScoreService scoreService = new ScoreService();
        boolean success = scoreService.recordScore(matchId, playerId, points);

        if (success) {
            System.out.println("Score recorded successfully!");
        } else {
            System.out.println("Error: Could not record score.");
        }
    }
}
