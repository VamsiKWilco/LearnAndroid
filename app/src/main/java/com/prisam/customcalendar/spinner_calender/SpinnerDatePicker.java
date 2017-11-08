package com.prisam.customcalendar.spinner_calender;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.prisam.customcalendar.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by vamsi on 10/5/2017.
 */

public class SpinnerDatePicker extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spinner_main);

        // Get the widgets reference from XML layout
        final RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
        final TextView tv = (TextView) findViewById(R.id.tv);
        DatePicker dp = (DatePicker) findViewById(R.id.dp);

        // Get a new Calendar instance
        Calendar calendar = Calendar.getInstance();
        // Get the Calendar current year, month and day of month
        int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        /*
            init(int year, int monthOfYear, int dayOfMonth,
                DatePicker.OnDateChangedListener onDateChangedListener)

            Initialize the state of DatePicker.
         */
        dp.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Do something when the date changed from date picker object

                // Create a Date variable/object with user chosen date
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(0);
                cal.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                Date chosenDate = cal.getTime();

                // Format the date
                DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
                String formattedDate = df.format(chosenDate);

                tv.setText("Your selected date is\n");
                tv.setText(String.format("%s%s", tv.getText(), formattedDate));
            }
        });
    }
}
