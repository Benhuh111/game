# Ready Set Game - Backend Service

## Table of Contents

1. [Introduction](#1-introduction)
2. [Project Setup](#2-project-setup)
  - [2.1 Requirements](#21-requirements)
  - [2.2 Dependencies](#22-dependencies)
3. [AWS Setup (DynamoDB & S3)](#3-aws-setup-dynamodb--s3)
  - [3.1 NoSQL Database (AWS DynamoDB)](#31-nosql-database-aws-dynamodb)
  - [3.2 AWS S3](#32-aws-s3)
4. [Testing](#4-testing)
  - [4.1 Unit Tests](#41-unit-tests)
  - [4.2 Integration Tests](#42-integration-tests)
5. [Self-Reflection](#5-self-reflection)
6. [License](#6-license)

---

## 1. Introduction

The **Ready Set Game** backend service is designed to manage a competitive gaming platform that tracks players, matches, and scores. It leverages **AWS services** (DynamoDB and S3) and integrates a **MySQL database** for storing game data.

The platform provides functionalities for creating matches, registering players, and storing scores. The backend is designed to be scalable and efficient, with integration of unit and integration tests to ensure reliability.

---

## 2. Project Setup

### 2.1 Requirements

- **Java 11 or higher**: Make sure you have the required version of Java installed.
- **MySQL Database**: Ensure a local or remote MySQL database is set up to store the relational data.
- **AWS Account**: To integrate AWS services like DynamoDB and S3.
- **Maven**: Dependency management and build tool.
- **IDE**: IntelliJ IDEA, Eclipse, or any Java-supported IDE.

### 2.2 Dependencies

The project uses the following dependencies:

- **JUnit 5** for unit testing.
- **Mockito** for mocking database interactions during testing.
- **AWS SDK** for interacting with AWS DynamoDB.
- **JDBC** for MySQL database interaction.

These dependencies are managed with Maven. Ensure your `pom.xml` includes the necessary dependencies:

```xml
<dependencies>
    <!-- JUnit 5 for unit testing -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <version>5.12.0</version>
        <scope>test</scope>
    </dependency>

    <!-- Mockito for mocking in tests -->
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-core</artifactId>
        <version>5.16.0</version>
        <scope>test</scope>
    </dependency>

    <!-- AWS SDK for DynamoDB -->
    <dependency>
        <groupId>software.amazon.awssdk</groupId>
        <artifactId>dynamodb</artifactId>
        <version>2.30.26</version>
    </dependency>

    <!-- MySQL JDBC Driver -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>9.2.0</version>
    </dependency>
</dependencies>
```
## 3. AWS Setup (DynamoDB & S3)

### 3.1 NoSQL Database (AWS DynamoDB)
AWS DynamoDB is utilized for storing game scores and other dynamic data in a flexible, NoSQL format. To set it up:

1. Sign in to AWS Console.
2. Navigate to DynamoDB and create a new table to store game scores.
3. Define the Primary Key as score_id.
4. Adjust capacity settings for provisioned or on-demand usage.
5. Integrate DynamoDB SDK into your project using the AWS SDK.

Sample code for interacting with DynamoDB:
```java
// Example: Saving a Score to DynamoDB
DynamoDbClient dynamoDbClient = DynamoDbClient.create();
PutItemRequest putItemRequest = PutItemRequest.builder()
    .tableName("Scores")
    .item(ImmutableMap.of(
        "score_id", AttributeValue.builder().s("1").build(),
        "player_id", AttributeValue.builder().n("1").build(),
        "match_id", AttributeValue.builder().n("101").build(),
        "points", AttributeValue.builder().n("200").build()))
    .build();
dynamoDbClient.putItem(putItemRequest);
```
## 4. Testing
Testing is crucial for ensuring that the application functions as expected. The project includes unit tests using JUnit 5 and Mockito.

### 4.1 Unit Tests
Testing is crucial for ensuring that the application functions as expected. The project includes unit tests and integration tests using JUnit 5 and Mockito.

#### 4.1.1 Match Model Unit Test

```java
package models;

import org.example.models.Match;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MatchTests {

    @Test
    public void testConstructorAndGetters() {
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
```

#### 4.1.2 Player Model Unit Test
```java
package models;

import org.example.models.Player;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTests {

    @Test
    public void testConstructorAndGetters() {
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
```

## 4.2 Integration Tests
Integration tests check how well different parts of the system work together. These tests typically mock interactions with the database to isolate the service logic.

#### 4.2.1 MatchService Integration Test
```java
package services;

import org.example.data.mysql.MatchDb;
import org.example.models.Match;
import org.example.services.MatchService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;

import java.sql.SQLException;
import java.util.List;

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
        assertEquals("Deathmatch", result.get(0).getGameMode());
    }

    @AfterEach
    public void teardown() {
        mockedMatchDb.close();
    }
}
```
## 5. Self-Reflection
Throughout this project, several challenges were faced, including integrating AWS services (DynamoDB and S3), ensuring proper database interactions, and setting up testing frameworks. The following solutions were adopted:

- **DynamoDB Integration**: I used the AWS SDK to interact with DynamoDB, ensuring that game scores were stored efficiently and in a scalable manner.
- **Testing**: By leveraging JUnit 5 and Mockito, I was able to mock dependencies and isolate components for thorough unit and integration tests.
- **Database Design**: Relational data for players and matches was stored in MySQL, while DynamoDB was used for flexible, high-performance NoSQL storage for game scores.

## 6. License
This project is licensed under the MIT License. See the LICENSE file for more details.