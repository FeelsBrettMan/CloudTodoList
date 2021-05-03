package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class todoList extends AppCompatActivity implements FireBaseSetUp.TodoCallback, AddDialog.ButtonClickListener  {
    String TAG = "TODO";
    RecyclerView recyclerView;
    List<String> itemNames;
    List<Boolean> itemChecks;
    List<String> itemIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        recyclerView = findViewById(R.id.todoListRecyclerView);
        itemChecks = new ArrayList<>();
        itemNames = new ArrayList<>();
        itemIDs = new ArrayList<>();
        TextView listNameView = findViewById(R.id.listName);
        listNameView.setText(FireBaseSetUp.currentListName);

/*        String[] testStrings = getResources().getStringArray(R.array.test_list);
        int[] intArray = getResources().getIntArray(R.array.isChecked);
        for(int i =0; i<intArray.length;i++) {
            itemChecks.add(intArray[i] != 0);
        }
        itemNames.addAll(Arrays.asList(testStrings));*/

        FireBaseSetUp.getInstance().getTodoList(this,this, docID -> {
            setRecyclerViewContent();
            FireBaseSetUp.getInstance().setCollectionListener(this);
        });
        FloatingActionButton addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(this::addItemClick);
    }

    @Override
    public void getElements(String itemName, Boolean isDone, String itemID) {
        Log.d("TAG", "getElements: " + itemName + " " + isDone);
        itemNames.add(itemName);
        itemChecks.add(isDone);
        itemIDs.add(itemID);
    }

    @Override
    public void getChanged(String itemName, Boolean isDone, String itemID) {
        boolean updated = false;
        boolean newItem = true;
        int position = 0;
        for(int i=0; i<itemIDs.size();i++){
            if(itemIDs.get(i).equals(itemID)){
                newItem = false;
                if(itemChecks.get(i) != isDone || itemNames.get(i).equals(itemName)){
                    updated = true; position = i;
                    itemChecks.set(i, isDone); itemNames.set(i, itemName);
                }
            }
        }
        if(updated){
            recyclerView.getAdapter().notifyItemChanged(position);
        }
        else if(newItem) {
            itemIDs.add(itemID); itemNames.add(itemName); itemChecks.add(isDone);
            recyclerView.getAdapter().notifyItemInserted(itemIDs.size());
        }

    }

    public void setRecyclerViewContent(){
        Log.d("TAG", "setRecyclerViewContent: " + itemNames);
        todoListAdapter todoListAdapter = new todoListAdapter( this, itemNames, itemChecks,itemIDs);
        recyclerView.setAdapter(todoListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void addItemClick(View view){
        AddDialog addDialog = new AddDialog();
        addDialog.show(getSupportFragmentManager(), "addDialog");

    }

    @Override
    public void onButtonClick(boolean button, String itemName) {
        if(button){
            Log.d(TAG, "onButtonClick: " + itemName);
            FireBaseSetUp.getInstance().addItemToCurrent(itemName);
        }
        else{
            Log.d(TAG, "onButtonClick: canceled");
        }
    }
}