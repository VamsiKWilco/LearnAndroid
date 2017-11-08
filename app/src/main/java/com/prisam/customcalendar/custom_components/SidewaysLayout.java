package com.prisam.customcalendar.custom_components;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by vamsi on 11/6/2017.
 */

public class SidewaysLayout extends LinearLayout {

    public SidewaysLayout(Context context) {
        super(context);
    }

    public SidewaysLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SidewaysLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SidewaysLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(0, getHeight());
        canvas.rotate(-90);
        super.dispatchDraw(canvas);
        canvas.restore();
    }
}
