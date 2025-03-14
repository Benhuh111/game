package org.example.models;

public class Match {
    private int matchId;
    private String gameMode;
    private String startTime;
    private String endTime;

    public Match(String gameMode, String startTime, String endTime) {
        this.gameMode = gameMode;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Match() {

    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return String.format("Match ID: %d | Game Mode: %s | Start: %s | End: %s",
                matchId, gameMode, startTime, endTime != null ? endTime : "Ongoing");
    }
}
