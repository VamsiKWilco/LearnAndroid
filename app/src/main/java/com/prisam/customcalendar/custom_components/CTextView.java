package com.prisam.customcalendar.custom_components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by vamsi on 10/31/2017.
 */

@SuppressLint("AppCompatCustomView")
public class CTextView extends TextView {
    public CTextView(Context context) {
        super(context);
        setData();
    }

    public CTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setData();
    }

    public CTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setData();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setData();
    }

    private void setData() {

        try {
            if (isInEditMode()) {
                setTextSize(18);
                PackageInfo packageInfo = getContext().getPackageManager().getPackageInfo(
                        getContext().getPackageName(), 0);
                setText(packageInfo.versionName);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }
}
