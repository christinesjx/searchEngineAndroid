package com.example.searchengine.Model;

public class Message {

    private Integer success;
    private String errorMessage;
    private String message;
    private boolean isSelf;
    private Enum mode;


    public Enum getMode() { return mode; }

    public void setMode(Enum mode) { this.mode = mode; }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public void setSelf(boolean self) {
        isSelf = self;
    }
}
