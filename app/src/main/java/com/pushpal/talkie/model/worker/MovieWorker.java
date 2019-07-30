package com.pushpal.talkie.model.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.pushpal.talkie.model.util.NotificationUtil;
import com.pushpal.talkie.view.main.MainActivity;

public class MovieWorker extends Worker {

    private static final String WORK_RESULT = "work_result";

    public MovieWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Data taskData = getInputData();
        String taskDataString = taskData.getString(MainActivity.MESSAGE_STATUS);

        NotificationUtil.showNotification(1,
                "MovieWorker",
                taskDataString != null ? taskDataString : "Message has been Sent",
                "movie_work_channel",
                "Movie Work Channel",
                null,
                getApplicationContext()
        );

        Data outputData = new Data.Builder().putString(WORK_RESULT, "Jobs Finished").build();
        return Result.success(outputData);
    }
}