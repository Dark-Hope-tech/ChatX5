package com.example.chatx5.ModelClass;

public class Messages {
    String message;
    String senderID;
    long time_stamp;

    public Messages() {
    }

    public Messages(String message, String senderID, long time_stamp) {
        this.message = message;
        this.senderID = senderID;
        this.time_stamp = time_stamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public long getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(long time_stamp) {
        this.time_stamp = time_stamp;
    }
}
