package es.gonpuga.receiveroreo;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;

/**
 * Created by gonzalo on 23/11/17.
 */

/**
 * JobService to be scheduled by the JobScheduler.
 * start another service
 */
public class TestJobService extends JobService {
    private static final String TAG = "SyncService";

    @Override
    public boolean onStartJob(JobParameters params) {
        Intent service = new Intent(getApplicationContext(), ServicioCirculoPolar.class);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
            getApplicationContext().startForegroundService(service);//whitout notification => only 5 seconds alive
        else
            getApplicationContext().startService(service);
        Util.scheduleJob(getApplicationContext()); // reschedule the job performed in the main thread,
        //if you start asynchronous processing in this method, return true otherwise false.
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        //if something wrong => true to restart
        return true;
    }

}
