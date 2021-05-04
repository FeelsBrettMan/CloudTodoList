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

public class JoinListDialog extends DialogFragment {
    private String TAG = "JLDF";
    public interface ButtonClickListener{
        void onButtonClick(boolean button, String listID);
    }
    private JoinListDialog.ButtonClickListener listener;
    private EditText editText;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add, null);
        editText = view.findViewById(R.id.itemName);
        editText.setHint(R.string.enter_list_id);
        builder.setView(view)
                .setTitle(R.string.join_list_name)
                .setPositiveButton(R.string.join_list_positive, (dialogInterface, i) -> listener.onButtonClick(true, editText.getText().toString()))
                .setNegativeButton(R.string.add_negative, (dialogInterface, i) -> listener.onButtonClick(false, ""));
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            listener = (JoinListDialog.ButtonClickListener) context;
        }
        catch (ClassCastException e){
            Log.e(TAG, "onAttach: ", e);
        }
    }
}
