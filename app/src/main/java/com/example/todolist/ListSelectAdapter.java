package com.example.todolist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;

import java.lang.annotation.Documented;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListSelectAdapter extends RecyclerView.Adapter<ListSelectAdapter.ListViewHolder> {
    String TAG = "LSA";
    Context context;
    List<String> data;
    List<String> document;

    public ListSelectAdapter(Context context, List<String> data, List<String> document){
        this.context = context;
        this.data= data;
        this.document = document;
    }


    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_select_element,parent,false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.textListName.setText(data.get(position));
        holder.listName = data.get(position);
        holder.parentDoc= document.get(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView textListName;
        String listName;
        String parentDoc;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            textListName = itemView.findViewById(R.id.textListName);


            textListName.setOnClickListener(view -> {
                Log.d(TAG, "ListViewHolder: "+ listName +" : "+ parentDoc);
                FireBaseSetUp.setCurrents(parentDoc, listName);
                Intent intent = new Intent(context, todoList.class);
                context.startActivity(intent);

            });
        }


    }
}
