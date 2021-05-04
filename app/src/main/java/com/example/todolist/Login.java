package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    EditText email, password;
    Button login, createAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = (EditText) findViewById(R.id.email_address);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.log_in);
        createAccount = (Button) findViewById(R.id.create_account);
        login.setOnClickListener(this::signInClick);
        createAccount.setOnClickListener(this::createAccount);
    }

    public void signInClick(View view){
        FireBaseSetUp.getInstance().authenticate(this, (boolean success, String message)->{
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show(); },
                email.getText().toString(), password.getText().toString());
    }
    public  void createAccount(View view){
        Intent switchActivity = new Intent(Login.this, createAccount.class);
        Login.this.startActivity(switchActivity);

    }

}