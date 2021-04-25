package com.common;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

public class ConnectionImpl implements Connection, Runnable {
    private Socket socket;
    private ConnectionListener connectionListener;
    private boolean needToRun = true;
    private OutputStream out;

    public ConnectionImpl(Socket socket, ConnectionListener connectionListener) {
        try {
            this.socket = socket;
            this.connectionListener = connectionListener;
            out = socket.getOutputStream();
            Thread t = new Thread(this);
            t.setPriority(Thread.MIN_PRIORITY);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(Message msg) {
        try {
            ObjectOutputStream objOut = new ObjectOutputStream(out);
            objOut.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        needToRun = false;
    }

    @Override
    public void run() {
        while (needToRun) {
            try {
                InputStream in = socket.getInputStream();
                int amount = in.available();
                if (amount != 0) {
                    ObjectInputStream objIn = new ObjectInputStream(in);
                    Message msg = (Message) objIn.readObject();
                    connectionListener.receivedMessage(msg, this);
                } else {
                    Thread.sleep(200);
                }
            } catch (IOException | ClassNotFoundException | InterruptedException | SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
