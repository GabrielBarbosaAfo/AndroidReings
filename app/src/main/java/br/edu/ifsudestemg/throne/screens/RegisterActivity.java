package br.edu.ifsudestemg.throne.screens;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import br.edu.ifsudestemg.throne.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText etNome, etUsername, etEmail, etSenha, etApiKey;
    private Button btnRegistrar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initViews();

        btnRegistrar.setOnClickListener(v -> cadastrarUsuario());
    }

    private void initViews() {
        etNome = findViewById(R.id.et_nome);
        etUsername = findViewById(R.id.et_username);
        etEmail = findViewById(R.id.et_email);
        etSenha = findViewById(R.id.et_senha);
        etApiKey = findViewById(R.id.et_api_key);
        btnRegistrar = findViewById(R.id.btn_registrar);
    }

    private void cadastrarUsuario() {
        String email = etEmail.getText().toString().trim();
        String senha = etSenha.getText().toString().trim();
        String nome = etNome.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String apiKey = etApiKey.getText().toString().trim();

        if (email.isEmpty() || senha.isEmpty() || username.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            salvarDadosNoBanco(user.getUid(), nome, username, email, apiKey);
                        }
                    } else {
                        String erro = "Erro desconhecido";
                        if (task.getException() != null) {
                            erro = task.getException().getMessage();
                        }
                        Toast.makeText(RegisterActivity.this, "Falha: " + erro, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void salvarDadosNoBanco(String uid, String nome, String username, String email, String apiKey) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("nome", nome);
        userMap.put("username", username);
        userMap.put("email", email);
        userMap.put("apiKey", apiKey);

        db.collection("users").document(uid)
                .set(userMap)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(RegisterActivity.this, "Bem-vindo ao Reino!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RegisterActivity.this, "Erro ao salvar dados: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}