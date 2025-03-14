package models;

import org.example.models.Match;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MatchTests {

    @Test
    public void testConstructorAndGetters(){
        // Arrange
        var match = new Match("Deathmatch", "2025-02-15 14:00:00", "2025-02-15 14:30:00");

        // Act
        var gameMode = match.getGameMode();
        var startTime = match.getStartTime();
        var endTime = match.getEndTime();

        // Assert
        assertEquals("Deathmatch", gameMode);
        assertEquals("2025-02-15 14:00:00", startTime);
        assertEquals("2025-02-15 14:30:00", endTime);
    }

}
