package com.pushpal.talkie.view.main;

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

import com.pushpal.talkie.R;
import com.pushpal.talkie.databinding.FragmentMovieListBinding;
import com.pushpal.talkie.model.model.Movie;
import com.pushpal.talkie.model.util.GridAutoFitLayoutManager;
import com.pushpal.talkie.view.adapter.FavPagedListAdapter;
import com.pushpal.talkie.viewmodel.FavViewModel;
import com.pushpal.talkie.viewmodel.FavViewModelFactory;
import com.pushpal.talkie.viewmodel.ProviderUtil;

public class FavPagedFragment extends Fragment implements FavPagedListAdapter.FavPagedListAdapterOnClickHandler {

    /**
     * ViewModel for MainActivity
     */
    private FavViewModel mFavViewModel;
    /**
     * MoviePagedListAdapter enables for data to be loaded in chunks
     */
    private FavPagedListAdapter mFavPagedListAdapter;

    /**
     * Member variable for restoring list items positions on device rotation
     */
    private Parcelable mSavedLayoutState;

    private FragmentMovieListBinding mBinding;

    public static FavPagedFragment newInstance() {
        Bundle args = new Bundle();
        FavPagedFragment fragment = new FavPagedFragment();
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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_list, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initAdapter();
        setupViewModel();
        updateUI();
    }

    private void initAdapter() {
        GridAutoFitLayoutManager layoutManager = new GridAutoFitLayoutManager(getActivity(), 500);
        mBinding.movieRv.setLayoutManager(layoutManager);
        mBinding.movieRv.setHasFixedSize(true);
        mFavPagedListAdapter = new FavPagedListAdapter(this);
    }

    private void setupViewModel() {
        if (getActivity() != null) {
            FavViewModelFactory viewModelFactory = ProviderUtil.provideFavViewModelFactory(getActivity());
            mFavViewModel = ViewModelProviders.of(this, viewModelFactory).get(FavViewModel.class);
        }
    }

    private void updateUI() {
        mBinding.movieRv.setAdapter(mFavPagedListAdapter);
        observeFavPagedList();
    }

    private void observeFavPagedList() {
        try {
            mFavViewModel.getFavPagedList().observe(this, pagedList -> {
                try {
                    if (pagedList != null) {
                        mFavPagedListAdapter.submitList(pagedList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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
