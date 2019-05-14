package com.example.android_calendar;

import android.databinding.DataBindingUtil;
import android.support.annotation.IdRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.android_calendar.databinding.ActivityMainBinding;
import com.example.android_calendar.fragment.DayFragment;
import com.example.android_calendar.fragment.MonthFragment;
import com.example.android_calendar.fragment.WeekFragment;
import com.example.android_calendar.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity implements TabLayout.BaseOnTabSelectedListener {

    ActivityMainBinding binding;

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Log.i("tab", tab.getText().toString());
        switch (tab.getPosition()){
            case 0:
                replace(R.id.main_fragment, new MonthFragment(),"month");
                break;
            case 1:
                replace(R.id.main_fragment, new WeekFragment(),"week");
                break;
            case 2:
                replace(R.id.main_fragment, new DayFragment(),"day");
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setVm(new MainViewModel());
        binding.calendarTable.addOnTabSelectedListener(this);
        replace(R.id.main_fragment, new MonthFragment(),"month");
    }


    private void replace(@IdRes int frameId, Fragment fragment, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(frameId, fragment, tag)
                .commit();
    }

}

