package com.ests.gestionmodules.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.ests.gestionmodules.data.dao.UserDao;
import com.ests.gestionmodules.data.dao.ModuleDao;
import com.ests.gestionmodules.data.dao.ModuleUserDao;
import com.ests.gestionmodules.data.entity.User;
import com.ests.gestionmodules.data.entity.Module;
import com.ests.gestionmodules.data.entity.ModuleUser;

@Database(entities = {User.class, Module.class, ModuleUser.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract ModuleDao moduleDao();
    public abstract ModuleUserDao moduleUserDao();

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "app_database"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
} 