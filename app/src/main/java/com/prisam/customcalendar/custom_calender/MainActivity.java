package com.prisam.customcalendar.custom_calender;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.prisam.customcalendar.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    CalendarView calendar_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendar_view = (CalendarView) findViewById(R.id.calendar_view);

        calendar_view.setEventHandler(new CalendarView.EventHandler() {
            @Override
            public void onDayLongPress(Date date) {
                // show returned day
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
//                Toast.makeText(MyHabitDetailActivity.this, df.format(date), Toast.LENGTH_SHORT).show();
                Log.v("Click", " : " + df.format(date));
            }

            @Override
            public void onDayPress(Date date) {
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Log.v("Click", " : " + df.format(date));
            }

            @Override
            public void setEvents() {
//                calendar_view.updateCalendar(null, null);
            }
        });
    }
}
