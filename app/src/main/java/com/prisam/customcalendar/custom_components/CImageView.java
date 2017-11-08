package com.prisam.customcalendar.custom_components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.bumptech.glide.Glide;
import com.prisam.customcalendar.R;

/**
 * Created by vamsi on 11/7/2017.
 */

public class CImageView extends AppCompatImageView {

    /*Reference -
                --> https://android.jlelse.eu/avatarview-custom-implementation-of-imageview-4bcf0714d09d*/
    /*
   * Path of them image to be clipped (to be shown)
   * */
    Path clipPath;

    /*
    * Place holder drawable (with background color and initials)
    * */
    Drawable drawable;

    /*
    * Contains initials of the member
    * */
//    String text;

    /*
    * Used to set size and color of the member initials
    * */
    TextPaint textPaint;

    /*
    * Used as background of the initials with user specific color
    * */
    Paint paint;

    /*
    * Shape to be drawn
    * */
    int shape;

    /*
    * Constants to define shape
    * */
    protected static final int CIRCLE = 0;
    protected static final int RECTANGLE = 1;

    /*
    * User whose avatar should be displayed
    * */
//    String user;

    /*
    * Image width and height (both are same and constant, defined in dimens.xml
    * We cache them in this field
    * */
//    private int imageSize;

    /*
    * We will set it as 5dp
    * */
    int cornerRadius;

    /*
    * Bounds of the canvas in float
    * Used to set bounds of member initial and background
    * */
    RectF rectF;

    public CImageView(Context context) {
        super(context);
    }

    public CImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        getAttributes(attrs);
        init();
    }

    public CImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        getAttributes(attrs);
        init();
    }

    private void getAttributes(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CImageView,
                0, 0);

        try {

            /*
            * Get the shape and set shape field accordingly
            * */
            String viewShape = a.getString(R.styleable.CImageView_iv_shape);

            /*
            * If the attribute is not specified, consider circle shape
            * */
            if (viewShape == null) {
                shape = RECTANGLE;
            } else {
                if (getContext().getString(R.string.rectangle).equals(viewShape)) {
                    shape = RECTANGLE;
                } else {
                    shape = CIRCLE;
                }
            }
        } finally {
            a.recycle();
        }
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

//        imageSize = getResources().getDimensionPixelSize(R.dimen.image_size);
        cornerRadius = 30;//(int) Util.dpToPixel(2, getResources());

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint.setColor(Color.parseColor("#dfe5f3"));
        paint.setColor(Color.LTGRAY);
//        dfe5f3

        /*textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(16f * getResources().getDisplayMetrics().scaledDensity);
        textPaint.setColor(Color.WHITE);*/
    }

    /*
    * Get User object and set values based on the user
    * This is the only exposed method to the developer
    * */
    public void setUser(String user) {
//        this.user = user;
//        setValues();
    }

    /*
    * Set user specific fields in here
    * */
    private void setValues() {

        /*
        * user specific color for initial background
        * */
//        paint.setColor(Color.LTGRAY);

        /*
        * Initials of member
        * */
        /*if (user != null)
            text = String.valueOf(user.indexOf(0));*/

//        setDrawable();

        /*if (user != null) {
            Glide.with(getContext())
                    .load(user)
                    .placeholder(drawable)
                    .centerCrop()
//                    .override(imageSize, imageSize)
                    .into(this);
        } else {
            setImageDrawable(drawable);
            invalidate();
        }*/
    }


    /*
    * Create placeholder drawable
    * */
    private void setDrawable() {
        drawable = new Drawable() {
            @Override
            public void draw(@NonNull Canvas canvas) {

                /*int centerX = Math.round(canvas.getWidth() * 0.5f);
                int centerY = Math.round(canvas.getHeight() * 0.5f);*/

        /*
        * To draw text
        * */
//                if (text != null) {
//                    float textWidth = textPaint.measureText(text) * 0.5f;
//                    float textBaseLineHeight = textPaint.getFontMetrics().ascent * -0.4f;

            /*
            * Draw the background color before drawing initials text
            * */
                if (shape == RECTANGLE) {
                    canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint);
                }
                   /* else {
                        canvas.drawCircle(centerX,
                                centerY,
                                Math.max(canvas.getHeight() / 2, textWidth / 2),
                                paint);
                    }*/

            /*
            * Draw the text above the background color
            * */
//                    canvas.drawText(text, centerX - textWidth, centerY + textBaseLineHeight, textPaint);
//                }
            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(ColorFilter colorFilter) {

            }

            @Override
            public int getOpacity() {
                return PixelFormat.UNKNOWN;
            }
        };
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
    protected void onDraw(Canvas canvas) {

        if (shape == RECTANGLE) {
            canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint);
            clipPath.addRoundRect(rectF, cornerRadius, cornerRadius, Path.Direction.CW);
        }
        /*else {
            canvas.drawCircle(rectF.centerX(), rectF.centerY(), (rectF.height() / 2) - rectF.centerX(), paint);

            clipPath.addCircle(rectF.centerX(), rectF.centerY(), (rectF.height() / 2), Path.Direction.CW);
        }*/
        canvas.clipPath(clipPath);
        super.onDraw(canvas);
    }

}
