package com.example.anhqu.orderApp.data.database;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.example.anhqu.orderApp.data.database.model.OrderItem;

/**
 * Created by anhqu on 2/26/2018.
 */

@androidx.room.Database(entities = {OrderItem.class}, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {
    public static final String DATABASE_NAME = "database";
    private static Database instance;

    public static synchronized Database getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static Database create(final Context context) {
        return Room.databaseBuilder(
                context,
                Database.class,
                DATABASE_NAME).build();
    }

    public abstract Dao Dao();
}
