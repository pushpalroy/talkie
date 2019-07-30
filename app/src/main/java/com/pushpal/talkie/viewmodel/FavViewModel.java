package com.pushpal.talkie.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.pushpal.talkie.model.data.AppExecutors;
import com.pushpal.talkie.model.data.MovieRepository;
import com.pushpal.talkie.model.model.Movie;

public class FavViewModel extends ViewModel {

    private final MovieRepository mRepository;

    private LiveData<PagedList<Movie>> mFavPagedList;

    FavViewModel(MovieRepository repository) {
        mRepository = repository;
        init();
    }

    private void init() {
        DataSource.Factory<Integer, Movie> dataSourceFactory = mRepository.getAllPagedMovies();

        LivePagedListBuilder<Integer, Movie> livePagedListBuilder =
                new LivePagedListBuilder<>(dataSourceFactory, 10);

        mFavPagedList = livePagedListBuilder
                .setFetchExecutor(AppExecutors.getInstance().networkIO())
                .build();
    }

    public LiveData<PagedList<Movie>> getFavPagedList() {
        return mFavPagedList;
    }
}