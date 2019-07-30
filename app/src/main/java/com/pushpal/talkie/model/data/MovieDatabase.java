package com.pushpal.talkie.model.data;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.pushpal.talkie.model.model.Movie;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    public abstract MovieDao movieDao();

    private static final String TAG = MovieDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DB_NAME = "movieDatabase";
    private static volatile MovieDatabase sInstance = null;

    // Returning database instance using Singleton pattern
    public static synchronized MovieDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(TAG, "Creating new database instance");
                sInstance = createDatabase(context);
            }
        }
        return sInstance;
    }

    private static MovieDatabase createDatabase(final Context context) {
        return Room.databaseBuilder(
                context,
                MovieDatabase.class,
                DB_NAME).build();
    }

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {
    }
}
