package com.prisam.customcalendar.imagedownload;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by vamsi on 10/17/2017.
 */

public class ImageSavingService extends IntentService {


    private static final String TAG = "ImageSavingService";
    private int result = Activity.RESULT_CANCELED;
    public static final String URL = "url";
    public static final String FILENAME = "filename";
    public static final String FILEPATH = "CustomCalender";
    public static final String RESULT = "result";
    public static final String NOTIFICATIOND = "com.prisam.customcalendar.imagedownload.ImageActivity";

    public static final String IMAGES = "images";
    public static final String NOTIFICATIONG = "com.prisam.customcalendar.imagedownload.imagesfolder.GridViewActivity";

    public static final String NOTIFICATIONN = "com.prisam.customcalendar.imagedownload";
    public static final String INTERNET = "network";


    public ImageSavingService() {
        super("ImageSavingService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent == null) {
            publishResults("Sorry", Activity.RESULT_CANCELED);
        } else if (intent.getStringExtra(URL) != null) {
            downloadingImage(intent);
        } else if (intent.getStringExtra(IMAGES) != null) {
            ArrayList<String> listOfAllImages = new ArrayList<>();
            listOfAllImages = retriveSavedImages(this.getApplicationContext());
            if (listOfAllImages != null)
                publishImagesResults(listOfAllImages, Activity.RESULT_OK);
            else
                publishImagesResults(null, Activity.RESULT_CANCELED);
        }
    }

    /**
     * Getting All Images Path.
     *
     * @param activity the activity
     * @return ArrayList with images Path
     */
    @SuppressLint("Recycle")
    private synchronized ArrayList<String> retriveSavedImages(Context activity) {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name, column_index_file_name;
        ArrayList<String> listOfAllImages = new ArrayList<>();
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.MediaColumns.DISPLAY_NAME};

        cursor = activity.getApplicationContext().getContentResolver().query(uri, projection, null,
                null, MediaStore.Images.Media.DATE_ADDED);//

        assert cursor != null;
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        column_index_file_name = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);

        while (cursor.moveToNext()) {

                /*Images from CustomCalender folder only*/
            if (cursor.getString(column_index_folder_name).contains("CustomCalender")) {
                listOfAllImages.add(cursor.getString(column_index_data));
//                Log.v("Images: ", cursor.getString(column_index_file_name));
            }
        }

            /*Testing Glide cache by making duplicates total 768 images*/
            /*listOfAllImages.addAll(listOfAllImages); //24
            listOfAllImages.addAll(listOfAllImages); //48
            listOfAllImages.addAll(listOfAllImages); //96
            listOfAllImages.addAll(listOfAllImages); //192
            listOfAllImages.addAll(listOfAllImages); //384
            listOfAllImages.addAll(listOfAllImages); //768*/

        return listOfAllImages;
    }

    /*Download the image from the URL*/
    private void downloadingImage(Intent intent) {
        Bitmap bitmap = null;
        InputStream in = null;
        int responseCode = -1;

        String urlPath = intent.getStringExtra(URL);

        String fileName = intent.getStringExtra(FILENAME);

            /*7.2 mb on web and 15.85 mb on mobile image file*/
        String s = "https://upload.wikimedia.org/wikipedia/commons/0/05/Sambro_Island_Lighthouse_%282%29.jpg";

        try {

            URL url = new URL(urlPath);//params[0]
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();
            responseCode = httpURLConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                in = httpURLConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(in);
                in.close();

                if (bitmap != null) {
                    // successfully finished
                    result = Activity.RESULT_OK;
                    saveImage(bitmap, fileName);
                } else {
                    publishResults(fileName, result);
                }
            }

        } catch (MalformedURLException e) {
            publishResults(fileName, Activity.RESULT_CANCELED);
            e.printStackTrace();
        } catch (IOException e) {
            publishResults(fileName, Activity.RESULT_CANCELED);
            e.printStackTrace();
        } catch (Exception e) {
            publishResults(fileName, Activity.RESULT_CANCELED);
            Log.v(TAG, "Exception : " + e.getMessage());
        }
    }

    /*Save the image to local folder*/
    private void saveImage(Bitmap bitmap, String fileName) {
        File createFolder = new File(Environment.getExternalStorageDirectory(), FILEPATH);
        if (!createFolder.exists())
            createFolder.mkdir();

        File output = new File(Environment.getExternalStorageDirectory(),
                FILEPATH + "/" + fileName + ".jpg");
        if (output.exists()) {
            output.delete();

             /*Whenever you delete a file, let MediaStore Content Provider knows about it*/
//            getContentResolver().delete(Uri.fromFile(output), null, null);
        }

//        System.currentTimeMillis()
        File saveImage = new File(createFolder, fileName + ".jpg");
        try {
            OutputStream outputStream = new FileOutputStream(saveImage);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            if (saveImage.exists()) {
                /*Whenever you add a file, let MediaStore Content Provider knows about it using*/
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(saveImage)));


                publishResults(fileName, result);//output.getAbsolutePath()
                Log.v(TAG, fileName + ", size : " + saveImage.getAbsoluteFile().length());
            }
        } catch (FileNotFoundException e) {
            publishResults(fileName, result);
            e.printStackTrace();
        } catch (IOException e) {
            publishResults(fileName, result);
            e.printStackTrace();
        }
    }

    /*public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm != null ? cm.getActiveNetworkInfo() : null;
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }*/


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /*Send result to ImageActivity(NOTIFICATION)*/
    private void publishResults(String outputPath, int result) {
        Intent intent = new Intent(NOTIFICATIOND);
        intent.putExtra(FILENAME, outputPath);
        intent.putExtra(RESULT, result);
        sendBroadcast(intent);

        stopSelf();
    }

    /*Send result to GridViewActivity(NOTIFICATIONG)*/
    private void publishImagesResults(ArrayList<String> output, int result) {
        Intent intent = new Intent(NOTIFICATIONG);
        intent.putExtra(IMAGES, output);
        intent.putExtra(RESULT, result);
        sendBroadcast(intent);

        stopSelf();
    }

}
