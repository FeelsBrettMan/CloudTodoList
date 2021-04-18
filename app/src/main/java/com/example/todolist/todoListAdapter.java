package com.example.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class todoListAdapter extends RecyclerView.Adapter<todoListAdapter.ListViewHolder> {
    Context context;
    String[] data;
    boolean[] isChecked;
    public todoListAdapter(Context context, String[] data, boolean[] isChecked){
        this.context = context;
        this.data= data;
        this.isChecked = isChecked;
    }


    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_element,parent,false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.textTodo.setText(data[position]);
        holder.checkBox.setChecked(isChecked[position]);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView textTodo;
        CheckBox checkBox;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            textTodo = itemView.findViewById(R.id.textItemName);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}