package services;

import org.example.data.mysql.PlayerDb;
import org.example.models.Player;
import org.example.services.PlayerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;

public class PlayerServiceTests {

    private MockedStatic<PlayerDb> mockedPlayerDb;
    private PlayerService service;

    @BeforeEach
    public void setup() {
        mockedPlayerDb = mockStatic(PlayerDb.class);
        service = new PlayerService();
    }

    @Test
    public void testGetAllPlayers() throws SQLException {
        // Arrange
        var players = List.of(
                new Player("Dora the Explorer", "Dora@jensen.com"),
                new Player("Benny Huang", "Behu@jensen.com")
        );
        mockedPlayerDb.when(PlayerDb::findAll).thenReturn(players);

        // Act
        var result = service.getAllPlayers();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Dora the Explorer", result.getFirst().getUsername());
    }

    @Test
    public void testGetPlayerEmails() throws SQLException {
        // Arrange
        var players = List.of(
                new Player("Dora the Explorer", "Dora@jensen.com"),
                new Player("Benny Huang", "Behu@jensen.com")
        );
        mockedPlayerDb.when(PlayerDb::findAll).thenReturn(players);

        // Act
        var result = service.getPlayerEmails();

        // Assert
        assertEquals("Dora@jensen.com, Behu@jensen.com", result);
    }

    @AfterEach
    public void teardown() {
        mockedPlayerDb.close();
    }
}
