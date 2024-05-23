package com.example.chatbot;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.EditText;
import android.view.WindowManager;
import android.widget.ImageButton;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsCompat;


import com.example.chatbot.API.RetrofitClient;
import com.example.chatbot.API.models.ResponsePost;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import retrofit2.Call;
import retrofit2.Callback;


public class MainActivity extends AppCompatActivity {

    private EditText userMessage;
    private ImageButton sendButton;
    public static final String extraUsersname = "extrausersname";
    private RecyclerView recyclerView;

    private JsonArray chatHistory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        chatHistory = new JsonArray();;
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra(extraUsersname)) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
            return;
        }




        userMessage = findViewById(R.id.userMessageInput);

        sendButton = findViewById(R.id.sendButtonInput);

        //auto adjust user message input field when user going to message
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        recyclerView = findViewById(R.id.recyclerView);

        ArrayList<ChatMessage> messages = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        ChatMessageAdapter adapter = new ChatMessageAdapter(this, messages);

        recyclerView.setAdapter(adapter);

        String username = intent.getStringExtra(extraUsersname);

        userMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                    sendButton.performClick();
                    sendButton.requestFocus();
                    return true;
                }
                return false;
            }
        });


        sendButton.setOnClickListener(view -> {
            if (messages.size() > 0) {
                ChatMessage lastMessage = messages.get(messages.size() - 1);
                if (lastMessage.getAuthor() == ChatMessage.AUTHOR_TYPE.userauthorType) {
                    return;
                }
            }

            String message = userMessage.getText().toString();
            if (message.isEmpty()) {
                return;
            }


            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            ChatMessage usersMessage = new ChatMessage(message, ChatMessage.AUTHOR_TYPE.userauthorType);

            messages.add(usersMessage);

            recyclerView.scrollToPosition(messages.size() - 1);

            adapter.notifyItemInserted(messages.size() - 1);

            System.out.println("placing users message");

            userMessage.setText("");

            userMessage.clearFocus();

            userMessage.clearComposingText();

            userMessage.setEnabled(false);




            ChatMessage placeholderMessage = new ChatMessage("", ChatMessage.AUTHOR_TYPE.aiauthorType);
            messages.add(placeholderMessage);

            adapter.notifyItemInserted(messages.size() - 1);

            recyclerView.scrollToPosition(messages.size() - 1);




            try {
                Call<ResponsePost> call = null;
                try {
                    call = RetrofitClient.getInstance()
                            .getAPI().getChatResponse(new MessageRequest(message, ""));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                call.enqueue(new Callback<ResponsePost>() {
                    @Override
                    public void onResponse(Call<ResponsePost> call, Response<ResponsePost> response) {
                        if (!response.isSuccessful()) {
                            System.out.println("Error!");
                            return;
                        }



                        String messageResponse = response.body().message;

                        ChatMessage newMessage = messages.get(messages.size() - 1);

                        newMessage.setMessage(messageResponse);

                        recyclerView.scrollToPosition(messages.size() - 1);

                        adapter.notifyItemChanged(messages.size() - 1);

                        userMessage.setEnabled(true);

                        System.out.println("AI response" + messageResponse);
                    }

                    @Override
                    public void onFailure(Call<ResponsePost> call, Throwable throwable) {
                        System.out.println("Error getting on ai response" + throwable.getMessage());
                    }
                });


            } catch (Exception exception) {
                Log.e("Ex", exception.getMessage());
            }
        });
    }
}