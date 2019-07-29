package com.pushpal.talkie.model.service;

import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.pushpal.talkie.model.util.NotificationUtil;
import com.pushpal.talkie.view.main.MainActivity;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NotificationJobService extends JobService {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    // Runs on main thread
    public boolean onStartJob(JobParameters params) {
        //Set up the notification content intent to launch the app when clicked
        PendingIntent pendingIntent = PendingIntent.getActivity
                (this, 0, new Intent(this, MainActivity.class),
                        PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationUtil.showNotification(2,
                "MovieJob",
                "This is a Job Scheduler",
                "movie_job_channel",
                "Movie Job Channel",
                pendingIntent,
                getApplicationContext()
        );

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}
