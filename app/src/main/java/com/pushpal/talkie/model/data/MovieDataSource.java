package com.pushpal.talkie.model.data;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.pushpal.talkie.model.api.RESTApi;
import com.pushpal.talkie.model.api.RESTClient;
import com.pushpal.talkie.model.model.Movie;
import com.pushpal.talkie.model.model.MovieResponse;
import com.pushpal.talkie.model.util.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pushpal.talkie.model.util.Constants.API_STATUS_401;
import static com.pushpal.talkie.model.util.Constants.NEXT_PAGE_KEY_TWO;
import static com.pushpal.talkie.model.util.Constants.PREVIOUS_PAGE_KEY_ONE;

public class MovieDataSource extends PageKeyedDataSource<Integer, Movie> {

    private static final String TAG = MovieDataSource.class.getSimpleName();
    private RESTApi mRestApi;
    private String mCategory;

    public MovieDataSource(String category) {
        mRestApi = RESTClient.getClient().create(RESTApi.class);
        mCategory = category;
    }

    /**
     * This method is called first to initialize a PageList with data.
     */
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params,
                            @NonNull final LoadInitialCallback<Integer, Movie> callback) {

        mRestApi.getMovies(mCategory, Constants.API_KEY, Constants.PAGE_ONE)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                callback.onResult(response.body().getResults(),
                                        PREVIOUS_PAGE_KEY_ONE, NEXT_PAGE_KEY_TWO);
                            }

                        } else if (response.code() == API_STATUS_401)
                            Log.e(TAG, "Invalid API key. Response code: " + response.code());
                        else
                            Log.e(TAG, "Response Code: " + response.code());

                    }

                    @Override
                    public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                        Log.e(TAG, "Failed initializing a PageList: " + t.getMessage());
                    }
                });
    }

    /**
     * Prepend page with the key specified by LoadParams.key
     */
    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params,
                           @NonNull LoadCallback<Integer, Movie> callback) {

    }

    /**
     * Append page with the key specified by LoadParams.key
     */
    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params,
                          @NonNull final LoadCallback<Integer, Movie> callback) {

        final int currentPage = params.key;

        mRestApi.getMovies(mCategory, Constants.API_KEY, currentPage)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                        if (response.isSuccessful()) {
                            int nextKey = currentPage + 1;
                            if (response.body() != null) {
                                callback.onResult(response.body().getResults(), nextKey);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                        Log.e(TAG, "Failed appending page: " + t.getMessage());
                    }
                });
    }
}