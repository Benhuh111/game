package org.example.data;

import org.example.models.Score;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;

public class DynamoDb {

    private final static String TABLE_NAME = "Scores";
    private final static String KEY_ATTRIBUTE_NAME = "ScoreId";

    private static DynamoDbClient dynamoDbClient = null;

    public DynamoDb() {
        try {
            createTable();
        } catch (ResourceInUseException e) {
            System.out.println("Table exists: " + TABLE_NAME);
        }
    }

    private static DynamoDbClient getClient() {
        if (dynamoDbClient == null) {
            dynamoDbClient = DynamoDbClient.builder()
                    .region(Region.EU_NORTH_1)
                    .build();
        }
        return dynamoDbClient;
    }

    private void createTable() {
        CreateTableRequest request = CreateTableRequest.builder()
                .attributeDefinitions(
                        AttributeDefinition.builder()
                                .attributeName(KEY_ATTRIBUTE_NAME)
                                .attributeType(ScalarAttributeType.S)
                                .build()
                )
                .keySchema(
                        KeySchemaElement.builder()
                                .attributeName(KEY_ATTRIBUTE_NAME)
                                .keyType(KeyType.HASH)  // Primary Key
                                .build()
                )
                .provisionedThroughput(
                        ProvisionedThroughput.builder()
                                .readCapacityUnits(5L)
                                .writeCapacityUnits(5L)
                                .build()
                )
                .tableName(TABLE_NAME)
                .build();

        getClient().createTable(request);

        System.out.println("Created table: " + TABLE_NAME);
    }

    /**
     * Saves a Score object to DynamoDB
     * @param score The Score object to be saved
     * @return True if save successful
     */
    public boolean save(Score score) {
        HashMap<String, AttributeValue> scoreMap = new HashMap<>();

        String scoreId = UUID.randomUUID().toString();
        scoreMap.put(KEY_ATTRIBUTE_NAME, AttributeValue.builder().s(scoreId).build());

        scoreMap.put("PlayerId", AttributeValue.builder().s(String.valueOf(score.getPlayerId())).build());
        scoreMap.put("MatchId", AttributeValue.builder().s(String.valueOf(score.getMatchId())).build());
        scoreMap.put("Points", AttributeValue.builder().n(String.valueOf(score.getPoints())).build());
        scoreMap.put("CreatedAt", AttributeValue.builder().s(score.getCreatedAt()).build());
        scoreMap.put("Username", AttributeValue.builder().s(score.getUsername()).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .item(scoreMap)
                .build();

        try {
            getClient().putItem(request);
            return true;
        } catch (AwsServiceException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Score> findAll() {
        ScanRequest scanRequest = ScanRequest.builder()
                .tableName(TABLE_NAME)
                .build();

        ScanResponse response = getClient().scan(scanRequest);

        List<Score> scoreList = new ArrayList<>();
        for (Map<String, AttributeValue> item : response.items()) {

            Score score = new Score();
            score.setScoreId(item.get(KEY_ATTRIBUTE_NAME).s());
            score.setPlayerId(Integer.parseInt(item.get("PlayerId").s()));
            score.setMatchId(Integer.parseInt(item.get("MatchId").s()));
            score.setPoints(Integer.parseInt(item.get("Points").n()));
            score.setCreatedAt(item.get("CreatedAt").s());
            score.setUsername(item.get("Username").s());

            scoreList.add(score);
        }

        return scoreList;
    }

    public void emptyTable() {
        List<Score> scoreList = findAll();

        scoreList.forEach(score -> delete(String.valueOf(score.getScoreId())));
    }

    public void delete(String scoreId) {
        DeleteItemRequest request = DeleteItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(Map.of(KEY_ATTRIBUTE_NAME, AttributeValue.fromS(scoreId)))
                .build();

        getClient().deleteItem(request);
    }
}