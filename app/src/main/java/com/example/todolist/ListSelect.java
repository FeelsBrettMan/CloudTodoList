package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListSelect extends AppCompatActivity implements FireBaseSetUp.NestedCallback {
    private String TAG = "LS";
    RecyclerView recyclerView;
    ArrayList<String> nestedList;
    ArrayList<String> parentList;
    ListSelectAdapter listSelectAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_select);
        recyclerView = findViewById(R.id.listSelectRecyclerView);
            nestedList = FireBaseSetUp.getInstance().returnUsersList();
            parentList = FireBaseSetUp.getInstance().returnUsersDocs();
            listSelectAdapter = new ListSelectAdapter(this, nestedList, parentList);

        setRecyclerViewContent();
        Log.d(TAG, "onCreate: " + nestedList);
    }

    @Override
    public void getNested(String nested, String currentDoc) {
        Log.d(TAG, "getNested: " + nested);

    }

    public void setRecyclerViewContent(){
        Log.d(TAG, "setRecyclerViewContent: " + nestedList);
        recyclerView.setAdapter(listSelectAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putStringArrayList("listnames", nestedList);
        outState.putStringArrayList("docNames", parentList);
    }
}