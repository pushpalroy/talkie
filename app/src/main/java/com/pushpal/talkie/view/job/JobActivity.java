package com.pushpal.talkie.view.job;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pushpal.talkie.R;
import com.pushpal.talkie.model.jobservice.NotificationJobService;

public class JobActivity extends AppCompatActivity {

    private static final int JOB_ID = 0;
    private JobScheduler mScheduler;

    //Switches for setting job options
    private Switch mDeviceIdleSwitch;
    private Switch mDeviceChargingSwitch;
    private Switch mPeriodicSwitch;

    //Override deadline seekbar
    private SeekBar mSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);

        mDeviceIdleSwitch = findViewById(R.id.idleSwitch);
        mDeviceChargingSwitch = findViewById(R.id.chargingSwitch);
        mPeriodicSwitch = findViewById(R.id.periodicSwitch);

        mSeekBar = findViewById(R.id.seekBar);

        Button scheduleJobButton = findViewById(R.id.scheduleJobButton);
        Button cancelJobButton = findViewById(R.id.cancelJobsButton);

        final TextView label = findViewById(R.id.seekBarLabel);
        final TextView seekBarProgress = findViewById(R.id.seekBarProgress);


        //Switch that toggles between periodic tasks and tasks with single deadlines
        mPeriodicSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                label.setText(R.string.periodic_interval);
            } else {
                label.setText(R.string.override_deadline);
            }
        });

        //Updates the TextView with the value from the seekbar
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean userSet) {
                if (progress > 0) {
                    String progressLabel = getString(R.string.seekbar_label, progress);
                    seekBarProgress.setText(progressLabel);
                } else {
                    seekBarProgress.setText(R.string.not_set);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // OnClickListener that sets the job.
        scheduleJobButton.setOnClickListener(view -> scheduleJob());

        //OnClickListener that cancels all existing jobs.
        cancelJobButton.setOnClickListener(view -> cancelJobs());
    }


    /**
     * onClick method that schedules the jobs based on the parameters set
     */
    private void scheduleJob() {
        mScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

        RadioGroup networkOptions = findViewById(R.id.networkOptions);

        int selectedNetworkID = networkOptions.getCheckedRadioButtonId();

        int selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE;
        switch (selectedNetworkID) {
            case R.id.noNetwork:
                selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE;
                break;
            case R.id.anyNetwork:
                selectedNetworkOption = JobInfo.NETWORK_TYPE_ANY;
                break;
            case R.id.wifiNetwork:
                selectedNetworkOption = JobInfo.NETWORK_TYPE_UNMETERED;
                break;
        }

        ComponentName serviceName = new ComponentName(getPackageName(),
                NotificationJobService.class.getName());
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, serviceName)
                .setRequiredNetworkType(selectedNetworkOption)
                .setRequiresDeviceIdle(mDeviceIdleSwitch.isChecked())
                .setRequiresCharging(mDeviceChargingSwitch.isChecked());

        int seekBarInteger = mSeekBar.getProgress();
        boolean seekBarSet = seekBarInteger > 0;

        //Set the job parameters based on the periodic switch.
        if (mPeriodicSwitch.isChecked()) {
            if (seekBarSet) {
                builder.setPeriodic(seekBarInteger * 1000);
            } else {
                Toast.makeText(JobActivity.this, R.string.no_interval_toast,
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            if (seekBarSet) {
                builder.setOverrideDeadline(seekBarInteger * 1000);
            }
        }

        boolean constraintSet = selectedNetworkOption != JobInfo.NETWORK_TYPE_NONE
                || mDeviceChargingSwitch.isChecked() || mDeviceIdleSwitch.isChecked()
                || seekBarSet;

        if (constraintSet) {
            //Schedule the job and notify the user
            JobInfo myJobInfo = builder.build();
            mScheduler.schedule(myJobInfo);
            Toast.makeText(this, R.string.job_scheduled, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.no_constraint_toast, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * onClick method for cancelling all existing jobs
     */
    private void cancelJobs() {
        if (mScheduler != null) {
            mScheduler.cancelAll();
            mScheduler = null;
            Toast.makeText(this, R.string.jobs_canceled, Toast.LENGTH_SHORT).show();
        }
    }
}
