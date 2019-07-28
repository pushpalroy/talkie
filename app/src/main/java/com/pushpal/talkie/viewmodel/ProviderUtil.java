package com.pushpal.talkie.viewmodel;

import android.content.Context;

import com.pushpal.talkie.model.api.RESTApi;
import com.pushpal.talkie.model.api.RESTClient;
import com.pushpal.talkie.model.data.AppExecutors;
import com.pushpal.talkie.model.data.MovieDatabase;
import com.pushpal.talkie.model.data.MovieRepository;

public class ProviderUtil {

    public static MainViewModelFactory provideMainActivityViewModelFactory(Context context,
                                                                           String sortCriteria) {
        MovieRepository repository = provideRepository(context.getApplicationContext());
        return new MainViewModelFactory(repository, sortCriteria);
    }

    private static MovieRepository provideRepository(Context context) {
        MovieDatabase database = MovieDatabase.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        RESTApi theMovieApi = RESTClient.getClient().create(RESTApi.class);

        return MovieRepository.getInstance(
                database.movieDao(),
                theMovieApi,
                executors);
    }
}