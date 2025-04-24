package com.ests.gestionmodules.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "modules")
public class Module {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String title;

    @NonNull
    private String description;

    @NonNull
    private String fileUrl;

    public Module(@NonNull String title, @NonNull String description, @NonNull String fileUrl) {
        setTitle(title);
        setDescription(description);
        setFileUrl(fileUrl);
    }

    // Getters
    public int getId() {
        return id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    @NonNull
    public String getFileUrl() {
        return fileUrl;
    }

    // Setters avec validation
    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(@NonNull String title) {
        if (title.trim().isEmpty()) {
            throw new IllegalArgumentException("Le titre ne peut pas être vide");
        }
        this.title = title.trim();
    }

    public void setDescription(@NonNull String description) {
        if (description.trim().isEmpty()) {
            throw new IllegalArgumentException("La description ne peut pas être vide");
        }
        this.description = description.trim();
    }

    public void setFileUrl(@NonNull String fileUrl) {
        if (fileUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("L'URL du fichier ne peut pas être vide");
        }
        if (!fileUrl.startsWith("http://") && !fileUrl.startsWith("https://")) {
            throw new IllegalArgumentException("L'URL doit commencer par http:// ou https://");
        }
        this.fileUrl = fileUrl.trim();
    }
} 