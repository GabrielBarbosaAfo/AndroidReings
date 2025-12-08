package br.edu.ifsudestemg.throne.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import br.edu.ifsudestemg.throne.R;
import br.edu.ifsudestemg.throne.utils.FeedbackUtils;

public class LoginActivity extends AppCompatActivity {

    private EditText inputUser, inputPass;
    private Button btnLogin;
    private TextView btnGoToRegister;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        init();
    }

    public void init() {
        inputUser = findViewById(R.id.input_username_login);
        inputPass = findViewById(R.id.input_password_login);
        btnLogin = findViewById(R.id.btn_login_action);
        btnGoToRegister = findViewById(R.id.btn_go_register);

        btnLogin.setOnClickListener(v -> {
            FeedbackUtils.playClickFeedback(this);
            realizarLogin();
        });

        btnGoToRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    private void realizarLogin() {
        String username = inputUser.getText().toString();
        String password = inputPass.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Preencha os campos, cavaleiro!", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("users")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot document = task.getResult();
                        if (document != null && !document.isEmpty()) {
                            String emailEncontrado = document.getDocuments().get(0).getString("email");
                            logarNoFirebase(emailEncontrado, password);
                        } else {
                            Toast.makeText(this, "Usuário não encontrado!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Erro de conexão.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void logarNoFirebase(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(this, SettingsActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Senha incorreta!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}