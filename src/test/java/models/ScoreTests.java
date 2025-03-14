package models;

import org.example.models.Score;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScoreTests {

    @Test
    public void testConstructorAndGetters(){
        // Arrange
        var score = new Score(1, 1, 200, "2025-02-15 14:10:00");

        // Act
        var playerId = score.getPlayerId();
        var matchId = score.getMatchId();
        var points = score.getPoints();
        var createdAt = score.getCreatedAt();

        // Assert
        assertEquals(1, playerId);
        assertEquals(1, matchId);
        assertEquals(200, points);
        assertEquals("2025-02-15 14:10:00", createdAt);

    }
}
