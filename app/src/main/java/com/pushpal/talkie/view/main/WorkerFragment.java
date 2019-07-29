package com.pushpal.talkie.view.main;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.pushpal.talkie.R;
import com.pushpal.talkie.databinding.FragmentWorkBinding;
import com.pushpal.talkie.model.model.Movie;
import com.pushpal.talkie.model.workmanager.MovieWorker;
import com.pushpal.talkie.view.adapter.FavoriteAdapter;

import java.util.concurrent.TimeUnit;

public class WorkerFragment extends Fragment implements FavoriteAdapter.FavoriteAdapterOnClickHandler {

    PeriodicWorkRequest mWorkRequest;
    WorkManager mWorkManager;
    private FragmentWorkBinding mBinding;

    static WorkerFragment newInstance() {
        Bundle args = new Bundle();
        WorkerFragment fragment = new WorkerFragment();
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

        mWorkManager = WorkManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mWorkRequest = new PeriodicWorkRequest.Builder(MovieWorker.class, 15,
                    TimeUnit.MINUTES).build();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_work, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mBinding.enqueueBtn.setOnClickListener(v -> {
            mWorkManager.enqueue(mWorkRequest);
        });

        mBinding.cancelBtn.setOnClickListener(v -> {
            mWorkManager.cancelWorkById(mWorkRequest.getId());
        });

        mWorkManager.getWorkInfoByIdLiveData(mWorkRequest.getId()).observe(this, workInfo -> {
            // onChanged
            if (workInfo != null) {
                WorkInfo.State state = workInfo.getState();
                mBinding.workStatusTv.append(state.toString() + "\n");
            }
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
