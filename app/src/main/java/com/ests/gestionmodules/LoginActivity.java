package com.ests.gestionmodules;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.ests.gestionmodules.data.AppDatabase;
import com.ests.gestionmodules.data.entity.User;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private AppDatabase db;
    private ExecutorService executorService;
    private static final String PREFS_NAME = "GestionModulesPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_FIRST_RUN = "firstRun";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Vérifier si c'est la première exécution
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (prefs.getBoolean(KEY_FIRST_RUN, true)) {
            // Réinitialiser la base de données
            AppDatabase.destroyInstance();
            db = AppDatabase.getDatabase(this);
            prefs.edit().putBoolean(KEY_FIRST_RUN, false).apply();
        } else {
            db = AppDatabase.getDatabase(this);
        }

        executorService = Executors.newSingleThreadExecutor();

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        MaterialButton loginButton = findViewById(R.id.loginButton);
        MaterialButton registerButton = findViewById(R.id.registerButton);

        loginButton.setOnClickListener(v -> attemptLogin());
        registerButton.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));

        // Créer l'utilisateur admin par défaut
        createDefaultAdmin();
    }

    private void createDefaultAdmin() {
        executorService.execute(() -> {
            // Vérifier si l'admin existe déjà
            User existingAdmin = db.userDao().getUserByEmail("admin@example.com");
            if (existingAdmin == null) {
                User admin = new User("admin@example.com", "admin123");
                db.userDao().insert(admin);
                Log.d(TAG, "Utilisateur admin créé par défaut");
            }
        });
    }

    private void attemptLogin() {
        String email = String.valueOf(emailInput.getText());
        String password = String.valueOf(passwordInput.getText());

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Tentative de connexion pour l'email: " + email);

        executorService.execute(() -> {
            User user = db.userDao().getUserByEmail(email);
            Log.d(TAG, "Utilisateur trouvé: " + (user != null));
            
            if (user != null) {
                Log.d(TAG, "Hash du mot de passe stocké: " + user.getPassword());
                boolean passwordMatch = user.checkPassword(password);
                Log.d(TAG, "Mot de passe correspond: " + passwordMatch);
            }

            runOnUiThread(() -> {
                if (user != null && user.checkPassword(password)) {
                    Log.d(TAG, "Connexion réussie");
                    // Sauvegarder l'état de connexion
                    SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean(KEY_IS_LOGGED_IN, true);
                    editor.apply();

                    startActivity(new Intent(this, ModuleListActivity.class));
                    finish();
                } else {
                    Log.d(TAG, "Échec de la connexion");
                    Toast.makeText(this, "Email ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
} 