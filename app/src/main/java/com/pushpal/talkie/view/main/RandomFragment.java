package com.pushpal.talkie.view.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.pushpal.talkie.R;
import com.pushpal.talkie.databinding.FragmentRandomMovieBinding;
import com.pushpal.talkie.model.model.Movie;
import com.pushpal.talkie.view.adapter.FavoriteAdapter;
import com.pushpal.talkie.viewmodel.MainViewModel;
import com.pushpal.talkie.viewmodel.MainViewModelFactory;
import com.pushpal.talkie.viewmodel.ProviderUtil;
import com.squareup.picasso.Picasso;

import static com.pushpal.talkie.model.util.Constants.IMAGE_BASE_URL;
import static com.pushpal.talkie.model.util.Constants.IMAGE_SIZE_342;

public class RandomFragment extends Fragment implements FavoriteAdapter.FavoriteAdapterOnClickHandler {

    private FragmentRandomMovieBinding mBinding;
    private MainViewModel mViewModel;

    static RandomFragment newInstance() {
        Bundle args = new Bundle();
        RandomFragment fragment = new RandomFragment();
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

        if (getActivity() != null) {
            MainViewModelFactory viewModelFactory = ProviderUtil.provideMainActivityViewModelFactory(
                    getActivity(), null);

            mViewModel = ViewModelProviders.of(this, viewModelFactory)
                    .get(MainViewModel.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_random_movie, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mViewModel.getRandomMovie().observe(getViewLifecycleOwner(), movie -> {
            mBinding.tvMovieTitle.setText(movie.getTitle());
            mBinding.tvVoteCount.setText(String.valueOf(movie.getVoteAverage()));
            String thumbnail = IMAGE_BASE_URL + IMAGE_SIZE_342 + movie.getPosterPath();
            Picasso.with(getActivity())
                    .load(thumbnail)
                    .into(mBinding.ivMoviePoster);
        });
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
