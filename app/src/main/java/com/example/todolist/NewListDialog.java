package com.example.todolist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class NewListDialog extends DialogFragment {
    private String TAG = "JLDF";
    public interface ButtonClickListener{
        void onNewButtonClick(boolean button, String listName);
    }
    private NewListDialog.ButtonClickListener listener;
    private EditText editText;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add, null);
        editText = view.findViewById(R.id.itemName);
        editText.setHint(R.string.new_list);
        builder.setView(view)
                .setTitle(R.string.add_list_name)
                .setPositiveButton(R.string.new_list_positive, (dialogInterface, i) -> listener.onNewButtonClick(true, editText.getText().toString()))
                .setNegativeButton(R.string.add_negative, (dialogInterface, i) -> listener.onNewButtonClick(false, ""));
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            listener = (NewListDialog.ButtonClickListener) context;
        }
        catch (ClassCastException e){
            Log.e(TAG, "onAttach: ", e);
        }
    }
}
