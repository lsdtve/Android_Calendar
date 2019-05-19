package com.example.android_calendar.fragment;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android_calendar.DB.DbOpenHelper;
import com.example.android_calendar.DB.DbSupport;
import com.example.android_calendar.MainActivity;
import com.example.android_calendar.R;
import com.example.android_calendar.databinding.MonthFragmentBinding;
import com.example.android_calendar.decorator.EventDecorator;
import com.example.android_calendar.decorator.OneDayDecorator;
import com.example.android_calendar.decorator.SaturdayDecorator;
import com.example.android_calendar.decorator.SundayDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

public class MonthFragment extends Fragment implements OnMonthChangedListener, DbSupport {

    private DbOpenHelper mDbOpenHelper;
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    private MonthFragmentBinding binding;
    MaterialCalendarView materialCalendarView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.month_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDbOpenHelper = new DbOpenHelper(this.getContext());
        mDbOpenHelper.open();
        mDbOpenHelper.create();
        materialCalendarView = (MaterialCalendarView) binding.calendarView;
        materialCalendarView.setOnMonthChangedListener(this);

        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(1900, 4, 3))
                .setMaximumDate(CalendarDay.from(2100, 12, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)

                .commit();
        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                oneDayDecorator);

    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        new ApiSimulator().executeOnExecutor(Executors.newSingleThreadExecutor());
    }
    public void refresh() {
        new ApiSimulator().executeOnExecutor(Executors.newSingleThreadExecutor());
    }
    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {

            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();
            Cursor iCursor = mDbOpenHelper.selectall();

            while (iCursor.moveToNext()) {
                Long day = iCursor.getLong(iCursor.getColumnIndex("myday"));
                calendar.setTimeInMillis(day);
                CalendarDay cal = CalendarDay.from(calendar);
                dates.add(cal);
                Log.i("달에 내용",":"+cal.getDate());
            }

            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            materialCalendarView.addDecorator(new EventDecorator(R.color.red, calendarDays,(MainActivity)MainActivity.mContext));
        }
    }
}
