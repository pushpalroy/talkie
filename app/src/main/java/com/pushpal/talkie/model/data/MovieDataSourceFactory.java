package com.pushpal.talkie.model.data;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.pushpal.talkie.model.model.Movie;

public class MovieDataSourceFactory extends DataSource.Factory<Integer, Movie> {

    private MutableLiveData<MovieDataSource> mPostLiveData;
    private String mCategory;

    public MovieDataSourceFactory(String category) {
        mCategory = category;
    }

    @Override
    public DataSource<Integer, Movie> create() {
        MovieDataSource mMovieDataSource = new MovieDataSource(mCategory);

        // Keep reference to the data source with a MutableLiveData reference
        mPostLiveData = new MutableLiveData<>();
        mPostLiveData.postValue(mMovieDataSource);

        return mMovieDataSource;
    }

    public MutableLiveData<MovieDataSource> getPostLiveData() {
        return mPostLiveData;
    }
}