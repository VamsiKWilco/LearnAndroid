package com.prisam.customcalendar.imagedownload;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

import static com.prisam.customcalendar.common.Utils.isNetworkConnected;
import static com.prisam.customcalendar.imagedownload.ImageSavingService.INTERNET;
import static com.prisam.customcalendar.imagedownload.ImageSavingService.NOTIFICATIONN;

/**
 * Created by vamsi on 10/23/2017.
 */

public class BackgroundService extends Service {

    public static final int notify = 5000;  //interval between two services(Here Service run every 5 seconds)
    int count = 0;  //number of times service is display
    private Handler mHandler = new Handler();   //run on another Thread to avoid crash
    private Timer mTimer = null;    //timer handling

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        if (mTimer != null) // Cancel if already existed
            mTimer.cancel();
        else
            mTimer = new Timer();   //recreate new

        mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, notify);   //Schedule task
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTimer != null) // Cancel if already existed
            mTimer.cancel();
//        Toast.makeText(this, "Service is Destroyed", Toast.LENGTH_SHORT).show();
    }

    //class TimeDisplay for handling task
    class TimeDisplay extends TimerTask {

        @Override
        public void run() {

            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    // display toast
//                    Toast.makeText(ImageSavingService.this, "Service is running", Toast.LENGTH_SHORT).show();

                    if (!isNetworkConnected(getApplicationContext())) {
                        publishNetwork(false);
                    } else {
                        publishNetwork(true);
                    }
                }
            });

        }

    }

    /*Network update*/
    private void publishNetwork(boolean flag) {
        Intent intent = new Intent(NOTIFICATIONN);
        intent.putExtra(INTERNET, flag);
        sendBroadcast(intent);
    }
}
