package org.example.models;

public class Score {
    private String scoreId;
    private int playerId;
    private int matchId;
    private int points;
    private String createdAt;
    private String username;

    public Score(int playerId, int matchId, int points, String createdAt) {
        this.playerId = playerId;
        this.matchId = matchId;
        this.points = points;
        this.createdAt = createdAt;
    }

    public Score() {

    }

    public String getScoreId() {
        return scoreId;
    }

    public void setScoreId(String scoreId) {
        this.scoreId = scoreId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return String.format("Score ID: %s | Player ID: %d | Match ID: %d | Points: %d | Created At: %s",
                scoreId, playerId, matchId, points, createdAt);
    }
}