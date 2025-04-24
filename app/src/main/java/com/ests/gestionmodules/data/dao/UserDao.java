package com.ests.gestionmodules.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.ests.gestionmodules.data.entity.User;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User getUserByEmail(String email);

    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
    int checkEmailExists(String email);
} 