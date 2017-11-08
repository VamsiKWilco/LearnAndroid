package com.prisam.customcalendar.custom_components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by vamsi on 10/31/2017.
 */

@SuppressLint("AppCompatCustomView")
public class CButton extends Button {

    public CButton(Context context) {
        super(context);
        setData();
    }

    public CButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setData();
    }

    public CButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setData();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setData();
    }

    private void setData() {
        if (isInEditMode()) {
            setClickable(true);
            setHeight(48);
            setWidth(48);
        }
    }
}
