package com.shauncore.chatbotinterface;

import java.util.ArrayList;

public class AIRequest {
    private double temperature;
    private String model = "gpt-3.5-turbo";

    private ArrayList<AIMessage> messages;
    // Add other necessary parameters

    // Generate constructor, getters, and setters

    public AIRequest(String prompt, double temperature) {
        this.temperature = temperature;
        this.messages = new ArrayList<>();
        this.messages.add(new AIMessage(prompt));
    }
    public void appendToPrompt(AIResponseChoices choice) {
        this.messages.add(choice.getMessage());
    }
    public void appendToPrompt(AIMessage message) {
        this.messages.add(message);
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

}