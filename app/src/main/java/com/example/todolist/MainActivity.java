package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements FireBaseSetUp.OnAuthenticatedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void signInClick(View view){
        if(FireBaseSetUp.getInstance().isSignedIn()) {
            Intent switchActivity = new Intent(MainActivity.this, ListSelect.class);
            MainActivity.this.startActivity(switchActivity);
        }
        else {
            Intent switchActivity = new Intent(MainActivity.this, Login.class);
            MainActivity.this.startActivity(switchActivity);
        }
    }
    public void logIt(View view) {
        if (FireBaseSetUp.getInstance().isSignedIn()) {
            Toast.makeText(this,"Already signed in!", Toast.LENGTH_SHORT).show();
        }
        else{
            Intent switchActivity = new Intent(MainActivity.this, Login.class);
            MainActivity.this.startActivity(switchActivity);
        }

    }

    public void newListClick(View view){
        if(FireBaseSetUp.getInstance().isSignedIn()){
            FireBaseSetUp.getInstance().createNewList("testListNameFromApp", docID -> {
                        Intent switchActivity = new Intent(MainActivity.this, todoList.class);
                        MainActivity.this.startActivity(switchActivity);
            });
        }
        else {
            Intent switchActivity = new Intent(MainActivity.this, Login.class);
            MainActivity.this.startActivity(switchActivity);
        }
    }
    public void joinListClick(View view){
        if(FireBaseSetUp.getInstance().isSignedIn()){
            FireBaseSetUp.getInstance().joinList("jz32gfnAMoCqRVomFHfe",docID -> {
                Intent switchActivity = new Intent(MainActivity.this, todoList.class);
                MainActivity.this.startActivity(switchActivity);
            });
        }
        else {
            Intent switchActivity = new Intent(MainActivity.this, Login.class);
            MainActivity.this.startActivity(switchActivity);
        }
    }


    @Override
    public void onAuthenticated(boolean success, String message) {
        Log.d("FBSU-AU", "onAuthenticated: " + success);
    }
}