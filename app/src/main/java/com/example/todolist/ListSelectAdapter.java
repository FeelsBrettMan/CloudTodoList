package com.example.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListSelectAdapter extends RecyclerView.Adapter<ListSelectAdapter.ListViewHolder> {
    Context context;
    List<String> data;
    public ListSelectAdapter(Context context, List<String> data){
        this.context = context;
        this.data= data;
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
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView textListName;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            textListName = itemView.findViewById(R.id.textListName);
        }
    }
}
