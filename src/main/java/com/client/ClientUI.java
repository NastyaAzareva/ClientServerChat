package com.client;

import com.common.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ClientUI extends JFrame implements ConnectionListener {
    private JPanel panel;
    private JTextArea textArea;
    private JTextField hostTextField;
    private JButton connectButton;
    private JTextField textSend;
    private JButton sendButton;
    private JTextField loginTextField;
    private JPasswordField passwordField;

    private Connection c;

    public ClientUI() {
        setContentPane(panel);
        setState(false); // false state - chat is not allowed, true state - chat is allowed

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {

                System.out.println("close event");
                connectionClosed(c);
            }
        });

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String host = hostTextField.getText().trim();
                    Socket socket = new Socket(InetAddress.getByName(host), Connection.PORT);
                    ClientUI.this.c = new ConnectionImpl(socket, ClientUI.this);

                    //send auth msg
                    Message msg = new MessageImpl(loginTextField.getText().trim(), passwordField.getText().trim(), Message.AUTHORIZATION_REQUEST);
                    c.send(msg);
                    ClientUI.this.connectionCreated(c);

                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String content = textSend.getText().trim();
                Message msg = null;
                msg = new MessageImpl(loginTextField.getText().trim(), content, Message.CONTENT_TYPE);
                ClientUI.this.c.send(msg);
            }
        });
    }

    @Override
    public void connectionCreated(Connection c) {
        c.send(new MessageImpl(loginTextField.getText(), "", Message.WELCOME_MESSAGE));
        System.out.println("Client connection was created");
        this.c = c;
    }

    @Override
    public void connectionClosed(Connection c) {
        c.send(new MessageImpl(loginTextField.getText(), " left", Message.LEAVE_MESSAGE));
        c.close();
        System.out.println("Client connection was closed");
    }

    @Override
    public void connectionException(Connection c, Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void receivedMessage(Message msg, Connection connection) {

        switch (msg.getType()) {
            case Message.CONTENT_TYPE:

            case Message.LEAVE_MESSAGE:
            case Message.HISTORY_MESSAGE:
                textArea.append("\n");
                textArea.append(msg.toString());
                break;

            case Message.AUTHORIZATION_REQUEST:
                break;

            case Message.AUTHORIZATION_RESPONSE:
                if (msg.getContent().equals("logon and password are ok"))
                    setState(true);
                else
                    setState(false);

                textArea.append("\n");
                textArea.append(msg.toString());
                break;
            case Message.WELCOME_MESSAGE:
                textArea.append("\n");
                textArea.append(msg.getNick() + " has connected");
                break;
        }
    }

    private void setState(boolean isConnected) {
        // host and connect button enable while isConnected=true
        hostTextField.setEnabled(!isConnected);
        connectButton.setEnabled(!isConnected);
        loginTextField.setEnabled(!isConnected);
        passwordField.setEnabled(!isConnected);

        // text and send button disable while isConnected=true
        textSend.setEnabled(isConnected);
        sendButton.setEnabled(isConnected);
        textArea.setEnabled(isConnected);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
                    ClientUI ui = new ClientUI();
                    ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    ui.setSize(800, 400);
                    ui.setVisible(true);
                }
        );
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel = new JPanel();
        panel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(21, 2, new Insets(0, 0, 0, 0), -1, -1));
        textArea = new JTextArea();
        textArea.setText("");
        panel.add(textArea, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 21, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        sendButton = new JButton();
        sendButton.setText("Send");
        panel.add(sendButton, new com.intellij.uiDesigner.core.GridConstraints(10, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        connectButton = new JButton();
        connectButton.setText("Connect / Register");
        panel.add(connectButton, new com.intellij.uiDesigner.core.GridConstraints(8, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        hostTextField = new JTextField();
        hostTextField.setText("127.0.0.1");
        panel.add(hostTextField, new com.intellij.uiDesigner.core.GridConstraints(7, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        passwordField = new JPasswordField();
        passwordField.setText("");
        panel.add(passwordField, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        loginTextField = new JTextField();
        loginTextField.setText("Login");
        panel.add(loginTextField, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        textSend = new JTextField();
        panel.add(textSend, new com.intellij.uiDesigner.core.GridConstraints(9, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }
}
