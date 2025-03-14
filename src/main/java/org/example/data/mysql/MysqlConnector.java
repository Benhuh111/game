package org.example.data.mysql;

import java.sql.*;

public class MysqlConnector {

    private static final String URL = "jdbc:mysql://localhost:3306/ready_set_game"; // System.getenv("MYSQL_URL");
    private static final String USER = "root"; // System.getenv("MYSQL_USER");
    private static final String PASSWORD = "root"; // System.getenv("MYSQL_PASSWORD");

    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            createTables(connection);
        }
        return connection;
    }



    private static void createTables(Connection connection) throws SQLException {
        Statement stmt = connection.createStatement();

        stmt.executeUpdate(CreateTableQueries.PLAYERS);
        stmt.executeUpdate(CreateTableQueries.MATCHES);
        stmt.executeUpdate(CreateTableQueries.SCORES);
    }

    public static ResultSet executeSelect(String selectQuery) throws SQLException {
        Connection connection = getConnection();
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(selectQuery);
    }
}
