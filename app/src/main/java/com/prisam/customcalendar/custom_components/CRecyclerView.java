package com.prisam.customcalendar.custom_components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by vamsi on 11/6/2017.
 */

public class CRecyclerView extends RecyclerView {

    int cornerRadius;
    Paint paint;
    Path clipPath;
    RectF rectF;

    public CRecyclerView(Context context) {
        super(context);
        init();
    }

    public CRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /*
    * Initialize fields
    * */
    protected void init() {
        /*
        * Below Jelly Bean, clipPath on canvas would not work because lack of hardware acceleration
        * support. Hence, we should explicitly say to use software acceleration.
        * */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }

        rectF = new RectF();
        clipPath = new Path();

        cornerRadius = 30;

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.TRANSPARENT);
    }

    /*
    * Set the canvas bounds here
    * */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int screenWidth = MeasureSpec.getSize(widthMeasureSpec);
        int screenHeight = MeasureSpec.getSize(heightMeasureSpec);
        rectF.set(0, 0, screenWidth, screenHeight);
    }

    @Override
    public void onDraw(Canvas canvas) {

        /*Circle*/
        canvas.drawCircle(rectF.width(), rectF.height(), cornerRadius, paint);

        /*Rectangle with corner radius*/
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint);
        clipPath.addRoundRect(rectF, cornerRadius, cornerRadius, Path.Direction.CW);

        canvas.clipPath(clipPath);
        super.onDraw(canvas);
    }
}
