package com.example.todolist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class FireBaseSetUp {
    private static final String TAG = "FBSU";
    public interface OnAuthenticatedListener{
        void onAuthenticated(boolean success, String message);
    }
    public interface NestedCallback{
        void getNested(String nested, String parentDoc);
    }
    public interface TodoCallback{
        void getElements(String itemName, Boolean isDone, String itemID);
        void getChanged(String itemName, Boolean isDone, String itemID, String changeType);
    }
    public interface ListDocCallback{
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
    public boolean isSignedIn(){
        return user != null;
    }

    public void authenticate(Activity activity, final OnAuthenticatedListener listener, String email, String password) {
        if (user == null) {
            db = FirebaseFirestore.getInstance();
            usersList = new ArrayList<>();
            usersDocs = new ArrayList<>();
            final FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(activity, task -> {
                        if (task.isSuccessful()) {
                            user = auth.getCurrentUser();
                            listener.onAuthenticated(true, "Logged in with id: " + user.getUid());
                            getUsersList((nested, parentDoc) -> {
                                usersList.add(nested);
                                usersDocs.add(parentDoc);
                            });
                            Intent intent = new Intent(activity, MainActivity.class);
                            activity.startActivity(intent);
                        } else {
                            listener.onAuthenticated(false, null);
                        }
                    });
        }
    }
    public void createNewAccount(Activity activity, final OnAuthenticatedListener listener, String email, String password){
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                authenticate(activity, listener, email, password);
            }
            else{
                Log.d(TAG, "createNewAccount: FAILED!");
            }
        });
    }
    public  void getUsersList(NestedCallback listener){
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

    public void getTodoList(final TodoCallback listener, todoList callingClass, ListDocCallback docListener){
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
                   listener.getElements(current.getString("itemName"), current.getBoolean("isDone"), current.getId());
               });
               docListener.getDocID(currentDoc);
           }
           else {
               Log.d(TAG, "getTodoList: FAILED!");
           }
        });
    }
    public void createNewList(String listName, ListDocCallback listener){
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
    public void joinList(String docID, ListDocCallback listener){
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
    public void addItemToCurrent(String itemName){
        Map<String, Object> map = new HashMap<>();
        map.put("itemName", itemName);
        map.put("isDone", false);
        db.collection(mainCollection).document(currentDoc).collection(currentListName).add(map);
    }
    public void checkItemOnCurrent(String itemID, boolean isChecked){
        Map<String, Object> map = new HashMap<>();
        map.put("isDone", isChecked);
        db.collection(mainCollection).document(currentDoc).collection(currentListName).document(itemID).set(map, SetOptions.merge()).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Log.d(TAG, "checkItemOnCurrent: updated check");
            }
            else{
                Log.d(TAG, "checkItemOnCurrent: check update failed");
            }
        });
    }

    public void setCollectionListener(TodoCallback listener){
        Log.d(TAG, "setCollectionListener: setting!");
        db.collection(mainCollection).document(currentDoc).collection(currentListName).addSnapshotListener((@Nullable QuerySnapshot snapshot, FirebaseFirestoreException e) ->{
            if(e!=null){
                Log.w(TAG,"Listen Failed: ", e);
                return;
            }
            if(snapshot!=null){
                List<DocumentChange> changes =snapshot.getDocumentChanges();
                Log.d(TAG, "setCollectionListener: " + changes);
                for (DocumentChange change : changes) {
                    Log.d(TAG, "setCollectionListener: " + change.getDocument().getId()+ " " +(String)change.getDocument().get("itemName")+ " " +(Boolean) change.getDocument().get("isDone")+ " " + change.getType()) ;
                    listener.getChanged((String)change.getDocument().get("itemName"), (Boolean) change.getDocument().get("isDone"), change.getDocument().getId(), change.getType().toString());
                    ;

                }

            }
        });
    }
    public void deleteItemOnCurrent(String itemID){
        db.collection(mainCollection).document(currentDoc).collection(currentListName).document(itemID).delete();
    }
}

