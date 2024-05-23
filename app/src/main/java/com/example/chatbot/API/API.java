package com.example.chatbot.API;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import com.example.chatbot.API.models.ResponsePost;
import com.example.chatbot.MessageRequest;

import org.json.JSONArray;

public interface API {

    @POST("/chat")
    Call<ResponsePost> getChatResponse(@Body MessageRequest messageRequest);
}
