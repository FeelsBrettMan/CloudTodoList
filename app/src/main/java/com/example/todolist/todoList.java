package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class todoList extends AppCompatActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        recyclerView = findViewById(R.id.todoListRecyclerView);
        String[] testStrings = getResources().getStringArray(R.array.test_list);
        int[] testTypedArray = getResources().getIntArray(R.array.isChecked);
        boolean[] testParse =  new boolean[testTypedArray.length];
        for(int i =0; i<testTypedArray.length;i++) {
            testParse[i] = (testTypedArray[i] != 0);
        }
        todoListAdapter todoListAdapter = new todoListAdapter( this, testStrings,testParse);
        recyclerView.setAdapter(todoListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}