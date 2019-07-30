package com.pushpal.talkie.model.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.pushpal.talkie.model.model.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movies")
    LiveData<List<Movie>> getAllMovies();

    @Query("SELECT * FROM movies WHERE id = :id")
    LiveData<Movie> getMovie(int id);

    /**
     * Returns all data in table for paging
     */
    @Query("SELECT * FROM movies ORDER BY title ASC")
    LiveData<List<Movie>> getAll();

    /**
     * Returns LiveData of random movies
     *
     * @param limit number of returns
     */
    @Query("SELECT * FROM movies ORDER BY RANDOM() limit :limit")
    LiveData<List<Movie>> getRandomMovies(int limit);

    /**
     * Returns a random movie
     */
    @Query("SELECT * FROM movies ORDER BY RANDOM() limit 1")
    LiveData<Movie> getRandomMovie();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(Movie movie);

    @Update
    void updateMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);
}