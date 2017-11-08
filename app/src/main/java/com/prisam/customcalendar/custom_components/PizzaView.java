package com.prisam.customcalendar.custom_components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.prisam.customcalendar.R;

/**
 * Created by vamsi on 11/1/2017.
 */

public class PizzaView extends View {

    private Paint paint;
    private int numWedges = 0;

    public PizzaView(Context context) {
        super(context);
        init(context, null);
    }

    public PizzaView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PizzaView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PizzaView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        int strokeWidth = 4;
        int color = Color.MAGENTA;

        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PizzaView);
            strokeWidth = array.getDimensionPixelOffset(R.styleable.PizzaView_pv_stroke_width,strokeWidth);
            color = array.getColor(R.styleable.PizzaView_pv_color,color);
            numWedges = array.getInt(R.styleable.PizzaView_pv_num_wedges,numWedges);
        }

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth() - getPaddingLeft() - getPaddingRight();
        int height = getHeight() - getPaddingTop() - getPaddingBottom();
        int cx = width / 2 + getPaddingLeft();
        int cy = height / 2 + getPaddingTop();

        float diameter = Math.min(width, height) - paint.getStrokeWidth();
        float radius = diameter / 2;

        canvas.drawCircle(cx, cy, radius, paint);
        drawPizzaCuts(canvas, cx, cy, radius);
    }

    private void drawPizzaCuts(Canvas canvas, float cx, float cy, float radius) {
        float degrees = 360f / numWedges;
        canvas.save();
        for (int i = 0; i < numWedges; i++) {
//            canvas.drawText(" vamsi " + i, cx + getPaddingTop(), cy+ getPaddingRight(), paint);
            canvas.rotate(degrees, cx, cy);
            canvas.drawLine(cx, cy, cx, cy - radius, paint);
        }
        canvas.restore();
    }
}
