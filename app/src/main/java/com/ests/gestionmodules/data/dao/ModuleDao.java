package com.ests.gestionmodules.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.ests.gestionmodules.data.entity.Module;
import java.util.List;

@Dao
public interface ModuleDao {
    @Insert
    void insert(Module module);

    @Update
    void update(Module module);

    @Delete
    void delete(Module module);

    @Query("SELECT * FROM modules ORDER BY title ASC")
    LiveData<List<Module>> getAllModules();

    @Query("SELECT * FROM modules WHERE id = :moduleId")
    LiveData<Module> getModuleById(int moduleId);

    @Query("SELECT * FROM modules ORDER BY title ASC")
    List<Module> getAllModulesSync();
} 