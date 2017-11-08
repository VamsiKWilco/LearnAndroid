package com.prisam.customcalendar.custom_components;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.prisam.customcalendar.R;

/**
 * Created by vamsi on 10/31/2017.
 */

public class LengthPickerLinearLayout extends LinearLayout {

    private static final String KEY_SUPER_STATE = "superState";
    private static final String KEY_NUM_INCHES = "numInches";

    private CTextView cTextView;
    private CButton mButton, pButton;

    private int mNumInches = 0;

    public LengthPickerLinearLayout(Context context) {
        super(context);
        init();
    }

    public LengthPickerLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LengthPickerLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LengthPickerLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_SUPER_STATE, super.onSaveInstanceState());
        bundle.putInt(KEY_NUM_INCHES, mNumInches);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mNumInches = bundle.getInt(KEY_NUM_INCHES);
            super.onRestoreInstanceState(bundle.getParcelable(KEY_SUPER_STATE));
        } else {
            super.onRestoreInstanceState(state);
        }

        updateControls();
    }

    // similar to /*onCreate*/
    private void init() {

        // similar to /*setContentView*/
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.length_view, this);

        /*Xml code*/
        cTextView = (CTextView) findViewById(R.id.c_tv);
        mButton = (CButton) findViewById(R.id.minus_btn);
        pButton = (CButton) findViewById(R.id.plus_btn);

        updateControls();

        View.OnClickListener listener = view -> {
            switch (view.getId()) {
                case R.id.plus_btn:
                    mNumInches++;
                    updateControls();
                    break;
                case R.id.minus_btn:
                    if (mNumInches > 0) {
                        mNumInches--;
                        updateControls();
                    }
                    break;
            }
        };

        mButton.setOnClickListener(listener);
        pButton.setOnClickListener(listener);

        setOrientation(LinearLayout.VERTICAL);
    }


    private void updateControls() {
        int feet = mNumInches / 12;
        int inches = mNumInches % 12;

        String text = String.format("%d' %d\"", feet, inches);
        if (feet == 0) {
            text = String.format("%d\"", inches);
        } else {
            if (inches == 0) {
                text = String.format("%d'", feet);
            }
        }

        cTextView.setText(text);
        mButton.setEnabled(mNumInches > 0);
    }


    public int getNumInches() {
        return mNumInches;
    }
}
