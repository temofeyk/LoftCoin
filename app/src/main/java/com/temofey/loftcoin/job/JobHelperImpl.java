package com.temofey.loftcoin.job;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.PersistableBundle;
import android.widget.Toast;

import com.temofey.loftcoin.R;

public class JobHelperImpl implements JobHelper {

    private final static int SYNC_RATE_JOB_ID = 99;

    private Context context;

    public JobHelperImpl(Context context) {
        this.context = context;
    }

    @Override
    public void startSyncRateJob(String symbol) {

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        if (jobScheduler == null) {
            return;
        }

        ComponentName componentName = new ComponentName(context, SyncRateJobService.class);
        PersistableBundle extras = new PersistableBundle();

        extras.putString(SyncRateJobService.EXTRA_SYMBOL, symbol);


        JobInfo jobInfo = new JobInfo.Builder(SYNC_RATE_JOB_ID, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setRequiresDeviceIdle(false)
                .setRequiresCharging(false)
                .setExtras(extras)
                .setPersisted(true)
                .build();

        int result = jobScheduler.schedule(jobInfo);
        if (result == JobScheduler.RESULT_SUCCESS)
            Toast.makeText(context, context.getString(R.string.rate_job_started, symbol), Toast.LENGTH_LONG)
                    .show();
    }
}
