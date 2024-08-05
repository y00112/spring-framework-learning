package com.zhaoyss.entity;

public class MailMessage {
    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public MailMessage() {
    }

    public MailMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MailMessage{" +
                "message='" + message + '\'' +
                '}';
    }
}
