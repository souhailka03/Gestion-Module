package com.ests.gestionmodules.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;
import java.util.regex.Pattern;

@Entity(tableName = "users")
public class User {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@(.+)$"
    );

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String email;

    @NonNull
    private String password;

    public User(@NonNull String email, @NonNull String password) {
        setEmail(email);
        setPassword(password);
    }

    // Getters
    public int getId() {
        return id;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    // Setters avec validation
    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(@NonNull String email) {
        String trimmedEmail = email.trim();
        if (trimmedEmail.isEmpty()) {
            throw new IllegalArgumentException("L'email ne peut pas être vide");
        }
        if (!EMAIL_PATTERN.matcher(trimmedEmail).matches()) {
            throw new IllegalArgumentException("Format d'email invalide");
        }
        this.email = trimmedEmail.toLowerCase();
    }

    public void setPassword(@NonNull String password) {
        if (password.length() < 6) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 6 caractères");
        }
        this.password = password;
    }

    public boolean checkPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }
} 