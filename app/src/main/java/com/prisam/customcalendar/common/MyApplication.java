package com.prisam.customcalendar.common;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.google.firebase.crash.FirebaseCrash;
import com.prisam.customcalendar.BuildConfig;
import com.prisam.customcalendar.imagedownload.ImageActivity;

import io.fabric.sdk.android.Fabric;

/**
 * Created by vamsi on 10/26/2017.
 */

public class MyApplication extends Application implements Thread.UncaughtExceptionHandler {

    public static MyApplication instance;

    private Thread.UncaughtExceptionHandler _androidUncaughtExceptionHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        _androidUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);

//        setUpCrashlytics();
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    public static MyApplication getInstance() {
        return instance;
    }

    private void setUpCrashlytics() {
        //common to both flavours
        /*Fabric.with(this, new Crashlytics());*/

        if (BuildConfig.DEBUG) {
            final Fabric fabric = new Fabric.Builder(this)
                    .kits(new Crashlytics())
                    .debuggable(true)
                    .build();
            Fabric.with(fabric);
        } else {
            // disabled for debug builds
            Crashlytics crashlyticsKit = new Crashlytics.Builder()
                    .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                    .build();

            // Initialize Fabric with the debug-disabled crashlytics.
            Fabric.with(this, crashlyticsKit);
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        try {
            /*Do your stuff with the exception*/
        } catch (Exception e) {
            /* Ignore */
        } finally {
//            FirebaseCrash.report(throwable);
             /*Let Android show the default error dialog*/
//            _androidUncaughtExceptionHandler.uncaughtException(thread, throwable);

//            reLaunchApp();
        }
    }

    void reLaunchApp() {

        Intent intent = new Intent(getInstance(), ImageActivity.class);
        intent.putExtra("crash", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity
                (MyApplication.getInstance().getBaseContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);

        AlarmManager mgr = (AlarmManager) MyApplication.getInstance().getBaseContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent);

        getInstance().onTerminate();

        System.exit(2);

    }


}
