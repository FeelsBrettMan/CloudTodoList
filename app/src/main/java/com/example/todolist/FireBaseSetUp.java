package com.example.todolist;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

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

    private static FireBaseSetUp instance;
    private FirebaseUser user;
    private FirebaseFirestore db;

    private static String currentDoc;
    private static String currentListName;


    public static synchronized FireBaseSetUp getInstance(){
        if(instance == null){
            instance = new FireBaseSetUp();
        }
        return instance;
    }

    public void authenticate(Activity activity, final OnAuthenticatedListener listener) {
        if (user == null) {
            db = FirebaseFirestore.getInstance();

            final FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signInWithEmailAndPassword("bkreiser98@gmail.com","password")
                    .addOnCompleteListener(activity, task -> {
                        if (task.isSuccessful()) {
                            user = auth.getCurrentUser();
                            listener.onAuthenticated(true, "Logged in with id: " + user.getUid());
                        } else {
                            listener.onAuthenticated(false, null);
                        }
                    });
        }
    }

    public void getListSelect(Activity activity, final nestedCallback listener, ListSelect callingClass){
        db.collection("listIDs").get().addOnCompleteListener(task -> {
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
                callingClass.setRecyclerViewContent();
            }
        });
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
        db.collection("listIDs").document(currentDoc).collection(currentListName).get().addOnCompleteListener(task -> {
           if(task.isSuccessful()){

               List<DocumentSnapshot> docs = task.getResult().getDocuments();
               docs.forEach(current ->{
                   Log.d(TAG, "getTodoList: " + current.getString("itemName"));
                   Log.d(TAG, "getTodoList: "+ current.getBoolean("isDone"));
                   listener.getElements(current.getString("itemName"), current.getBoolean("isDone"));
               });
               callingClass.setRecyclerViewContent();
           }
           else {
               Log.d(TAG, "getTodoList: FAILED!");
           }
        });
    }
}

