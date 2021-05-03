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


    public void signInClick(View view){
        FireBaseSetUp.getInstance().authenticate(this,this);
        Intent switchActivity = new Intent(MainActivity.this, ListSelect.class);
        MainActivity.this.startActivity(switchActivity);
    }
    public void logIt(View view){
        FireBaseSetUp.getInstance().authenticate(this,this);
    }

    public void newListClick(View view){
        FireBaseSetUp.getInstance().authenticate(this,this);
        FireBaseSetUp.getInstance().createNewList("testListNameFromApp", docID -> {
            Intent switchActivity = new Intent(MainActivity.this, todoList.class);
            MainActivity.this.startActivity(switchActivity);
        });
    }
    public void joinListClick(View view){
        FireBaseSetUp.getInstance().joinList("jz32gfnAMoCqRVomFHfe",docID -> {
            Intent switchActivity = new Intent(MainActivity.this, todoList.class);
            MainActivity.this.startActivity(switchActivity);
        });
    }


    @Override
    public void onAuthenticated(boolean success, String message) {
        Log.d("FBSU-AU", "onAuthenticated: " + success);
    }
}