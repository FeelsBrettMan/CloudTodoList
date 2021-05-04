package com.example.todolist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ShareDialog extends DialogFragment {
    public interface ButtonsListener{
        void positiveClick(String docID);
        void neutralClick();
        void negativeClick(String DocID);
    }
    ShareDialog.ButtonsListener listener;
    TextView docIDView;
    ImageButton copyButton;
    String docID = "test";
    Context context;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_share, null);
        docIDView = view.findViewById(R.id.share_text_view);
        copyButton = view.findViewById(R.id.copy_button);
        builder.setView(view)
                .setTitle(R.string.share_list_button)
                .setPositiveButton("SMS",(dialogInterface, i) -> {listener.positiveClick(docID);})
                .setNeutralButton(R.string.add_negative,(dialogInterface, i) -> {listener.neutralClick();})
                .setNegativeButton("Email",(dialogInterface, i) -> {listener.negativeClick(docID);});
        docIDView.setText(docID);
        copyButton.setOnClickListener(this::copyButtonClick);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            listener = (ButtonsListener) context;
            this.context = context;


        }
        catch (ClassCastException e){
            Log.e("SDF", "onAttach: ", e);
        }
    }
    public void setDocID(String text){
        docID = text;
        Log.d("TAG", "setDocID: " + docID);
    }
    public void copyButtonClick(View view){
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("shareID", docID);
        clipboardManager.setPrimaryClip(clip);
        Toast.makeText(context,"Copied!", Toast.LENGTH_SHORT).show();
    }
    public void smsButton(){

        Uri uri = Uri.parse("smsto:7176448650");
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.putExtra("sms_body", "Join my list using list id: " + docID);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       // Log.d("SHD", "onActivityResult: " + data.getData());
        //Uri contactData = data.getData();
    }
}
