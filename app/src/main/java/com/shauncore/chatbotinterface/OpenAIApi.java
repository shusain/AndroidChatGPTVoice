package com.shauncore.chatbotinterface;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OpenAIApi {
    @POST("v1/chat/completions")
    Call<AIResponse> getCompletion(@Body AIRequest request);
}
