package com.shauncore.chatbotinterface;

import java.util.ArrayList;

public class AIResponseChoices {
    private int index;
    private AIMessage message;
    private String finish_reason;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public AIMessage getMessage() {
        return message;
    }

    public void setMessage(AIMessage message) {
        this.message = message;
    }

    public String getFinish_reason() {
        return finish_reason;
    }

    public void setFinish_reason(String finish_reason) {
        this.finish_reason = finish_reason;
    }
}
