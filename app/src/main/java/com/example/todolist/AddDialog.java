package com.example.todolist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddDialog extends DialogFragment {
    private String TAG = "ADDD";
    public interface ButtonClickListener{
        void onButtonClick(boolean button, String itemName);
    }
    private ButtonClickListener listener;
    private EditText editText;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add, null);
        editText = view.findViewById(R.id.itemName);
        builder.setView(view)
                .setTitle(R.string.add_title)
                .setPositiveButton(R.string.add_positive, (dialogInterface, i) -> listener.onButtonClick(true, editText.getText().toString()))
                .setNegativeButton(R.string.add_negative, (dialogInterface, i) -> listener.onButtonClick(false, ""));
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            listener = (ButtonClickListener) context;
        }
        catch (ClassCastException e){
            Log.e(TAG, "onAttach: ", e);
        }
    }
}
