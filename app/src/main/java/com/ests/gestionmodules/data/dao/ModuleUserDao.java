package com.ests.gestionmodules.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.ests.gestionmodules.data.entity.ModuleUser;
import java.util.List;

@Dao
public interface ModuleUserDao {
    @Insert
    void insert(ModuleUser moduleUser);

    @Update
    void update(ModuleUser moduleUser);

    @Delete
    void delete(ModuleUser moduleUser);

    @Query("SELECT * FROM module_users WHERE userId = :userId")
    LiveData<List<ModuleUser>> getModulesForUser(int userId);

    @Query("SELECT * FROM module_users WHERE moduleId = :moduleId AND userId = :userId")
    LiveData<ModuleUser> getModuleUser(int moduleId, int userId);

    @Query("UPDATE module_users SET isDownloaded = :isDownloaded, localFilePath = :localFilePath WHERE moduleId = :moduleId AND userId = :userId")
    void updateDownloadStatus(int moduleId, int userId, boolean isDownloaded, String localFilePath);
} 