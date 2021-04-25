package com.common;

public interface Connection {
    public static final int PORT = 3333;

    public void send(Message msg);

    public void close();
}
