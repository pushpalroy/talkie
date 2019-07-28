package com.pushpal.talkie.model.data;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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
    private static final Object LOCK = new Object();
    private static MovieRepository sInstance;
    private final MovieDao mMovieDao;
    private final RESTApi mRestApi;
    private final AppExecutors mExecutors;

    private MovieRepository(MovieDao movieDao,
                            RESTApi restApi,
                            AppExecutors executors) {
        mMovieDao = movieDao;
        mRestApi = restApi;
        mExecutors = executors;
    }

    public synchronized static MovieRepository getInstance(
            MovieDao movieDao,
            RESTApi restApi,
            AppExecutors executors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(TAG, "Making new repository");
                sInstance = new MovieRepository(movieDao, restApi, executors);
            }
        }
        return sInstance;
    }

    /**
     * Make a network request by calling enqueue and provide a LiveData object of MovieDetails for ViewModel
     *
     * @param movieId The ID of the movie
     */
    public LiveData<MovieDetails> getMovieDetails(int movieId) {
        final MutableLiveData<MovieDetails> movieDetailsData = new MutableLiveData<>();

        // Make a HTTP request to the remote web server. Send Request:
        // https://api.themoviedb.org/3/movie/{movie_id}?api_key={API_KEY}&language=en-US
        // &append_to_response=credits
        mRestApi.getMovieDetails(movieId, API_KEY)
                // Calls are executed with asynchronously with enqueue and notify callback of its response
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

    /**
     * Return a LiveData of the list of MovieEntries directly from the database
     */
    public LiveData<List<Movie>> getFavoriteMovies() {
        return mMovieDao.getAllMovies();
    }

    /**
     * Returns a LiveData of MovieEntry directly from the database
     *
     * @param movieId The movie ID
     */
    public LiveData<Movie> getFavoriteMovieByMovieId(int movieId) {
        return mMovieDao.getMovie(movieId);
    }
}