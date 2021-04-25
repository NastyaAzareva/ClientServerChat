package com.common;

import java.sql.Connection;
import java.sql.*;


public class ChatDatabase {
    private Connection dbConnection;

    //for tests
    public ChatDatabase(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public ChatDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
            dbConnection = DriverManager.getConnection("jdbc:sqlite:DBforChat.db");
            System.out.println("successfully connected to DB");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    1 - login exists, password is ok
    0 - login and password created
    -1 - wrong password
     */
    public int checkAuthorization(String login, String password) throws SQLException {
        PreparedStatement stmt = dbConnection.prepareStatement("SELECT * FROM Users WHERE Login = \"" + login + "\"");
        ResultSet rs = null;
        rs = stmt.executeQuery();

        if (!rs.next()) {
            System.out.println("no records");
            userRegistration(login, password);
            return 0;
        }

        if (password.equals(rs.getString("Password")))
            return 1;
        else
            return -1;
    }

    void userRegistration(String login, String password) throws SQLException {
        PreparedStatement stmt = dbConnection.prepareStatement("INSERT INTO Users VALUES (\"" + login + "\", " + "\"" + password + "\"" + ")");
        stmt.executeUpdate();
    }

    public void saveMessagetoDB(String timestamp, String nick, String content) {
        System.out.println("INSERT INTO History(Content, Timestamp, Nick) VALUES (\"" + content + "\", " + "\"" + timestamp + "\", " + "\"" + nick + "\"" + ")");
        try (PreparedStatement stmt = dbConnection.prepareStatement("INSERT INTO History(Content, Timestamp, Nick) VALUES (\"" + content + "\", " + "\"" + timestamp + "\", " + "\"" + nick + "\"" + ")")) {
            stmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public String getChatHistory() throws SQLException {

        PreparedStatement stmt = null;
        try {
            stmt = dbConnection.prepareStatement("select Timestamp, Nick, Content from History order by ID");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (rs != null) {
            String res = "";

            while (rs.next()) {
                res = res + rs.getString("Timestamp") + " " + rs.getString("Nick") + ": " + rs.getString("Content") + "\n";
            }
            System.out.println(res);
            return res;
        } else {
            return "";
        }
    }

    @Override
    protected void finalize() throws Throwable {
        dbConnection.close();
        super.finalize();
    }

}
