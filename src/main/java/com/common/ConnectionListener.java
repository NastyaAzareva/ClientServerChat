package com.common;

import java.sql.SQLException;

public interface ConnectionListener {
    public void connectionCreated(Connection c);

    public void connectionClosed(Connection c);

    public void connectionException(Connection c, Exception ex);

    public void receivedMessage(Message msg, Connection connection) throws SQLException;
}
