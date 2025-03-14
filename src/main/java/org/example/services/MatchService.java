package org.example.services;

import org.example.data.mysql.MatchDb;
import org.example.models.Match;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MatchService {

    public boolean startMatch(String gameMode) throws SQLException {
        return createAndInsertMatch(gameMode);
    }

    private boolean createAndInsertMatch(String gameMode) throws SQLException {
        String startTime = getCurrentTimestamp();
        Match newMatch = new Match(gameMode, startTime, null);
        boolean success = MatchDb.insert(newMatch);

        System.out.println(success ? "Match started successfully!" : "Error: Could not start match.");
        return success;
    }

    public List<Match> getAllMatches() throws SQLException {
        return MatchDb.findAll();
    }

    public String getMatchSummaries() throws SQLException {
        List<Match> matches = MatchDb.findAll();
        return formatMatchList(matches);
    }

    private static String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private static String formatMatchList(List<Match> matches) {
        StringBuilder result = new StringBuilder("=== Match Summaries ===\n");

        for (Match match : matches) {
            result.append(String.format("Match ID: %d | Game Mode: %s | Start: %s | End: %s%n",
                    match.getMatchId(),
                    match.getGameMode(),
                    match.getStartTime(),
                    (match.getEndTime() == null ? "Ongoing" : match.getEndTime())));
        }

        return result.toString().trim();
    }
}
