package com.prisam.customcalendar.imagedownload;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.prisam.customcalendar.R;
import com.prisam.customcalendar.common.OnItemClickListener;
import com.prisam.customcalendar.common.SuperActivity;
import com.prisam.customcalendar.common.Utils;
import com.prisam.customcalendar.custom_components.CRecyclerView;
import com.prisam.customcalendar.custom_components.StepView;
import com.prisam.customcalendar.imagedownload.imagesfolder.GridViewActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by vamsi on 10/12/2017.
 */

public class ImageActivity extends SuperActivity implements ImageAPIService.ImagesAPICallback,
        OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor>, View.OnTouchListener {

    public static final String TAG = "ImageActivity";
    private Context mContext = this;
    private ProgressBar mProgressBar;
    private Toolbar toolbar;
    private HashSet<String> stringHashSet;

    // Identifies a particular Loader being used in this component
    private static final int URL_LOADER = 0;
    ArrayList<ImageResponseModel> images = new ArrayList<>();


    private ArrayList<ImageResponseModel> imageResponseModels;
    private ImageAdapter imageAdapter;


    /*Firebase analytics log snippet for terminal*/
    /*
        You can enable verbose logging with a series of adb commands:
        adb shell setprop log.tag.FA VERBOSE
        adb shell setprop log.tag.FA-SVC VERBOSE
        adb logcat -v time -s FA FA-SVC
    */
//    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        // Obtain the FirebaseAnalytics instance.
        /*mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrash.setCrashCollectionEnabled(true);*/

        /*UI Initialization*/
        setUpUI();
//        recordScreenView();

        /*You can use Crash Reporting to log custom events
        in your error reports and optionally the logcat*/
//        FirebaseCrash.log("Activity created");

        if (getIntent().getBooleanExtra("crash", false)) {
            Toast.makeText(this, "App restarted after crash", Toast.LENGTH_SHORT).show();
//            FirebaseCrash.log("ImageActivity : App restarted after crash");
        }
    }


    /*@Override
    public boolean onTouchEvent(MotionEvent event) {
        *//* these events are traceable
        * ACTION_DOWN
        * ACTION_UP
        * ACTION_MOVE
        * *//*
        Log.v(TAG, "Touch event : " + event.toString());
        return super.onTouchEvent(event);
    }*/

    /**
     * This sample has a single Activity, so we need to manually record "screen views" as
     * we change fragments.
     */
    private void recordScreenView() {
        // This string must be <= 36 characters long in order for setCurrentScreen to succeed.
        String screenName = this.getLocalClassName();

        // [START set_current_screen]
//        mFirebaseAnalytics.setCurrentScreen(this, screenName, null /* class override */);
        // [END set_current_screen]
    }

    /**
     * Record a screen view for the visible displayed
     * inside activity
     */
    private void recordImageView(String id, String name) {

        // [START image_view_event]
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        /*predefined events*/
//        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        /*custom events*/
//        mFirebaseAnalytics.logEvent("share_selected_image", bundle);
        // [END image_view_event]
    }


    protected Menu mMenu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_save_item, menu);

        mMenu = menu;

        return mMenu.hasVisibleItems();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_image_download) {

//            Toast.makeText(this, stringHashSet.toString(), Toast.LENGTH_LONG).show();

            runIntentService();
            showMenu(false);
        }
        return super.onOptionsItemSelected(item);
    }

    private synchronized void runIntentService() {
        for (int i = 0; i < imageResponseModels.size(); i++) {

            if (stringHashSet.size() == 0)
                break;

            if (stringHashSet.contains(imageResponseModels.get(i).getName())) {
                stringHashSet.remove(imageResponseModels.get(i).getName());

                //Check box not clickable
                imageResponseModels.get(i).setDownloaded(true);

                // Call Async to download images
//                    new DownloadImage().execute(imageResponseModels.get(i).getUrl().getLarge());

                //Below code will remove the items
                    /*
                    Log.v(TAG, "Selected item removed : " + imageResponseModels.get(i).getName());
                    imageResponseModels.remove(i);

                    if (i != 0) {
                        i = i - 1;
                    }
                    */


                    /*Run the background service for image download*/
                Intent intent = new Intent(this, ImageSavingService.class);
                // add info for the service which file to download and where to store
                intent.putExtra(ImageSavingService.FILENAME, imageResponseModels.get(i).getName());
                intent.putExtra(ImageSavingService.URL, imageResponseModels.get(i).getUrl().getLarge());
                startService(intent);

                    /*Here we call only one operation*/
                break;
            }
        }

        imageAdapter.notifyDataSetChanged();
    }

    LinearLayoutManager linearLayoutManager;
    CRecyclerView recyclerView;

    private void setUpUI() {

        RelativeLayout image_activity = (RelativeLayout) findViewById(R.id.image_activity);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        recyclerView = (CRecyclerView) findViewById(R.id.recyclerView);
        StepView stepView = (StepView) findViewById(R.id.stepView);

        toolbar = new Toolbar(this);

        stringHashSet = new HashSet<>();
        imageResponseModels = new ArrayList<>();
        imageAdapter = new ImageAdapter(mContext, imageResponseModels, this);

        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(imageAdapter);


        mProgressBar.setOnTouchListener(this);
        stepView.setOnTouchListener(this);
        recyclerView.setOnTouchListener(this);


//        https://www.sitepoint.com/android-gestures-and-touch-mechanics/


        /*stepView.setOnTouchListener(this);
        recyclerView.setOnTouchListener(this);*/


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                if (linearLayoutManager.findFirstVisibleItemPosition() != 0) {
                    if (linearLayoutManager.findFirstVisibleItemPosition() == 1)
                        stepView.setData(
                                "", "", String.valueOf(imageResponseModels.get(linearLayoutManager.findFirstVisibleItemPosition() - 1).getUrl().getLarge())
                        );
                    else if (linearLayoutManager.findFirstVisibleItemPosition() == 2)
                        stepView.setData(
                                "",
                                String.valueOf(imageResponseModels.get(linearLayoutManager.findFirstVisibleItemPosition() - 2).getUrl().getLarge()),
                                String.valueOf(imageResponseModels.get(linearLayoutManager.findFirstVisibleItemPosition() - 1).getUrl().getLarge())
                        );
                    else if (linearLayoutManager.findFirstVisibleItemPosition() >= 3)
                        stepView.setData(
                                String.valueOf(imageResponseModels.get(linearLayoutManager.findFirstVisibleItemPosition() - 3).getUrl().getLarge()),
                                String.valueOf(imageResponseModels.get(linearLayoutManager.findFirstVisibleItemPosition() - 2).getUrl().getLarge()),
                                String.valueOf(imageResponseModels.get(linearLayoutManager.findFirstVisibleItemPosition() - 1).getUrl().getLarge())
                        );

                } else
                    stepView.setData("", "", "");

            }

        });


        /*Call the service*/
        if (isStoragePermissionGranted()) {
            /*
             * Initializes the CursorLoader. The URL_LOADER value is eventually passed
             * to onCreateLoader().
             */
            getLoaderManager().initLoader(URL_LOADER, null, this);

            if (Utils.isNetworkConnected(this))
                callImagesService();
            else
                Toast.makeText(this, "Please check your network connection", Toast.LENGTH_SHORT).show();
        } else {
            /*If user not granted permissions take him to permission page*/
        }

        startService(new Intent(this, BackgroundService.class)); //start service which is MyService.java
    }

    void navigateToPermissionsPage() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);

//        startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID)));

    }

    @SuppressLint("RestrictedApi")
    private void showMenu(Boolean flag) {
        Menu menu = mMenu == null ? toolbar.getMenu() : mMenu;
        if (menu != null)
            ((MenuBuilder) menu).getItem(0).setVisible(flag);
    }

    private void showLoading(boolean show) {
        if (show) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*IntentFilter filter = new IntentFilter();
        filter.addAction(ImageSavingService.NOTIFICATIOND);
        filter.addAction(ImageSavingService.NOTIFICATIONN);
        registerReceiver(receiver, filter);*/

        /*multiple actions*/
        registerReceiver(receiver, new IntentFilter(ImageSavingService.NOTIFICATIOND));
        registerReceiver(receiver, new IntentFilter(ImageSavingService.NOTIFICATIONN));
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(receiver);
    }

    void callImagesService() {
        showLoading(true);
        if (!Utils.isNetworkConnected(mContext)) {
            showLoading(false);
        } else {
            ImageAPIService.getImages(new WeakReference<ImageAPIService.ImagesAPICallback>(this));
        }
    }

    /*Call back methods*/
    @Override
    public void onSuccess(ArrayList<ImageResponseModel> imageResponseModel) {
        showLoading(false);
        imageResponseModels.clear();
        imageResponseModels.addAll(imageResponseModel);
        imageAdapter.notifyDataSetChanged();
        showMenu(false);

        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFailure(ImageResponseModel imageResponseModel) {
        showLoading(false);
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.v(TAG, "Selected position : " + position + " , Name : " + imageResponseModels.get(position).getName());

//        ImageResponseModel model = new ImageResponseModel(imageResponseModels.get(position).getName(), imageResponseModels.get(position).getUrl().getLarge());

        recordImageView(String.valueOf(position), imageResponseModels.get(position).getName());

        if (stringHashSet.contains(imageResponseModels.get(position).getName()))
            stringHashSet.remove(imageResponseModels.get(position).getName());
        else
            stringHashSet.add(imageResponseModels.get(position).getName());


        if (stringHashSet.size() > 0)
            showMenu(true);
        else
            showMenu(false);

        /*Force Firebase crash*/
        /*if (position == 7) {
//            FirebaseCrash.report(new Exception("My first Android non-fatal error"));

            List<String> strings = null;
//            try {
            if (strings.size() == 0) {

            }
//            } catch (Exception e) {
//                FirebaseCrash.report(e);
//            }
        }*/

    }

    @Override
    public void onItemScroll(int position) {

//        imageResponseModels.get(linearLayoutManager.findFirstVisibleItemPosition()-position);
//        recyclerView.scrollToPosition(linearLayoutManager.findFirstVisibleItemPosition()-position);

    }
    /*Call back methods*/

    // start of content provider call backs
    /*
    * Callback that's invoked when the system has initialized the Loader and
    * is ready to start the query. This usually happens when initLoader() is
    * called. The loaderID argument contains the ID value passed to the
    * initLoader() call.
    */
    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
    /*
     * Takes action based on the ID of the Loader that's being created
     */
        Uri mDataUrl = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] mProjection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.MediaColumns.DISPLAY_NAME};

        switch (loaderID) {
            case URL_LOADER:
                // Returns a new CursorLoader
                return new CursorLoader(
                        getApplicationContext(),   // Parent activity context
                        mDataUrl,        // Table to query
                        mProjection,     // Projection to return
                        null,            // No selection clause
                        null,            // No selection arguments
                        MediaStore.Images.Media.DATE_ADDED             // Default sort order
                );
            default:
                // An invalid id was passed in
                return null;
        }
    }

    /*
     * Defines the callback that CursorLoader calls
     * when it's finished its query
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        images = new ArrayList<>();

        int column_index_data, column_index_folder_name, column_index_file_name;


        assert cursor != null;
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        column_index_file_name = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);

        while (cursor.moveToNext()) {

                /*Images from CustomCalender folder only*/
            if (cursor.getString(column_index_folder_name).contains("CustomCalender")) {
                ImageResponseModel model = new ImageResponseModel(cursor.getString(column_index_file_name), cursor.getString(column_index_data));

                images.add(model);
//                Log.v("Images: ", cursor.getString(column_index_file_name));
            }
        }

    }

    /*
     * Invoked when the CursorLoader is being reset. For example, this is
     * called if the data in the provider changes and the Cursor becomes stale.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private Thread.UncaughtExceptionHandler _androidUncaughtExceptionHandler;


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        int eventAction = motionEvent.getAction();
        String actionName = "none";

        switch (eventAction) {
            case MotionEvent.ACTION_DOWN:
                actionName = "ACTION_DOWN";
                break;

            case MotionEvent.ACTION_MOVE:
                actionName = "ACTION_MOVE";
                break;

            case MotionEvent.ACTION_UP:
                actionName = "ACTION_UP";
                break;
        }

        /*
            User touch at:recyclerView, eventName : ACTION_DOWN
            User touch at:recyclerView, eventName : ACTION_UP
            User touch at:stepView, eventName : ACTION_DOWN
            User touch at:stepView, eventName : ACTION_DOWN
            User touch at:stepView, eventName : ACTION_DOWN
            User touch at:stepView, eventName : ACTION_DOWN
            User touch at:progressBar, eventName : ACTION_DOWN
            User touch at:progressBar, eventName : ACTION_DOWN
            User touch at:progressBar, eventName : ACTION_DOWN
        */

        Log.e(TAG, "User touch at:" + view.getContentDescription() + ", eventName : " + actionName);

        return false;
    }
    // end of content provider call backs

    /*Download image*/
    @SuppressLint("StaticFieldLeak")
    private class DownloadImage extends AsyncTask<String, Integer, Bitmap> {

        private NotificationManager mNotifyManager;
        private Notification.Action.Builder build;
        int id = (int) System.currentTimeMillis();

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            showLoading(true);
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            Bitmap bitmap = null;
            InputStream in = null;
            int responseCode = -1;

            /*7.2 mb on web and 15.85 mb on mobile image file*/
            String s = "https://upload.wikimedia.org/wikipedia/commons/0/05/Sambro_Island_Lighthouse_%282%29.jpg";

            try {

                URL url = new URL(s);//params[0]
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                responseCode = httpURLConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    in = httpURLConnection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(in);
                    in.close();

                    if (bitmap != null)
                        saveImage(bitmap);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                Log.v(TAG, "Exception : " + e.getMessage());
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            // To avoid ANR exception, run image saving process in doInBackground*
            /*if (bitmap != null)
                saveImage(bitmap);*/

            showLoading(false);
        }

        private void saveImage(Bitmap bitmap) {
            File createFolder = new File(Environment.getExternalStorageDirectory(), "CC");

            if (!createFolder.exists())
                createFolder.mkdir();

            File saveImage = new File(createFolder, System.currentTimeMillis() + ".PNG");
            try {
                OutputStream outputStream = new FileOutputStream(saveImage);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.flush();
                outputStream.close();
                if (saveImage.exists()) {
                    Log.v(TAG, "File size : " + saveImage.getAbsoluteFile().length());
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /*Download image*/

    /*Broadcast receiver*/
    boolean isShowing = false;
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case ImageSavingService.NOTIFICATIONN:
                    // do something
                    Bundle bundle = intent.getExtras();
                    if (bundle != null) {
                        boolean flag = bundle.getBoolean(ImageSavingService.INTERNET);
                        if (flag) {
                            if (isShowing) {
                                Utils.hideLoadingDialog();
                                isShowing = false;
                            }
//                            Toast.makeText(ImageActivity.this, "Have network", Toast.LENGTH_SHORT).show();
                        } else {
                            if (!isShowing) {
                                Utils.showLoadingDialog(context);
                                isShowing = true;
                            }
//                            Toast.makeText(ImageActivity.this, "No network", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case ImageSavingService.NOTIFICATIOND:
                    // do something

                    bundle = intent.getExtras();
                    if (bundle != null) {
                        String FILENAME = bundle.getString(ImageSavingService.FILENAME);
                        int resultCode = bundle.getInt(ImageSavingService.RESULT);
                        if (resultCode == RESULT_OK) {
                    /*Toast.makeText(ImageActivity.this,
                            "Download complete. Download URI: " + filePath,
                            Toast.LENGTH_SHORT).show();*/
                            displayNotification(FILENAME, "Download complete.");
                        } else {
                    /*Toast.makeText(ImageActivity.this, "Download failed",
                            Toast.LENGTH_SHORT).show();*/
                            stringHashSet.add(FILENAME);
                            displayNotification(FILENAME, "Download failed.");
                        }
                        Log.v(TAG, FILENAME + ", resultCode : " + resultCode);

                /*User selected items will download sequentially one after one*/
                        runIntentService();
                    }
                    break;
            }
        }
    };

    /*Runtime permission*/
    private static final int PERMISSION_REQUEST_CODE_EXTERNAL_STORAGE = 123;

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE_EXTERNAL_STORAGE);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    if (Utils.isNetworkConnected(this))
                        callImagesService();
                    else
                        Toast.makeText(this, "Please check your network connection", Toast.LENGTH_SHORT).show();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    navigateToPermissionsPage();
                }
                return;
            }
            default: {

            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /*Display notification*/
    private void displayNotification(String title, String message) {
        Intent intent = new Intent(mContext, GridViewActivity.class);
        intent.putExtra("key", "value");
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);//FLAG_ONE_SHOT

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notif = new Notification.Builder(mContext)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
//                .setLargeIcon(result)
//                .setStyle(new Notification.BigPictureStyle().bigPicture(result))
                .build();
        notif.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify((int) System.currentTimeMillis(), notif);
    }
}
