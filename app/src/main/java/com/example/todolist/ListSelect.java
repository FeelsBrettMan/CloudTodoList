package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListSelect extends AppCompatActivity implements FireBaseSetUp.nestedCallback {
    private String TAG = "LS";
    RecyclerView recyclerView;
    List<String> nestedList;
    List<String> parentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_select);
        recyclerView = findViewById(R.id.listSelectRecyclerView);
        nestedList= new ArrayList<>();
        parentList = new ArrayList<>();
        FireBaseSetUp.getInstance().getListSelect(this, this,this);
    }

    @Override
    public void getNested(String nested, String currentDoc) {
        Log.d(TAG, "getNested: " + nested);
        nestedList.add(nested);
        parentList.add(currentDoc);

    }

    public void setRecyclerViewContent(){
        Log.d(TAG, "setRecyclerViewContent: " + nestedList);
        ListSelectAdapter listSelectAdapter = new ListSelectAdapter(this, nestedList, parentList);
        recyclerView.setAdapter(listSelectAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}