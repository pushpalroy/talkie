package com.pushpal.talkie.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.pushpal.talkie.model.data.AppExecutors;
import com.pushpal.talkie.model.data.MovieDataSourceFactory;
import com.pushpal.talkie.model.data.MovieRepository;
import com.pushpal.talkie.model.model.Movie;

import java.util.List;
import java.util.concurrent.Executor;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;
import static com.pushpal.talkie.model.util.Constants.INITIAL_LOAD_SIZE_HINT;
import static com.pushpal.talkie.model.util.Constants.PREFETCH_DISTANCE;

public class MainViewModel extends ViewModel {

    private final MovieRepository mRepository;

    private LiveData<PagedList<Movie>> mMoviePagedList;
    private LiveData<List<Movie>> mFavoriteMovies;

    MainViewModel(MovieRepository repository, String sortCriteria) {
        mRepository = repository;
        init(sortCriteria);
    }

    /**
     * Initialize the paged list
     */
    private void init(String sortCriteria) {
        // Pool of 5 threads
        Executor executor = AppExecutors.getInstance().networkIO();

        // Create a MovieDataSourceFactory providing DataSource generations
        MovieDataSourceFactory movieDataSourceFactory = new MovieDataSourceFactory(sortCriteria);

        // Configures how a PagedList loads content from the MovieDataSource
        PagedList.Config pagedListConfig = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                // Size hint for initial load of PagedList
                .setInitialLoadSizeHint(INITIAL_LOAD_SIZE_HINT)
                // Size of each page loaded by the PagedList
                .setPageSize(PAGE_SIZE)
                // Prefetch distance which defines how far ahead to load
                .setPrefetchDistance(PREFETCH_DISTANCE)
                .build();

        // The LivePagedListBuilder class is used to get a LiveData object of type PagedList
        mMoviePagedList = new LivePagedListBuilder<>(movieDataSourceFactory, pagedListConfig)
                .setFetchExecutor(executor)
                .build();
    }

    /**
     * Returns LiveData of PagedList of movie
     */
    public LiveData<PagedList<Movie>> getMoviePagedList() {
        return mMoviePagedList;
    }

    /**
     * Set the LiveData of PagedList of movie to clear the old list and reload
     *
     * @param sortCriteria The sort order of the movies by popular, top rated, now playing,
     *                     upcoming, and favorites
     */
    public void setMoviePagedList(String sortCriteria) {
        init(sortCriteria);
    }

    /**
     * Returns LiveData of the List of MovieEntries
     */
    public LiveData<List<Movie>> getFavoriteMovies() {
        return mFavoriteMovies;
    }

    /**
     * Set a new value for the list of MovieEntries
     */
    public void setFavoriteMovies() {
        mFavoriteMovies = mRepository.getAllMovies();
    }

    public LiveData<Movie> getRandomMovie(){
        return mRepository.getRandomMovie();
    }
}