package com.example.todolist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class todoListAdapter extends RecyclerView.Adapter<todoListAdapter.ListViewHolder> {
    Context context;
    List<String> data;
    List<Boolean> isChecked;
    List<String> itemIDs;
    DeleteItemListener listener;
    public todoListAdapter(Context context, List<String > data, List<Boolean> isChecked, List<String> itemIDs, DeleteItemListener listener){
        this.context = context;
        this.data= data;
        this.isChecked = isChecked;
        this.itemIDs = itemIDs;
        this.listener = listener;
    }


    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_element,parent,false);
        return new ListViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.textTodo.setText(data.get(position));
        holder.checkBox.setChecked(isChecked.get(position));
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ADAPT", "onClick: ");
                FireBaseSetUp.getInstance().checkItemOnCurrent(itemIDs.get(position), holder.checkBox.isChecked());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        TextView textTodo;
        CheckBox checkBox;
        DeleteItemListener listener;

        public ListViewHolder(@NonNull View itemView, DeleteItemListener listener) {
            super(itemView);
            this.listener = listener;
            textTodo = itemView.findViewById(R.id.textItemName);
            checkBox = itemView.findViewById(R.id.checkBox);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            listener.deleteItem(getAdapterPosition());
            Log.d("TAG", "onLongClick: " + getAdapterPosition());
            return true;
        }
    }
    public interface DeleteItemListener{
        void deleteItem(int position);
    }
}
