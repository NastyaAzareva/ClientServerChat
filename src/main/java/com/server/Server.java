package com.server;

import com.common.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

public class Server implements ConnectionListener {
    private Set<Connection> connections;
    private ServerSocket serverSocket;

    private ChatDatabase database;

    public Server() {
        try {
            serverSocket = new ServerSocket(Connection.PORT);
            connections = new LinkedHashSet();
            database = new ChatDatabase();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startServer() {
        System.out.println("Server started");
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                //connections.add(new ConnectionImpl(socket, this));
                connectionCreated(new ConnectionImpl(socket, this));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public synchronized void connectionCreated(Connection c) {
        connections.add(c);
        System.out.println("Connection was added");
    }

    @Override
    public synchronized void connectionClosed(Connection c) {
        connections.remove(c);
        c.close();
        System.out.println("Connection was closed");
    }

    @Override
    public synchronized void connectionException(Connection c, Exception ex) {
        connections.remove(c);
        c.close();
        System.out.println("Connection was closed");
        ex.printStackTrace();
    }

    @Override
    public synchronized void receivedMessage(Message msg, Connection connection) throws SQLException {
        switch (msg.getType()) {
            case Message.CLOSE_TYPE:
                connectionClosed(connection);
                break;

            case Message.CONTENT_TYPE:
                database.saveMessagetoDB(new SimpleDateFormat("dd.MM.yyyy").format(new Date()).toString(), msg.getNick(), msg.getContent());
                for (Connection c : connections) {
                    c.send(msg);
                }
                break;

            case Message.LEAVE_MESSAGE:
                for (Connection c : connections) {
                    c.send(msg);
                }
                break;

            case Message.WELCOME_MESSAGE:
                for (Connection c : connections) {
                    c.send(msg);
                }
                break;

            case Message.AUTHORIZATION_REQUEST:
                if (database.checkAuthorization(msg.getNick(), msg.getContent()) == 0)
                    connection.send(new MessageImpl(msg.getNick(), "you have successfully registered", Message.AUTHORIZATION_RESPONSE));
                if (database.checkAuthorization(msg.getNick(), msg.getContent()) == 1) {
                    connection.send(new MessageImpl(msg.getNick(), "logon and password are ok", Message.AUTHORIZATION_RESPONSE));
                    connection.send(new MessageImpl(msg.getNick(), database.getChatHistory(), Message.HISTORY_MESSAGE));
                } else
                    connection.send(new MessageImpl(msg.getNick(), "logon or password is invalid", Message.AUTHORIZATION_RESPONSE));
                break;

            case Message.AUTHORIZATION_RESPONSE:
                break;
        }


    }
}
