package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements FireBaseSetUp.OnAuthenticatedListener, JoinListDialog.ButtonClickListener, NewListDialog.ButtonClickListener {


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


    public void newListClick(View view){
        if(FireBaseSetUp.getInstance().isSignedIn()){
            NewListDialog newListDialog = new NewListDialog();
            newListDialog.show(getSupportFragmentManager(), "newListDialog");
        }
        else {
            Intent switchActivity = new Intent(MainActivity.this, Login.class);
            MainActivity.this.startActivity(switchActivity);
        }
    }
    public void joinListClick(View view){
        if(FireBaseSetUp.getInstance().isSignedIn()){
            JoinListDialog joinListDialog = new JoinListDialog();
            joinListDialog.show(getSupportFragmentManager(),"joinListDialog");
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

    @Override
    public void onButtonClick(boolean button, String listID) {
        if(button){
            FireBaseSetUp.getInstance().joinList(listID, docID -> {
                Intent switchActivity = new Intent(MainActivity.this, todoList.class);
                MainActivity.this.startActivity(switchActivity);
            });
        }
        else{
            Log.e("MAIN", "onButtonClick: Failed to join list");
        }
    }

    @Override
    public void onNewButtonClick(boolean button, String listName) {
        if(button){
            FireBaseSetUp.getInstance().createNewList(listName, docID -> {
                Intent switchActivity = new Intent(MainActivity.this, todoList.class);
                MainActivity.this.startActivity(switchActivity);
            });
        }
        else{
            Log.e("MAIN", "onButtonClick: Failed to join list");
        }
    }
}