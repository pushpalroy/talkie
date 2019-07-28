package com.pushpal.talkie.view.main;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.pushpal.talkie.R;
import com.pushpal.talkie.databinding.FragmentMovieListBinding;
import com.pushpal.talkie.model.model.Movie;
import com.pushpal.talkie.model.util.GridAutoFitLayoutManager;
import com.pushpal.talkie.view.adapter.FavoriteAdapter;
import com.pushpal.talkie.viewmodel.MainViewModel;
import com.pushpal.talkie.viewmodel.MainViewModelFactory;
import com.pushpal.talkie.viewmodel.ProviderUtil;

import static com.pushpal.talkie.model.util.Constants.CATEGORY_TOP_RATED;
import static com.pushpal.talkie.model.util.Constants.LAYOUT_MANAGER_STATE;

public class FavouriteFragment extends Fragment implements FavoriteAdapter.FavoriteAdapterOnClickHandler {

    /**
     * ViewModel for MainActivity
     */
    private MainViewModel mViewModel;
    /**
     * Exposes a list of favorite movies from database to a RecyclerView
     */
    private FavoriteAdapter mFavoriteAdapter;

    /**
     * Member variable for restoring list items positions on device rotation
     */
    private Parcelable mSavedLayoutState;

    private FragmentMovieListBinding mBinding;

    public static FavouriteFragment newInstance() {
        Bundle args = new Bundle();
        FavouriteFragment fragment = new FavouriteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_movie_list, container, false);
        View view = mBinding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initAdapter();
        setupViewModel();
        updateUI();

        if (savedInstanceState != null) {
            // Get the scroll position
            mSavedLayoutState = savedInstanceState.getParcelable(LAYOUT_MANAGER_STATE);
            // Restore the scroll position
            if (mBinding.movieRv.getLayoutManager() != null)
                mBinding.movieRv.getLayoutManager().onRestoreInstanceState(mSavedLayoutState);
        }
    }

    private void initAdapter() {
        GridAutoFitLayoutManager layoutManager = new GridAutoFitLayoutManager(getActivity(), 500);
        mBinding.movieRv.setLayoutManager(layoutManager);
        mBinding.movieRv.setHasFixedSize(true);
        mFavoriteAdapter = new FavoriteAdapter(getActivity(), this);
    }

    private void setupViewModel() {
        if (getActivity() != null) {
            MainViewModelFactory viewModelFactory = ProviderUtil.provideMainActivityViewModelFactory(
                    getActivity(), CATEGORY_TOP_RATED);

            mViewModel = ViewModelProviders.of(this, viewModelFactory)
                    .get(MainViewModel.class);
        }
    }

    private void updateUI() {
        mViewModel.setFavoriteMovies();
        mBinding.movieRv.setAdapter(mFavoriteAdapter);
        observeFavoriteMovies();
    }

    private void observeFavoriteMovies() {
        mViewModel.getFavoriteMovies().observe(this, movies -> {
            // Set the list of MovieEntries to display favorite movies
            mFavoriteAdapter.setMovies(movies);

            // Restore the scroll position after setting up the adapter with the list of favorite movies
            if (mBinding.movieRv.getLayoutManager() != null)
                mBinding.movieRv.getLayoutManager().onRestoreInstanceState(mSavedLayoutState);
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Store the scroll position in our bundle
        if (mBinding.movieRv.getLayoutManager() != null)
            outState.putParcelable(LAYOUT_MANAGER_STATE,
                    mBinding.movieRv.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onFavItemClick(Movie movie) {

    }
}
