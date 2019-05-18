package com.example.android_calendar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.annotation.IdRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android_calendar.DB.DBTest;
import com.example.android_calendar.databinding.ActivityMainBinding;
import com.example.android_calendar.fragment.DayFragment;
import com.example.android_calendar.fragment.MonthFragment;
import com.example.android_calendar.fragment.WeekFragment;
import com.example.android_calendar.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity implements TabLayout.BaseOnTabSelectedListener {
    SharedPreferences pref;

    ActivityMainBinding binding;

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt("Position",tab.getPosition());
        edit.commit();
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
        setSupportActionBar(binding.toolbar);
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        binding.calendarTable.getTabAt(pref.getInt("Position",0)).select();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_memoadd:
                Toast.makeText(getApplicationContext(), "일정 추가버튼", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, DBTest.class);
                startActivity(intent);
                return true;

            default:
                Toast.makeText(getApplicationContext(), "일정 삭제버튼", Toast.LENGTH_LONG).show();
                return super.onOptionsItemSelected(item);

        }
    }

    private void replace(@IdRes int frameId, Fragment fragment, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(frameId, fragment, tag)
                .commit();
    }

}

