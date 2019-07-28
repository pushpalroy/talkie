package com.pushpal.talkie.view.topRated;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.pushpal.talkie.R;
import com.pushpal.talkie.databinding.FragmentTopRatedBinding;
import com.pushpal.talkie.model.model.Movie;
import com.pushpal.talkie.view.adapter.MoviePagedListAdapter;
import com.pushpal.talkie.viewmodel.MainActivityViewModel;
import com.pushpal.talkie.viewmodel.MainViewModelFactory;
import com.pushpal.talkie.viewmodel.ProviderUtil;

import static com.pushpal.talkie.model.util.Constants.CATEGORY_TOP_RATED;
import static com.pushpal.talkie.model.util.Constants.GRID_SPAN_COUNT;
import static com.pushpal.talkie.model.util.Constants.LAYOUT_MANAGER_STATE;

public class TopRatedFragment extends Fragment implements MoviePagedListAdapter.MoviePagedListAdapterOnClickHandler {

    /**
     * ViewModel for MainActivity
     */
    private MainActivityViewModel mViewModel;
    /**
     * MoviePagedListAdapter enables for data to be loaded in chunks
     */
    private MoviePagedListAdapter mMoviePagedListAdapter;

    /**
     * Member variable for restoring list items positions on device rotation
     */
    private Parcelable mSavedLayoutState;

    private FragmentTopRatedBinding mBinding;

    public static TopRatedFragment newInstance() {
        Bundle args = new Bundle();
        TopRatedFragment fragment = new TopRatedFragment();
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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_top_rated, container, false);
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
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), GRID_SPAN_COUNT);
        mBinding.movieRv.setLayoutManager(layoutManager);
        mBinding.movieRv.setHasFixedSize(true);
        mMoviePagedListAdapter = new MoviePagedListAdapter(this);
    }

    private void setupViewModel() {
        if (getActivity() != null) {
            MainViewModelFactory viewModelFactory = ProviderUtil.provideMainActivityViewModelFactory(
                    getActivity(), CATEGORY_TOP_RATED);

            mViewModel = ViewModelProviders.of(this, viewModelFactory)
                    .get(MainActivityViewModel.class);
        }
    }

    private void updateUI() {
        mBinding.movieRv.setAdapter(mMoviePagedListAdapter);
        observeMoviePagedList();
    }

    private void observeMoviePagedList() {
        mViewModel.getMoviePagedList().observe(this, pagedList -> {
            if (pagedList != null) {
                mMoviePagedListAdapter.submitList(pagedList);

                // Restore the scroll position after setting up the adapter with the list of movies
                if (mBinding.movieRv.getLayoutManager() != null)
                    mBinding.movieRv.getLayoutManager().onRestoreInstanceState(mSavedLayoutState);
            }
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
    public void onItemClick(Movie movie) {

    }
}
