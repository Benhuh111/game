package org.example.debug;

import org.example.data.DynamoDb;
import org.example.models.Score;


public class DebugDynamoDb {
    public static void main(String[] args) {
        var dynamoDb = new DynamoDb();
        dynamoDb.emptyTable();

        Score score = new Score();
        score.setPlayerId(1);
        score.setMatchId(1);
        score.setPoints(100);
        score.setCreatedAt("2023-10-01 12:00:00");
        score.setUsername("player1");

        // Save the Score object to DynamoDB
        boolean isSaved = dynamoDb.save(score);
        if (isSaved) {
            System.out.println("Score saved successfully.");
        } else {
            System.out.println("Failed to save score.");
        }
    }
}