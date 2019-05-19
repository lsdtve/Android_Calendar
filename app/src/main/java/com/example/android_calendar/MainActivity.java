package com.example.android_calendar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.android_calendar.DB.CreateSchedule;
import com.example.android_calendar.DB.DbSupport;
import com.example.android_calendar.databinding.ActivityMainBinding;
import com.example.android_calendar.fragment.DayFragment;
import com.example.android_calendar.fragment.MonthFragment;
import com.example.android_calendar.fragment.WeekFragment;

public class MainActivity extends AppCompatActivity implements TabLayout.BaseOnTabSelectedListener {
    SharedPreferences pref;

    ActivityMainBinding binding;
    public static Context mContext;

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt("Position", tab.getPosition());
        edit.commit();
        switch (tab.getPosition()) {
            case 0:
                replace(R.id.main_fragment, new MonthFragment(), "month");
                break;
            case 1:
                replace(R.id.main_fragment, new WeekFragment(), "week");
                break;
            case 2:
                replace(R.id.main_fragment, new DayFragment(), "day");
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
        binding.calendarTable.addOnTabSelectedListener(this);
        replace(R.id.main_fragment, new MonthFragment(), "month");
        setSupportActionBar(binding.toolbar);
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        binding.calendarTable.getTabAt(pref.getInt("Position", 0)).select();
        mContext = this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ((DbSupport) getSupportFragmentManager().findFragmentById(R.id.main_fragment)).refresh();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scheduleadd:
                Intent intent = new Intent(this, CreateSchedule.class);
                startActivityForResult(intent, 300);
                return true;
            default:
                return true;
        }
    }

    private void replace(@IdRes int frameId, Fragment fragment, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(frameId, fragment, tag)
                .commit();
    }

}

