package com.pushpal.talkie.model.data;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.pushpal.talkie.model.api.RESTApi;
import com.pushpal.talkie.model.model.Movie;
import com.pushpal.talkie.model.model.MovieDetails;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pushpal.talkie.model.util.Constants.API_KEY;

public class MovieRepository {

    private static final String TAG = MovieRepository.class.getSimpleName();
    private static MovieRepository sInstance;
    private final MovieDao mMovieDao;
    private final RESTApi mRestApi;
    private final AppExecutors mExecutors;

    public synchronized static MovieRepository getInstance(MovieDao movieDao, RESTApi restApi, AppExecutors executors) {
        if (sInstance == null) {
            synchronized (MovieRepository.class) {
                Log.d(TAG, "Making new repository");
                sInstance = new MovieRepository(movieDao, restApi, executors);
            }
        }
        return sInstance;
    }

    private MovieRepository(MovieDao movieDao, RESTApi restApi, AppExecutors executors) {
        mMovieDao = movieDao;
        mRestApi = restApi;
        mExecutors = executors;
    }

    public LiveData<List<Movie>> getAllMovies() {
        return mMovieDao.getAllMovies();
    }

    public LiveData<List<Movie>> getAllMoviesOrderByTitle() {
        return mMovieDao.getAll();
    }

    public LiveData<List<Movie>> getSomeRandomMovies(int numOfMovies) {
        return mMovieDao.getRandomMovies(numOfMovies);
    }

    public LiveData<Movie> getMovieById(int movieId) {
        return mMovieDao.getMovie(movieId);
    }

    public LiveData<Movie> getRandomMovie() {
        return mMovieDao.getRandomMovie();
    }

    public DataSource.Factory<Integer, Movie> getPageMovies() {
        return null;
    }

    public void insertMovie(Movie movie) {
        mExecutors.diskIO().execute(() -> mMovieDao.insertMovie(movie));
    }

    public void deleteMovie(Movie movie) {
        mExecutors.diskIO().execute(() -> mMovieDao.deleteMovie(movie));
    }

    public LiveData<MovieDetails> fetchMovieDetails(int movieId) {
        final MutableLiveData<MovieDetails> movieDetailsData = new MutableLiveData<>();

        mRestApi.getMovieDetails(movieId, API_KEY)
                .enqueue(new Callback<MovieDetails>() {
                    @Override
                    public void onResponse(@NonNull Call<MovieDetails> call, @NonNull Response<MovieDetails> response) {
                        if (response.isSuccessful()) {
                            MovieDetails movieDetails = response.body();
                            movieDetailsData.setValue(movieDetails);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<MovieDetails> call, @NonNull Throwable t) {
                        movieDetailsData.setValue(null);
                        Log.e(TAG, "Failed getting MovieDetails: " + t.getMessage());
                    }
                });
        return movieDetailsData;
    }
}