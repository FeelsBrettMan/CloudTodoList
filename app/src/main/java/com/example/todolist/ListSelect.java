package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class ListSelect extends AppCompatActivity implements FireBaseSetUp.nestedCallback {
    private String TAG = "LS";
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_select);
        recyclerView = findViewById(R.id.listSelectRecyclerView);
        FireBaseSetUp.getInstance().getList(this, this);
    }

    @Override
    public void getNested(List<String> nested) {
        Log.d(TAG, "getNested: " + nested);
        ListSelectAdapter listSelectAdapter = new ListSelectAdapter(this, nested);
        recyclerView.setAdapter(listSelectAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}