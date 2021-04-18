package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity implements FireBaseSetUp.OnAuthenticatedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onMyListClick(View view){
        Intent switchActivity = new Intent(MainActivity.this, todoList.class);
        MainActivity.this.startActivity(switchActivity);
    }

    public void signInClick(View view){
        FireBaseSetUp.getInstance().authenticate(this,this);
        Intent switchActivity = new Intent(MainActivity.this, ListSelect.class);
        MainActivity.this.startActivity(switchActivity);
    }
    public void logIt(View view){
        FireBaseSetUp.getInstance().authenticate(this,this);
    }


    @Override
    public void onAuthenticated(boolean success, String message) {
        Log.d("myAUTH", "onAuthenticated: " + success);
    }
}