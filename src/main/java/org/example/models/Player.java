package org.example.models;

public class Player {
    private int playerId;
    private String username;
    private String email;
    private String createdAt;

    public Player(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public Player() {

    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return String.format("Player ID: %d | Username: %s | Email: %s | Created At: %s",
                playerId, username, email, createdAt);
    }
}
