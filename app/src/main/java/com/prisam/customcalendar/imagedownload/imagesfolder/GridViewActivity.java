package com.prisam.customcalendar.imagedownload.imagesfolder;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.prisam.customcalendar.R;
import com.prisam.customcalendar.imagedownload.ImageSavingService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by vamsi on 10/20/2017.
 */

public class GridViewActivity extends AppCompatActivity  {
    private static final String TAG = "GridViewActivity";


    /**
     * The images.
     */
    private ArrayList<String> images;
    private double IMAGE_MAX_SIZE = 480;
    GridView gallery;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridview);

        gallery = (GridView) findViewById(R.id.galleryGridView);

        images = new ArrayList<>();

        /*Run the background service for image download*/
        Intent intent = new Intent(this, ImageSavingService.class);
        intent.putExtra(ImageSavingService.IMAGES, "images");
        startService(intent);

        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                if (null != images && !images.isEmpty())
                    Toast.makeText(
                            getApplicationContext(),
                            "position " + position + " " + images.get(position),
                            Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ImageSavingService.NOTIFICATIONG);
        filter.addAction(ImageSavingService.NOTIFICATIONN);

        registerReceiver(receiver, filter);
//        new IntentFilter(ImageSavingService.NOTIFICATIONG)
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(receiver);
    }

    /*Broadcast receiver*/
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case ImageSavingService.NOTIFICATIONN:
                    // do something
                    Bundle bundle = intent.getExtras();
                    if (bundle != null) {
                        boolean flag = bundle.getBoolean(ImageSavingService.INTERNET);
//                        if (flag)
//                            Toast.makeText(GridViewActivity.this, "Have network", Toast.LENGTH_SHORT).show();
//                        else
//                            Toast.makeText(GridViewActivity.this, "No network", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ImageSavingService.NOTIFICATIONG:
                    try {
                        bundle = intent.getExtras();
                        if (bundle != null) {
                            int resultCode = bundle.getInt(ImageSavingService.RESULT);
                            if (resultCode == RESULT_OK) {
                                images = (ArrayList<String>) bundle.getSerializable(ImageSavingService.IMAGES);

                                gallery.setAdapter(new ImageAdapter(GridViewActivity.this, images));
                            }
                        }
                    } catch (Exception e) {
                        Log.v(TAG, "Exception : " + e.getMessage());
                    }
                    break;
            }
        }
    };



    /**
     * The Class ImageAdapter.
     */
    private class ImageAdapter extends BaseAdapter {

        /**
         * The context.
         */
        private Activity context;

        /**
         * Instantiates a new image adapter.
         *
         * @param localContext the local context
         */
        ImageAdapter(Activity localContext, ArrayList<String> data) {
            context = localContext;
            images = data;
        }

        public int getCount() {
            return images.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
           /* if (position == images.size() - 1) {
                System.gc();
                Runtime.getRuntime().gc();
            }*/
            return position;
        }

        public synchronized View getView(final int position, View convertView,
                                         ViewGroup parent) {
            ImageView picturesView;
            if (convertView == null) {
                picturesView = new ImageView(context);
                picturesView.setAdjustViewBounds(true);
                picturesView.setScaleType(ImageView.ScaleType.FIT_XY);
                picturesView.setLayoutParams(new GridView.LayoutParams
                        (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                );
            } else {
                picturesView = (ImageView) convertView;
            }

            /*Load actual images from storage*/
//            picturesView.setImageURI(Uri.parse(images.get(position)));


            /*Compress and load images to avoid OOM exception*/
//            try {
//                picturesView.setImageBitmap(decodeFile(new File(images.get(position))));
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }

            /*Recommended by google to load and cache images*/
            Glide.with(context)
                    .load(images.get(position))
                    .fitCenter()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.bg_circle)
                    .into(picturesView);

            return picturesView;
        }


        /*Compress the bitmap to reduce the heap memory*/
        private Bitmap decodeFile(File f) throws FileNotFoundException {
            Bitmap b = null;

            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            FileInputStream fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            int scale = 1;
            if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                scale = (int) Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE /
                        (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;

//        Log.v("GridView", "final size " + o2.outHeight +":"+ o2.outWidth + ", actual size : h: " + o.outHeight + ", w: " + o.outWidth);

            try {
                fis = new FileInputStream(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            b = BitmapFactory.decodeStream(fis, null, o2);
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return b;
        }
    }

}
