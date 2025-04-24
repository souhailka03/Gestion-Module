package com.ests.gestionmodules.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "module_users",
        foreignKeys = {
            @ForeignKey(entity = User.class,
                    parentColumns = "id",
                    childColumns = "userId",
                    onDelete = ForeignKey.CASCADE),
            @ForeignKey(entity = Module.class,
                    parentColumns = "id",
                    childColumns = "moduleId",
                    onDelete = ForeignKey.CASCADE)
        })
public class ModuleUser {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int userId;
    private int moduleId;
    private boolean isDownloaded;
    private String localFilePath;

    public ModuleUser(int userId, int moduleId) {
        this.userId = userId;
        this.moduleId = moduleId;
        this.isDownloaded = false;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getModuleId() {
        return moduleId;
    }

    public boolean isDownloaded() {
        return isDownloaded;
    }

    public String getLocalFilePath() {
        return localFilePath;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public void setDownloaded(boolean downloaded) {
        isDownloaded = downloaded;
    }

    public void setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
    }
} 