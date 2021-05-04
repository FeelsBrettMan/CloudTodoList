package com.example.todolist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firestore.v1.DocumentChange;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class todoList extends AppCompatActivity implements FireBaseSetUp.TodoCallback, AddDialog.ButtonClickListener, todoListAdapter.DeleteItemListener, ShareDialog.ButtonsListener  {
    String TAG = "TODO";
    RecyclerView recyclerView;
    List<String> itemNames;
    List<Boolean> itemChecks;
    List<String> itemIDs;
    Button shareButton;
    String docID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        recyclerView = findViewById(R.id.todoListRecyclerView);
        shareButton = findViewById(R.id.share_list);
        itemChecks = new ArrayList<>();
        itemNames = new ArrayList<>();
        itemIDs = new ArrayList<>();
        TextView listNameView = findViewById(R.id.listName);
        listNameView.setText(FireBaseSetUp.currentListName);

        FireBaseSetUp.getInstance().getTodoList(this,this, docID -> {
            setRecyclerViewContent();
            FireBaseSetUp.getInstance().setCollectionListener(this);
            this.docID = docID;
        });
        FloatingActionButton addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(this::addItemClick);
        shareButton.setOnClickListener(this::shareButtonClick);
    }

    @Override
    public void getElements(String itemName, Boolean isDone, String itemID) {
        Log.d("TAG", "getElements: " + itemName + " " + isDone);
        itemNames.add(itemName);
        itemChecks.add(isDone);
        itemIDs.add(itemID);
    }

    @Override
    public void getChanged(String itemName, Boolean isDone, String itemID, String changeType) {
        switch (changeType){
            case "MODIFIED":
                for (int i = 0; i < itemIDs.size(); i++) {
                    if (itemIDs.get(i).equals(itemID)) {
                        itemChecks.set(i, isDone);
                        itemNames.set(i, itemName);
                        recyclerView.getAdapter().notifyItemChanged(i);
                        return;
                    }

                }
            case "ADDED":
                if(itemIDs.contains(itemID)) return;
                itemIDs.add(itemID); itemNames.add(itemName); itemChecks.add(isDone);
                recyclerView.getAdapter().notifyItemInserted(itemIDs.size());
                return;
            case "REMOVED":
                for (int i = 0; i < itemIDs.size(); i++) {
                    if (itemIDs.get(i).equals(itemID)) {
                        itemIDs.remove(itemID); itemNames.remove(itemName); itemChecks.remove(isDone);
                        recyclerView.getAdapter().notifyItemRemoved(i);
                    }
                }
        }
    }

    public void setRecyclerViewContent(){
        Log.d("TAG", "setRecyclerViewContent: " + itemNames);
        todoListAdapter todoListAdapter = new todoListAdapter( this, itemNames, itemChecks, itemIDs, this);
        recyclerView.setAdapter(todoListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void addItemClick(View view){
        AddDialog addDialog = new AddDialog();
        addDialog.show(getSupportFragmentManager(), "addDialog");

    }

    @Override
    public void onButtonClick(boolean button, String itemName) {
        if(button){
            Log.d(TAG, "onButtonClick: " + itemName);
            FireBaseSetUp.getInstance().addItemToCurrent(itemName);
        }
        else{
            Log.d(TAG, "onButtonClick: canceled");
        }
    }

    @Override
    public void deleteItem(int position) {
        FireBaseSetUp.getInstance().deleteItemOnCurrent(itemIDs.get(position));
        Snackbar.make(findViewById(R.id.myCoordinator), "Item deleted!", Snackbar.LENGTH_LONG ).show();
    }
    public void shareButtonClick(View view){
        ShareDialog shareDialog = new ShareDialog();
        //Bundle args = new Bundle();
        //args.putString("docID", docID);
        shareDialog.setDocID(docID);
        shareDialog.show(getSupportFragmentManager(), "shareDialog");
    }

    @Override
    public void positiveClick(String docID) {
        Log.d(TAG, "positiveClick: SMS");
        Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        pickContact.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(pickContact, 1);

    }

    @Override
    public void neutralClick() {
        Log.d(TAG, "neutralClick: EMAIL");
    }

    @Override
    public void negativeClick(String docID) {
        Log.d(TAG, "negativeClick: CANCEL");
        sendEmail();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TAG", "onActivityResult: " + data.getData());
        Uri contactData = data.getData();
        Cursor c = getContentResolver().query(contactData, null, null, null, null);
        if (c.moveToFirst()) {
            int phoneIndex = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            String num = c.getString(phoneIndex);
            sendText(num);
        }
    }
    public void sendText(String number){
        Uri uri = Uri.parse("smsto:" + number);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", "Join my list using list id: " + docID);
        startActivity(intent);
    }

    public void sendEmail(){
        String subject = "Join my list";
        String message = "I would like you to join my list!\n Here is the list id: " + docID;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT , message);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an email client: "));
    }
}