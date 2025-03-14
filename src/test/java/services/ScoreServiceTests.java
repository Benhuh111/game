package services;

import org.example.data.mysql.ScoreDb;
import org.example.models.Score;
import org.example.services.ScoreService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;

public class ScoreServiceTests {

    private MockedStatic<ScoreDb> mockedScoreDb;
    private ScoreService service;

    @BeforeEach
    public void setup() {
        mockedScoreDb = mockStatic(ScoreDb.class);
        service = new ScoreService();
    }

    @Test
    public void testGetScoresForMatch() throws SQLException {
        // Arrange
        var scores = List.of(
                new Score(1, 101, 50, "2024-03-10T15:30:00"),
                new Score(2, 101, 40, "2024-03-10T15:30:00")
        );
        scores.get(0).setUsername("Player1");
        scores.get(1).setUsername("Player2");

        mockedScoreDb.when(() -> ScoreDb.getScoresForMatch(101)).thenReturn(scores);

        // Act
        var result = service.getScoresForMatch(101);

        // Assert
        assertEquals(2, result.size());
        assertEquals(50, result.get(0).getPoints());
        assertEquals("Player1", result.get(0).getUsername());
        assertEquals(40, result.get(1).getPoints());
        assertEquals("Player2", result.get(1).getUsername());
    }

    @Test
    public void testGetMatchHistoryForPlayer() throws SQLException {
        // Arrange
        var scores = List.of(
                new Score(1, 101, 50, "2024-03-10T15:30:00"),
                new Score(1, 102, 40, "2024-03-11T16:00:00")
        );
        scores.get(0).setUsername("Player1");
        scores.get(1).setUsername("Player1");

        mockedScoreDb.when(() -> ScoreDb.getMatchHistoryForPlayer(1)).thenReturn(scores);

        // Act
        var result = service.getMatchHistoryForPlayer(1);

        // Assert
        assertEquals(2, result.size());
        assertEquals(50, result.get(0).getPoints());
        assertEquals("Player1", result.get(0).getUsername());
        assertEquals(40, result.get(1).getPoints());
        assertEquals("Player1", result.get(1).getUsername());
    }

    @Test
    public void testGetTopPlayersByPoints() throws SQLException {
        // Arrange
        var scores = List.of(
                new Score(1, 101, 50, "2024-03-10T15:30:00"),
                new Score(2, 101, 40, "2024-03-10T15:30:00"),
                new Score(3, 102, 30, "2024-03-11T16:00:00")
        );
        scores.get(0).setUsername("Player1");
        scores.get(1).setUsername("Player2");
        scores.get(2).setUsername("Player3");

        mockedScoreDb.when(ScoreDb::getTopPlayersByTotalPoints).thenReturn(scores);

        // Act
        var result = service.getTopPlayersByPoints();

        // Assert
        assertEquals("=== Top Players by Points ===\n" +
                "Player1: 50 points\n" +
                "Player2: 40 points\n" +
                "Player3: 30 points", result);
    }

    @AfterEach
    public void teardown() {
        mockedScoreDb.close();
    }
}
