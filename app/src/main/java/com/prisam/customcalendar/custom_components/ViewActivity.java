package com.prisam.customcalendar.custom_components;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.prisam.customcalendar.R;

/**
 * Created by vamsi on 10/31/2017.
 */

public class ViewActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Java code*/
//       CTextView cTextView = new CTextView(this);
        setContentView(R.layout.activity_custom);
    }

    public void pizzaClick(View view) throws InterruptedException {

        RotateAnimation rotate = new RotateAnimation(
                0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );

        rotate.setDuration(10000);
        rotate.setRepeatCount(Animation.INFINITE);
        view.startAnimation(rotate);


    }
}
