package com.example.todolist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FireBaseSetUp {
    private static final String TAG = "FBSU";
    public interface OnAuthenticatedListener{
        void onAuthenticated(boolean success, String message);
    }
    public interface nestedCallback{
        void getNested(String nested, String parentDoc);
    }
    public interface todoCallback{
        void getElements(String itemName, Boolean isDone);
    }
    public interface listDocCallback{
        void getDocID(String docID);
    }

    private static FireBaseSetUp instance;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private String mainCollection = "listIDs";
    private ArrayList<String> usersList;
    private ArrayList<String> usersDocs;

    private static String currentDoc;
    public static String currentListName;


    public static synchronized FireBaseSetUp getInstance(){
        if(instance == null){
            instance = new FireBaseSetUp();
        }
        return instance;
    }

    public void authenticate(Activity activity, final OnAuthenticatedListener listener) {
        if (user == null) {
            db = FirebaseFirestore.getInstance();
            usersList = new ArrayList<>();
            usersDocs = new ArrayList<>();
            final FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signInWithEmailAndPassword("bkreiser98@gmail.com","password")
                    .addOnCompleteListener(activity, task -> {
                        if (task.isSuccessful()) {
                            user = auth.getCurrentUser();
                            listener.onAuthenticated(true, "Logged in with id: " + user.getUid());
                            getUsersList((nested, parentDoc) -> {
                                usersList.add(nested);
                                usersDocs.add(parentDoc);
                            });
                        } else {
                            listener.onAuthenticated(false, null);
                        }
                    });
        }
    }
    public  void getUsersList(nestedCallback listener){
        db.collection(mainCollection).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                List<DocumentSnapshot> docs = task.getResult().getDocuments();
                docs.forEach(currentDoc->{
                    List<String> userIDs = (List<String>) currentDoc.get("Users");
                    userIDs.forEach(currentUserID->{
                        if(user.getUid().equals(currentUserID)){
                            String nestedListName = currentDoc.getString("nested");
                            Log.d(TAG, "getList: "+ currentDoc.getId());
                            listener.getNested(nestedListName, currentDoc.getId());
                        }
                    });
                });
            }
        });
    }
    public ArrayList<String> returnUsersList(){
        return usersList;
    }
    public ArrayList<String> returnUsersDocs(){
        return usersDocs;
    }


    public static synchronized void setCurrents(String doc, String listName){
        Log.d(TAG, "setCurrents: "+ listName +" : "+ doc);
        currentDoc = doc;
        currentListName = listName;
    }

    public void getTodoList(final todoCallback listener, todoList callingClass){
        Log.d(TAG, "getTodoList: " + currentDoc + currentListName);
        if(currentDoc==null){
            Log.d(TAG, "getTodoList: currentDoc is null");
            return;
        }
        db.collection(mainCollection).document(currentDoc).collection(currentListName).get().addOnCompleteListener(task -> {
           if(task.isSuccessful()){

               List<DocumentSnapshot> docs = task.getResult().getDocuments();
               docs.forEach(current ->{
                   Log.d(TAG, "getTodoList: " + current.getString("itemName"));
                   Log.d(TAG, "getTodoList: "+ current.getBoolean("isDone"));
                   Log.d(TAG, "getTodoList: "+ current.getData());
                   listener.getElements(current.getString("itemName"), current.getBoolean("isDone"));
               });
               callingClass.setRecyclerViewContent();
           }
           else {
               Log.d(TAG, "getTodoList: FAILED!");
           }
        });
    }
    public void createNewList(String listName, listDocCallback listener){
        Map<String, Object > map = new HashMap<>();
        ArrayList<String> usersList = new ArrayList<>();
        usersList.add(user.getUid());
        map.put("Users",usersList);
        map.put("nested", listName);
        db.collection(mainCollection).add(map).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                HashMap<String, Object> sampleData = new HashMap<>();
                sampleData.put("itemName", "Sample");
                sampleData.put("isDone", true);
                task.getResult().collection(listName).add(sampleData).addOnCompleteListener(task1 -> {
                    if(task1.isSuccessful()){
                        Log.d(TAG, "createNewList: List created!");
                        FireBaseSetUp.setCurrents(task.getResult().getId(),listName);
                        listener.getDocID(currentDoc);
                    }
                    else {
                        Log.d(TAG, "createNewList: List failed!");
                    }
                });
            }
        });
    }
    public void joinList(String docID, listDocCallback listener){
        db.collection(mainCollection).document(docID).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Map<String, Object> map = task.getResult().getData();
                ArrayList<String>  userList = (ArrayList<String>) map.get("Users");
                userList.add(user.getUid());
                map.put("Users", userList);
                db.collection(mainCollection).document(docID).set(map);
                FireBaseSetUp.setCurrents(docID, (String) map.get("nested"));
                listener.getDocID(docID);
            }
        });
    }
}

