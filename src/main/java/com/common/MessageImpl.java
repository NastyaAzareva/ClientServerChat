package com.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MessageImpl implements Message {
    private final String nick;
    private final String content;
    private final int type;

    public MessageImpl(String nick, String content, int type) {
        this.nick = nick;
        this.content = content;
        this.type = type;
    }

    @Override
    public String getNick() {
        return nick;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        String date = new SimpleDateFormat("dd.MM.yyyy").format(new Date());

        switch (type) {
            case Message.CLOSE_TYPE:
                return "1";

            case Message.CONTENT_TYPE:
                return date +
                        " " + nick
                        + ": " + content;

            case Message.WELCOME_MESSAGE:
                return content;

            case Message.AUTHORIZATION_REQUEST:
                return content
                        + ", type=" + type;

            case Message.AUTHORIZATION_RESPONSE:
                return content.toUpperCase(Locale.ROOT);

            case Message.LEAVE_MESSAGE:
                return date +
                        " " + nick + content;
            case Message.HISTORY_MESSAGE:
                return content;
        }
        return ""; //never happens
    }
}
