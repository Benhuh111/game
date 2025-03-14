package models;

import org.example.models.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTests {

    @Test
    public void testConstructorAndGetters(){
        // Arrange
        var player = new Player("TEST_PLAYER", "TEST_EMAIL");

        // Act
        var playerName = player.getUsername();
        var playerEmail = player.getEmail();

        // Assert
        assertEquals("TEST_PLAYER", playerName);
        assertEquals("TEST_EMAIL", playerEmail);

    }
}
