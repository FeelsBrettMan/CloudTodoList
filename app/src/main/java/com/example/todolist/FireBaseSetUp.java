package com.example.todolist;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class FireBaseSetUp {
    private final String TAG = "FBSU";
    public interface OnAuthenticatedListener{
        void onAuthenticated(boolean success, String message);
    }

    private static FireBaseSetUp instance;
    private FirebaseUser user;
    private FirebaseFirestore db;

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

    public void getList(){
/*        db.collection("listIDs").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                List<String> users = (List<String>) task.getResult().getDocuments().get(0).get("Users");
                users.forEach(current ->{
                    if(user.getUid().equals(current)){
                        db.collection("listIDs").document("sVNtz4WnhWbba6Xaetrl").get();
                    }
                });
            }
        });*/
        db.collection("listIDs").document("sVNtz4WnhWbba6Xaetrl").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                List<String> nested = (List<String>) task.getResult().get("nested");
                db.collection("listIDs").document("sVNtz4WnhWbba6Xaetrl").collection(nested.get(0)).get().addOnCompleteListener(task1 -> {
                    Log.d(TAG, "getList: "+ task1.getResult().getDocuments());
                });
            }
        });

    }
}

