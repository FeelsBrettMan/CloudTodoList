package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class todoList extends AppCompatActivity implements FireBaseSetUp.todoCallback {
    RecyclerView recyclerView;
    List<String> itemNames;
    List<Boolean> itemChecks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        recyclerView = findViewById(R.id.todoListRecyclerView);
        itemChecks = new ArrayList<>();
        itemNames = new ArrayList<>();
        TextView listNameView = findViewById(R.id.listName);
        listNameView.setText(FireBaseSetUp.currentListName);

/*        String[] testStrings = getResources().getStringArray(R.array.test_list);
        int[] intArray = getResources().getIntArray(R.array.isChecked);
        for(int i =0; i<intArray.length;i++) {
            itemChecks.add(intArray[i] != 0);
        }
        itemNames.addAll(Arrays.asList(testStrings));*/

        FireBaseSetUp.getInstance().getTodoList(this,this);
    }

    @Override
    public void getElements(String itemName, Boolean isDone) {
        Log.d("TAG", "getElements: " + itemName + " " + isDone);
        itemNames.add(itemName);
        itemChecks.add(isDone);
    }

    public void setRecyclerViewContent(){
        Log.d("TAG", "setRecyclerViewContent: " + itemNames);
        todoListAdapter todoListAdapter = new todoListAdapter( this, itemNames, itemChecks);
        recyclerView.setAdapter(todoListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}