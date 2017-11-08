package com.prisam.customcalendar.custom_calender;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.prisam.customcalendar.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

/**
 * Created by VamsyK on 28/01/2017.
 */
public class CalendarView extends LinearLayout {

    //region variables
    private static final String LOGTAG = "Calendar View";
    private static final int DAYS_COUNT = 42;
    private static final String DATE_FORMAT = "MMMM";
    private Calendar currentDate = Calendar.getInstance();
    private Calendar now = Calendar.getInstance();
    private EventHandler eventHandler = null;

    int _maxDate = 0;
    int _maxMonth = 0;
    int _maxYear = 0;

    int _minMonth = 0;
    int _minYear = 1950;

    int mYear = 0;
    int pYear = 0;

    Date selectedDate;

    private List<String> yearList;
    String displayedposition = "";
    int _width = 0;

    //region adapters
    HorizontalAdapter horizontalAdapter;


    //region Views
    private ImageView btnPrev;
    private ImageView btnNext;

    /*private ImageView btnNextYear;
    private ImageView btnPrevYear;

    private ImageView btnPrevFiveYear;
    private ImageView btnNextFiveYears;*/

    private TextView txtDate;
    private GridView grid;

    private RecyclerView horizontal_recycler_view;
    //endregion

    //region constructor
    public CalendarView(Context context) {
        super(context);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context, attrs);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context, attrs);
    }

    //endregion

    //region LocalMethos
    private void initControl(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.control_calendar, this);

        _maxDate = now.get(Calendar.DAY_OF_MONTH);
        _maxMonth = now.get(Calendar.MONTH);
        _maxYear = now.get(Calendar.YEAR) - 18;

        if (now.get(Calendar.YEAR) == (currentDate.get(Calendar.YEAR)))
            currentDate.set((currentDate.get(Calendar.YEAR) - 18), currentDate.get(Calendar.MONTH), (currentDate.get(Calendar.DAY_OF_MONTH)));

        displayedposition = String.valueOf(_maxYear);
        _width = getDeviceWidth(context);

        assignUiElements();
        assignClickHandlers();

        updateCalendar();
    }

    private void assignUiElements() {
        // layout is inflated, assign local variables to components
        btnNext = (ImageView) findViewById(R.id.calendar_next_button);
        btnPrev = (ImageView) findViewById(R.id.calendar_prev_button);

        /*btnNextYear = (ImageView) findViewById(R.id.calendar_next_button_year);
        btnPrevYear = (ImageView) findViewById(R.id.calendar_prev_button_year);

        btnPrevFiveYear = (ImageView) findViewById(R.id.prev_five_years);
        btnNextFiveYears = (ImageView) findViewById(R.id.next_five_years);*/

        txtDate = (TextView) findViewById(R.id.calendar_date_display);
        grid = (GridView) findViewById(R.id.calendar_grid);

        /*horizontal year view*/
        horizontal_recycler_view = (RecyclerView) findViewById(R.id.yearList);

        yearList = fill_with_data();
        horizontalAdapter = new HorizontalAdapter(yearList, getContext());
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_view.setLayoutManager(horizontalLayoutManager);
        horizontal_recycler_view.setAdapter(horizontalAdapter);
        horizontal_recycler_view.smoothScrollToPosition(getYearPos(""));

    }

    private void assignClickHandlers() {
        // add one month and refresh UI
        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((currentDate.get(Calendar.YEAR)) != _maxYear) {

                    currentDate.add(Calendar.MONTH, 1);

                    int cY = currentDate.get(Calendar.YEAR);
                    if (!displayedposition.equalsIgnoreCase(String.valueOf(cY))) {

                        displayedposition = String.valueOf(cY);
                        LinearLayoutManager llm = (LinearLayoutManager) horizontal_recycler_view.getLayoutManager();

                        llm.scrollToPositionWithOffset((getYearPos(String.valueOf(cY)) - 2), yearList.size());
                        horizontalAdapter.notifyDataSetChanged();
                    }

                    updateCalendar();
                    eventHandler.setEvents();
                } else {
                    if ((currentDate.get(Calendar.MONTH)) < _maxMonth) {
                        currentDate.add(Calendar.MONTH, 1);
                        updateCalendar();
                        eventHandler.setEvents();
                    } else
                        Toast.makeText(getContext(), "Max Date reached", Toast.LENGTH_LONG).show();
                }
            }
        });

        // subtract one month and refresh UI
        btnPrev.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((currentDate.get(Calendar.YEAR)) != _minYear) {
                    currentDate.add(Calendar.MONTH, -1);
                    int cY = currentDate.get(Calendar.YEAR);
                    if (!displayedposition.equalsIgnoreCase(String.valueOf(cY))) {

                        displayedposition = String.valueOf(cY);
                        LinearLayoutManager llm = (LinearLayoutManager) horizontal_recycler_view.getLayoutManager();

                        llm.scrollToPositionWithOffset((getYearPos(String.valueOf(cY)) - 2), yearList.size());
                        horizontalAdapter.notifyDataSetChanged();
                    }
                    updateCalendar();

                    eventHandler.setEvents();
                } else {
                    int kv = currentDate.get(Calendar.MONTH) - _minMonth;
                    if (kv != 0) {
                        currentDate.add(Calendar.MONTH, -1);
                        updateCalendar();
                        eventHandler.setEvents();
                    } else
                        Toast.makeText(getContext(), "Min Date reached", Toast.LENGTH_LONG).show();
                }
            }
        });

        /*// add one year and refresh UI
        btnNextYear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pYear = 12;
                increaseYear(pYear);
            }
        });

        // subtract one year and refresh UI
        btnPrevYear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pYear = 12;
                decreaseYear(pYear);
            }
        });

        // add one year and refresh UI
        btnNextFiveYears.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mYear = 60;
                increaseYear(mYear);
            }
        });

        // subtract one year and refresh UI
        btnPrevFiveYear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pYear = 60;
                decreaseYear(pYear);
            }
        });*/

        // long-pressing a day
        grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> view, View cell, int position, long id) {
                // handle long-press
                if (eventHandler == null)
                    return false;

                eventHandler.onDayLongPress((Date) view.getItemAtPosition(position));
                return true;
            }
        });

        // Just click a day
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // handle long-press
                if (eventHandler != null) {
                    selectedDate = (Date) parent.getItemAtPosition(position);

                    int y = currentDate.get(Calendar.YEAR);
                    if ((selectedDate.getDate() > _maxDate) && (_maxMonth == selectedDate.getMonth()) && (y == _maxYear)) {
                        ((TextView) view).setTextColor(Color.GRAY);
                        selectedDate = null;
                    } else if ((selectedDate.getDate() == _maxDate) && (_maxMonth == selectedDate.getMonth()) && (y == _maxYear)) {
                        Toast.makeText(getContext(), "Age must be greater than 18 years.", Toast.LENGTH_LONG).show();
                        selectedDate = null;
                    } else {
                        eventHandler.onDayPress((Date) parent.getItemAtPosition(position));
                        updateCalendar();
                    }
                }
            }
        });
    }

    /* Increase 1 or 5 years*/
    public void increaseYear(int val) {

        if (((currentDate.get(Calendar.YEAR)) + (val / 12) <= _maxYear)) {

            if (((currentDate.get(Calendar.YEAR) + (val / 12)) != _maxYear)) {
                currentDate.add(Calendar.MONTH, val);
                updateCalendar();
                eventHandler.setEvents();
            } else {
                int cv = 0;

                int cm = currentDate.get(Calendar.MONTH);

                if (cm == _maxMonth) {
                    cv = 0;
                } else if (cm > _maxMonth) {
                    cv = cm - _maxMonth;
                } else {
                    cv = 0;
                }

                val = val - cv;
                currentDate.add(Calendar.MONTH, val);
                updateCalendar();
                eventHandler.setEvents();
            }

        } else {
            int cv = _maxYear - currentDate.get(Calendar.YEAR);

            if (cv == 0) {
                Toast.makeText(getContext(), "Max Date reached", Toast.LENGTH_LONG).show();
            } else {

                cv = cv * 12;
                int kv = currentDate.get(Calendar.MONTH) - _maxMonth;
                cv = cv - kv;
                currentDate.add(Calendar.MONTH, cv);
                updateCalendar();
                eventHandler.setEvents();
                Toast.makeText(getContext(), "Max Date reached", Toast.LENGTH_LONG).show();
            }
        }
    }

    /* Decrease 1 or 5 years*/
    public void decreaseYear(int val) {

        if (((currentDate.get(Calendar.YEAR)) - (val / 12) >= _minYear)) {

            if ((currentDate.get(Calendar.YEAR) - (val / 12)) != _minYear) {
                currentDate.add(Calendar.MONTH, -val);
                updateCalendar();
                eventHandler.setEvents();

            } else {
                currentDate.add(Calendar.MONTH, -val);
                updateCalendar();
                eventHandler.setEvents();
            }
        } else {
            int cv = currentDate.get(Calendar.YEAR) - _minYear;

            if (cv == 0) {
                int cvv = currentDate.get(Calendar.MONTH) - _minMonth;
                if (cvv != 0) {
                    currentDate.add(Calendar.MONTH, -cvv);
                    updateCalendar();
                    eventHandler.setEvents();
                }
                Toast.makeText(getContext(), "Min Date reached", Toast.LENGTH_LONG).show();
            } else {
                cv = cv * 12;
                int kv = currentDate.get(Calendar.MONTH) - _minMonth;
                cv = cv + kv;
                currentDate.add(Calendar.MONTH, -cv);
                updateCalendar();
                eventHandler.setEvents();
                Toast.makeText(getContext(), "Min Date reached", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void updateCalendar() {
        updateCalendar(null, null);
    }

    public void setEventHandler(EventHandler eventHandler) {
        try {
            this.eventHandler = eventHandler;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //region UPDATE CALENDAR
    public void updateCalendar(HashSet<Date> events, Calendar _currentDate) {
        try {
            ArrayList<Date> cells = new ArrayList<>();

            Calendar calendar = (Calendar) currentDate.clone();

            // determine the cell for current month's beginning
            calendar.set(Calendar.DAY_OF_MONTH, 1);

            int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;

            // move calendar backwards to the beginning of the week
            calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

            // fill cells
            while (cells.size() < DAYS_COUNT) {
                cells.add(calendar.getTime());
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            // update grid
            grid.setAdapter(new CalendarAdapter(getContext(), cells, events));

            // update title
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

            txtDate.setText(sdf.format(currentDate.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region calendar adapter
    private class CalendarAdapter extends ArrayAdapter<Date> {
        private HashSet<Date> eventDays;
        private LayoutInflater inflater;

        public CalendarAdapter(Context context, ArrayList<Date> days, HashSet<Date> eventDays) {
            super(context, R.layout.control_calendar_day, days);
            try {
                this.eventDays = eventDays;
                inflater = LayoutInflater.from(context);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @NonNull
        @Override
        public View getView(int position, View view, ViewGroup parent) {

            try {
                // day in question
                Date date = getItem(position);
                int day = date.getDate();
                int month = date.getMonth();
                int year = date.getYear();

                // inflate item if it does not exist yet
                if (view == null)
                    view = inflater.inflate(R.layout.control_calendar_day, parent, false);

                // if this day has an event, specify event image
                view.setBackgroundResource(0);
                // set text
                ((TextView) view).setText(String.valueOf(date.getDate()));

                // clear styling
//                ((TextView) view).setTypeface(null, Typeface.NORMAL);
                //((TextView) view).setTextColor(Color.WHITE);

                if (month != getItem(15).getMonth()) {
                    // if this day is outside current month, then Invisible
                    ((TextView) view).setVisibility(INVISIBLE);
                } else {
                    int y = currentDate.get(Calendar.YEAR);
                    if ((day > _maxDate) && (_maxMonth == getItem(15).getMonth()) && (y == _maxYear)) {
                        ((TextView) view).setTextColor(Color.GRAY);
                    }
                }

                if (selectedDate != null)
                    if (selectedDate.toString().contains(date.toString())) {
                        ((TextView) view).setTypeface(null, Typeface.BOLD);
                        ((TextView) view).setTextColor(Color.BLACK);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            ((TextView) view).setBackground(getResources().getDrawable(R.drawable.bg_circle));
                        }
                    }

                return view;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return view;
        }
    }

    /*new horizontal year view data*/
    public List<String> fill_with_data() {
        List<String> data = new ArrayList<>();
        for (int y = _minYear; y <= _maxYear; y++) {
            data.add("" + y);
        }
        return data;
    }

    /*new horizontal year view adapter*/
    public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {

        List<String> horizontalList = Collections.emptyList();
        Context context;

        public HorizontalAdapter(List<String> horizontalList, Context context) {
            this.horizontalList = horizontalList;
            this.context = context;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView yearTV;

            public MyViewHolder(View view) {
                super(view);
                yearTV = (TextView) view.findViewById(R.id.yearTV);

            }
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.control_calendar_year, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            holder.yearTV.setText(horizontalList.get(position));

            if (horizontalList.get(position).equalsIgnoreCase(displayedposition)) {
                holder.yearTV.setTextColor(Color.parseColor("#abedd8"));
            } else {
                holder.yearTV.setTextColor(Color.WHITE);
            }

            holder.yearTV.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    String list = horizontalList.get(position);
//                    Toast.makeText(getContext(), list, Toast.LENGTH_SHORT).show();

                    LinearLayoutManager llm = (LinearLayoutManager) horizontal_recycler_view.getLayoutManager();

                    llm.scrollToPositionWithOffset((position - 2), yearList.size());

                    displayedposition = list;
                    horizontalAdapter.notifyDataSetChanged();

                    holder.yearTV.setTextColor(Color.parseColor("#abedd8"));
                    holder.yearTV.setTypeface(null, Typeface.BOLD);

                    int currentYear = currentDate.get(Calendar.YEAR);

                    int selectedYear = Integer.parseInt(list);

                    int _months = (currentYear - selectedYear);

                    _months = _months * 12;

                    if (currentYear <= selectedYear) {
                        if (_months < 0)
                            _months = -_months;

                        increaseYear(_months);
                    } else {
                        decreaseYear(_months);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return horizontalList.size();
        }
    }

    public static int getDeviceWidth(Context context) {

        DisplayMetrics displayMetrics = new DisplayMetrics();

        WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);

        return (Math.round(displayMetrics.widthPixels / displayMetrics.density)) / 5;
    }

    public int getYearPos(String yearChan) {

        for (int i = 0; i < yearList.size(); i++) {

            if (yearList.get(i).equalsIgnoreCase(yearChan)) {
                return i;
            }
        }

        return yearList.size();
    }
    //endregion

    //region Interface
    public interface EventHandler {
        void onDayLongPress(Date date);

        void onDayPress(Date date);

        void setEvents();
    }
    //endregion
}
