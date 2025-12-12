package br.edu.ifsudestemg.throne.utils.setting;

import android.app.Activity;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.firestore.*;

import java.util.HashMap;
import java.util.Map;

public class AuthManager {

    private final FirebaseAuth auth;
    private final FirebaseFirestore db;
    private final Activity activity;
    private GoogleSignInClient googleClient;

    public AuthManager(Activity activity) {
        this.activity = activity;
        this.auth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
    }

    public void loginWithEmail(String email, String password, AuthCallback callback) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(a -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFail(e.getMessage()));
    }

    public void findEmailByUsername(String username, EmailLookupCallback callback) {

        db.collection("users")
                .whereEqualTo("username", username)
                .get()
                .addOnSuccessListener(query -> {
                    if (!query.isEmpty()) {
                        String email = query.getDocuments().get(0).getString("email");
                        callback.onFound(email);
                    } else {
                        callback.onNotFound();
                    }
                })
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void registerUser(String email, String password, AuthCallback callback) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(a -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFail(e.getMessage()));
    }

    public void saveUserData(String uid, String name, String username, String email, AuthCallback callback) {
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("username", username);
        data.put("email", email);

        db.collection("users").document(uid)
                .set(data)
                .addOnSuccessListener(a -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFail(e.getMessage()));
    }

    public Intent getGoogleSignInIntent(String webClientId) {

        if (googleClient == null) {

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestIdToken(webClientId)
                    .build();

            googleClient = GoogleSignIn.getClient(activity, gso);
        }

        return googleClient.getSignInIntent();
    }

    public void loginWithGoogle(Task<GoogleSignInAccount> task, AuthCallback callback) {

        try {
            GoogleSignInAccount account = task.getResult(Exception.class);

            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

            auth.signInWithCredential(credential)
                    .addOnSuccessListener(a -> callback.onSuccess())
                    .addOnFailureListener(e -> callback.onFail(e.getMessage()));

        } catch (Exception e) {
            callback.onFail(e.getMessage());
        }
    }

    public void logout() {
        auth.signOut();
    }

    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    public interface AuthCallback {
        void onSuccess();
        void onFail(String error);
    }

    public interface EmailLookupCallback {
        void onFound(String email);
        void onNotFound();
        void onError(String error);
    }
}