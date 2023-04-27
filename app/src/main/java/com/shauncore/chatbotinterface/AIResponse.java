package com.shauncore.chatbotinterface;


import java.util.ArrayList;

public class AIResponse {
    private String id;
    private ArrayList<AIResponseChoices> choices;
    // Add other necessary parameters

    // Generate constructor, getters, and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<AIResponseChoices> getChoices() {
        return choices;
    }

    public void setChoices(ArrayList<AIResponseChoices> choices) {
        this.choices = choices;
    }
}