package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class createAccount extends AppCompatActivity {
    EditText email, password;
    Button login, createAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        email = findViewById(R.id.create_email);
        password = findViewById(R.id.create_password);
        login = findViewById(R.id.sign_in);
        createAccount = findViewById(R.id.create_button);
        createAccount.setOnClickListener(this::createAccountClick);
    }

    public void createAccountClick(View view){
        FireBaseSetUp.getInstance().createNewAccount(this, (boolean success, String message)->{}, email.getText().toString(), password.getText().toString());
    }
    public void signInClick(View view){
        Intent switchActivity = new Intent(createAccount.this, Login.class);
        createAccount.this.startActivity(switchActivity);
    }
}