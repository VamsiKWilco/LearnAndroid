package com.prisam.customcalendar.custom_components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.prisam.customcalendar.R;
import com.prisam.customcalendar.common.MyApplication;

/**
 * Created by vamsi on 11/6/2017.
 */

public class StepView extends LinearLayout {

    @SuppressLint("StaticFieldLeak")
    public static CImageView one_Tv, two_Tv, three_Tv;
    private int numItems = 2;

    public StepView(Context context) {
        super(context);
        init(context, null);
    }

    public StepView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public StepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public StepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    // similar to /*onCreate*/
    public void init(Context context, @Nullable AttributeSet attrs) {
        int margin = 3;
        int height = 10;

        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.StepView);
            margin = array.getInt(R.styleable.StepView_sv_item_margin, margin);
            height = array.getInt(R.styleable.StepView_sv_item_height, height);
            numItems = array.getInt(R.styleable.StepView_sv_num_items, numItems);
        }

        // similar to /*setContentView*/
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.step_layout, this);

        /*Xml code*/
        one_Tv = (CImageView) findViewById(R.id.one_tv);
        two_Tv = (CImageView) findViewById(R.id.two_tv);
        three_Tv = (CImageView) findViewById(R.id.three_tv);

        /*one_Tv.setMaxHeight(height);
        two_Tv.setMaxHeight(height);
        three_Tv.setMaxHeight(height);*/

        View.OnClickListener listener = view -> {
            switch (view.getId()) {
                case R.id.one_tv:
                    updateControls(1);
                    break;
                case R.id.two_tv:
                    updateControls(2);
                    break;
                case R.id.three_tv:
                    updateControls(3);
                    break;
            }
        };

        one_Tv.setOnClickListener(listener);
        two_Tv.setOnClickListener(listener);
        three_Tv.setOnClickListener(listener);

        setOrientation(LinearLayout.VERTICAL);

        setData("", "", "");
    }

    private void updateControls(int i) {

    }

    public void setData(String s1, String s2, String s3) {

        if (!s1.isEmpty()) {
            one_Tv.setVisibility(VISIBLE);
            Glide.with(MyApplication.getInstance())
                    .load(s1)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.blueprogressbar)
                    .into(one_Tv);
        } else
            one_Tv.setVisibility(GONE);

        if (!s2.isEmpty()) {
            two_Tv.setVisibility(VISIBLE);
            Glide.with(getContext())
                    .load(s2)
                    .centerCrop()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.bg_circle)
                    .into(two_Tv);
        } else
            two_Tv.setVisibility(GONE);

        if (!s3.isEmpty()) {
            three_Tv.setVisibility(VISIBLE);
            Glide.with(getContext())
                    .load(s3)
                    .centerCrop()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.bg_circle)
                    .into(three_Tv);
        } else
            three_Tv.setVisibility(GONE);

    }
}
