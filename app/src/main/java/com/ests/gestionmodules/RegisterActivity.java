package com.ests.gestionmodules;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.ests.gestionmodules.data.AppDatabase;
import com.ests.gestionmodules.data.entity.User;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private TextInputEditText confirmPasswordInput;
    private AppDatabase db;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = AppDatabase.getDatabase(this);
        executorService = Executors.newSingleThreadExecutor();

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        MaterialButton registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(v -> attemptRegister());
    }

    private void attemptRegister() {
        String email = String.valueOf(emailInput.getText());
        String password = String.valueOf(passwordInput.getText());
        String confirmPassword = String.valueOf(confirmPasswordInput.getText());

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show();
            return;
        }

        executorService.execute(() -> {
            int existingUser = db.userDao().checkEmailExists(email);
            if (existingUser > 0) {
                runOnUiThread(() -> 
                    Toast.makeText(this, "Cet email est déjà utilisé", Toast.LENGTH_SHORT).show()
                );
                return;
            }

            User newUser = new User(email, password);
            db.userDao().insert(newUser);
            runOnUiThread(() -> {
                Toast.makeText(this, "Inscription réussie", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
} 