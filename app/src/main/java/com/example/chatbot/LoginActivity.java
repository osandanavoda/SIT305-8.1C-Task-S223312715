package com.example.chatbot;

import android.view.WindowManager;
import android.widget.Button;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;


public class LoginActivity extends AppCompatActivity {

    private Button goButton;
    private EditText usernameField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        usernameField = findViewById(R.id.usernameField);
        goButton = findViewById(R.id.goButton);

        //auto adjust user message input field when user going to message
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        goButton.setOnClickListener(view -> {
            String username = usernameField.getText().toString().trim();
            if (username.isEmpty()) {
                Toast.makeText(this, "Username cannot be empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra(MainActivity.extraUsersname, username);
            startActivity(intent);
            finish();
        });
    }
}