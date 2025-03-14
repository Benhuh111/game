package services;

import org.example.data.mysql.MatchDb;
import org.example.models.Match;
import org.example.services.MatchService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;

public class MatchServiceTests {

    private MockedStatic<MatchDb> mockedMatchDb;
    private MatchService service;

    @BeforeEach
    public void setup() {
        mockedMatchDb = mockStatic(MatchDb.class);
        service = new MatchService();
    }

    @Test
    public void testGetAllMatches() throws SQLException {
        // Arrange
        var matches = List.of(
                new Match("Deathmatch", "2024-03-10T15:00:00", "2024-03-10T15:30:00"),
                new Match("Capture the Flag", "2024-03-11T16:00:00", "2024-03-11T16:45:00")
        );
        mockedMatchDb.when(MatchDb::findAll).thenReturn(matches);

        // Act
        var result = service.getAllMatches();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Deathmatch", result.getFirst().getGameMode());
        assertEquals("2024-03-10T15:30:00", result.getFirst().getEndTime());
    }

    @AfterEach
    public void teardown() {
        mockedMatchDb.close();
    }
}
