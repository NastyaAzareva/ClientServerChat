package com.common;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class ChatDatabaseTest {

    @Test
    public void positiveCheckAuthorizationTest() throws SQLException {
        String login = "login";
        String password = "pass";

        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getString("Password")).thenReturn("pass");

        java.sql.Connection jdbcConnection = Mockito.mock(java.sql.Connection.class);

        PreparedStatement statement = Mockito.mock(PreparedStatement.class);
        Mockito.when(statement.executeQuery()).thenReturn(resultSet);
        Mockito.when(jdbcConnection.prepareStatement(anyString())).thenReturn(statement);

        ChatDatabase chatDatabase = new ChatDatabase(jdbcConnection);
        int result = chatDatabase.checkAuthorization(login, password);
        System.out.println("result = " + result);
        assertEquals(result, 1);

    }

    @Test
    public void negativeTestCheckAutorizationTest() throws SQLException {
        String login = "login";
        String password = "pass111";

        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getString("Password")).thenReturn("pass");

        java.sql.Connection jdbcConnection = Mockito.mock(java.sql.Connection.class);

        PreparedStatement statement = Mockito.mock(PreparedStatement.class);
        Mockito.when(statement.executeQuery()).thenReturn(resultSet);
        Mockito.when(jdbcConnection.prepareStatement(anyString())).thenReturn(statement);

        ChatDatabase chatDatabase = new ChatDatabase(jdbcConnection);
        int result = chatDatabase.checkAuthorization(login, password);
        System.out.println("result = " + result);
        assertEquals(result, -1);
    }

    @Test
    public void userShouldBeRegisteredTest() throws SQLException {
        String login = "login";
        String password = "pass111";

        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(resultSet.next()).thenReturn(false);

        java.sql.Connection jdbcConnection = Mockito.mock(java.sql.Connection.class);

        PreparedStatement statement = Mockito.mock(PreparedStatement.class);
        Mockito.when(statement.executeQuery()).thenReturn(resultSet);
        Mockito.when(jdbcConnection.prepareStatement(anyString())).thenReturn(statement);

        ChatDatabase chatDatabase = new ChatDatabase(jdbcConnection);
        int result = chatDatabase.checkAuthorization(login, password);
        System.out.println("result = " + result);
        assertEquals(result, 0);
    }

    @Test
    public void getChatHistoryTest() throws SQLException {
        String timestamp = "23.04.2021";
        String nick = "Ana";
        String content = "Hi!";

        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resultSet.getString("Timestamp")).thenReturn("23.04.2021");
        Mockito.when(resultSet.getString("Nick")).thenReturn("Ana");
        Mockito.when(resultSet.getString("Content")).thenReturn("Hi!");

        java.sql.Connection jdbcConnection = Mockito.mock(java.sql.Connection.class);

        PreparedStatement statement = Mockito.mock(PreparedStatement.class);
        Mockito.when(statement.executeQuery()).thenReturn(resultSet);
        Mockito.when(jdbcConnection.prepareStatement(anyString())).thenReturn(statement);

        ChatDatabase chatDatabase = new ChatDatabase(jdbcConnection);
        String result = chatDatabase.getChatHistory();
        System.out.println("result = " + result);
        assertEquals(result, timestamp + " " + nick + ": " + content + "\n");
    }
}