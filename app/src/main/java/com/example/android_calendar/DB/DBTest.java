package com.example.android_calendar.DB;

import android.app.DatePickerDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import com.example.android_calendar.R;
import com.example.android_calendar.databinding.CreateScheduleBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DBTest extends AppCompatActivity implements View.OnClickListener {
    CreateScheduleBinding binding;
    int mYear, mMonth, mDay;
    SimpleDateFormat df;

    Long DAY;
    String STR;

    ArrayAdapter<String> arrayAdapter;

    static ArrayList<String> arrayIndex =  new ArrayList<String>();
    static ArrayList<String> arrayData = new ArrayList<String>();
    private DbOpenHelper mDbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_schedule);
        binding = DataBindingUtil.setContentView(this, R.layout.create_schedule);
        setSupportActionBar(binding.toolbar);

        binding.btnchangedate.setOnClickListener(this);
        binding.saveButton.setOnClickListener(this);
        Calendar cal = new GregorianCalendar();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
        df = new SimpleDateFormat("yyyy년 MM월 dd일");
        DAY = new Date().getTime();
        DAY = DAY-(DAY%(1000 * 60 * 1440));

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();
        mDbOpenHelper.create();


        binding.saveButton.setEnabled(true);
        UpdateNow();
    }

    public void setInsertMode(){
        binding.txtdate.setText("");
        binding.editString.setText("");
        binding.saveButton.setEnabled(true);
    }
    void UpdateNow(){
        binding.txtdate.setText(df.format(DAY));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_button:
                STR = binding.editString.getText().toString();
                Log.i("Insert"," "+DAY+ " "+STR);
                mDbOpenHelper.open();
                mDbOpenHelper.insertColumn(DAY, STR);
                setInsertMode();
                break;
            case R.id.btnchangedate :
                new DatePickerDialog(this, mDateSetListener, mYear,
                    mMonth, mDay).show();
                break;
        }
    }
    DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    Calendar cal = Calendar.getInstance();
                    cal.set(year,monthOfYear,dayOfMonth);
                    DAY =  cal.getTimeInMillis();
                    UpdateNow();
                }
            };
}
