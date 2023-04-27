package com.shauncore.chatbotinterface;

public class AIMessage {
    private String role;
    private String content;

    public AIMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }
    public AIMessage(String content) {
        this.role = "user";
        this.content = content;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
