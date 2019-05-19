package com.example.android_calendar.fragment;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.android_calendar.DB.DbOpenHelper;
import com.example.android_calendar.DB.DbSupport;
import com.example.android_calendar.R;
import com.example.android_calendar.databinding.WeekFragmentBinding;
import com.example.android_calendar.decorator.OneDayDecorator;
import com.example.android_calendar.decorator.SaturdayDecorator;
import com.example.android_calendar.decorator.SundayDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class WeekFragment extends Fragment implements OnMonthChangedListener, DbSupport {
    public static final String DATE_FORMAT = "yyyy년 MM월 dd일";
    public long selectTimestart,selectTimeend;
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    private WeekFragmentBinding binding;
    private DbOpenHelper mDbOpenHelper;
    static ArrayList<String> arrayIndex = new ArrayList<String>();
    static ArrayList<String> arrayData = new ArrayList<String>();
    SimpleDateFormat df;
    ArrayAdapter<String> arrayAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.week_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        arrayAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1);
        ListView listView = binding.dbListView;
        listView.setAdapter(arrayAdapter);
        df = new SimpleDateFormat(DATE_FORMAT);
        mDbOpenHelper = new DbOpenHelper(this.getContext());
        mDbOpenHelper.open();
        mDbOpenHelper.create();

        MaterialCalendarView materialCalendarView = (MaterialCalendarView) binding.calendarView;
        materialCalendarView.setOnMonthChangedListener(this);
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(1900, 4, 3))
                .setMaximumDate(CalendarDay.from(2100, 12, 31))
                .setCalendarDisplayMode(CalendarMode.WEEKS)

                .commit();
        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                oneDayDecorator);
    }

    public void showDatabase() {
        Cursor iCursor = mDbOpenHelper.selectstart(selectTimestart, selectTimeend);
        Log.d("showDatabase", "DB Size: " + iCursor.getCount());
        arrayData.clear();
        arrayIndex.clear();
        while (iCursor.moveToNext()) {
            String tempIndex = iCursor.getString(iCursor.getColumnIndex("_id"));
            Long day = iCursor.getLong(iCursor.getColumnIndex("myday"));
            String schedule = iCursor.getString(iCursor.getColumnIndex("schedule"));
            String Result = "요일 : " + df.format(day) + "\n일정 : " + schedule;
            arrayData.add(Result);
            arrayIndex.add(tempIndex);
        }
        if(arrayData.size()==0){
            arrayData.add("일정이없습니다.");
            arrayIndex.add("1");
        }
        arrayAdapter.clear();
        arrayAdapter.addAll(arrayData);
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        selectTimestart = date.getCalendar().getTimeInMillis();
        selectTimeend = selectTimestart+(7*24*60*60*1000);
        Log.i(df.format(selectTimestart),df.format(selectTimeend));
        showDatabase();
    }

    @Override
    public void refresh() {
        showDatabase();
    }
}
