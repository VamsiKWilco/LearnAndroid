package com.prisam.customcalendar.animations;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;

import com.prisam.customcalendar.R;
import com.prisam.customcalendar.custom_components.PizzaView;

/**
 * Created by vamsi on 11/7/2017.
 */

public class AnimationActivity extends AppCompatActivity implements Animation.AnimationListener{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        PizzaView pizzaView =  findViewById(R.id.pizzaView);

        pizzaView.setBackground(ContextCompat.getDrawable(this,R.drawable.rounded_red_border));


        /*http://www.gadgetsaint.com/tips/rounded-corners-views-layouts-android/#.WgF-kFuCziw*/
    }


    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
