package com.common;

import java.io.Serializable;

public interface Message extends Serializable {
    public int CLOSE_TYPE = 0;
    public int CONTENT_TYPE = 1;
    public int AUTHORIZATION_REQUEST = 2;
    public int AUTHORIZATION_RESPONSE = 3;
    public int WELCOME_MESSAGE = 4;
    public int LEAVE_MESSAGE = 5;
    public int HISTORY_MESSAGE = 6;

    public String getNick();

    public String getContent();

    public int getType();
}
