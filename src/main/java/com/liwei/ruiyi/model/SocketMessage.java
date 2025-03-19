package com.liwei.ruiyi.model;

public class SocketMessage {

    public SocketMessage(String type, String message, String time) {
        this.type = type;
        this.message = message;
        this.time = time;
    }

    public static final String TYPE_MESSAGE = "message";
    public static final String TYPE_ANSWER = "answer";
    public static final String TYPE_PING = "ping";
    public static final String TYPE_PONG = "pong";

    private String type;
    private String message;
    private String time;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
