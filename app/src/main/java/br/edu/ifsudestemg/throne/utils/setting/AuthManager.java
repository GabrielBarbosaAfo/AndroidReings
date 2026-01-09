package br.edu.ifsudestemg.throne.utils.setting;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
public class AuthManager {

    private final FirebaseAuth auth;
    private final FirebaseFirestore db;

    public AuthManager() {
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
                .limit(1)
                .get()
                .addOnSuccessListener(query -> {
                    if (!query.isEmpty()) {
                        String email = query.getDocuments()
                                .get(0)
                                .getString("email");
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

    public void saveUserData(
            String uid,
            String name,
            String username,
            String email,
            AuthCallback callback
    ) {
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("username", username);
        data.put("email", email);

        db.collection("users")
                .document(uid)
                .set(data)
                .addOnSuccessListener(a -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFail(e.getMessage()));
    }

    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    public void fetchUserName(AuthUserCallback callback) {

        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            callback.onError("Usuário não autenticado");
            return;
        }

        db.collection("users")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        String name = doc.getString("name");
                        callback.onSuccess(name);
                    } else {
                        callback.onError("Usuário não encontrado");
                    }
                })

                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public interface AuthUserCallback {
        void onSuccess(String name);
        void onError(String error);
    }

    public void sendEmailVerification(AuthCallback callback) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null && !user.isEmailVerified()) {
            user.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            callback.onSuccess();
                        } else {
                            String errorMsg = task.getException() != null
                                    ? task.getException().getMessage()
                                    : "Falha ao enviar e-mail.";
                            callback.onFail(errorMsg);
                        }
                    });
        } else {
            callback.onSuccess();
        }
    }


    public void logout() {
        auth.signOut();
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