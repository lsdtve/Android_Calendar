package com.example.android_calendar.fragment;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.android_calendar.DB.DbOpenHelper;
import com.example.android_calendar.DB.DbSupport;
import com.example.android_calendar.R;
import com.example.android_calendar.databinding.DayFragmentBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DayFragment  extends Fragment implements DbSupport,View.OnClickListener {
    public static final String DATE_FORMAT = "yyyy년 MM월 dd일";
    public long selectTimestart;
    private DayFragmentBinding binding;
    private DbOpenHelper mDbOpenHelper;
    ArrayAdapter<String> arrayAdapter;
    SimpleDateFormat df;
    static ArrayList<String> arrayIndex = new ArrayList<String>();
    static ArrayList<String> arrayData = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.day_fragment, container, false);
        return binding.getRoot();

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        selectTimestart = new Date().getTime();
        selectTimestart -= selectTimestart%(1000*60*60*24);
        arrayAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1);
        ListView listView = binding.dbListView;
        listView.setAdapter(arrayAdapter);
        df = new SimpleDateFormat(DATE_FORMAT);
        binding.leftButton.setOnClickListener(this);
        binding.rightButton.setOnClickListener(this);

        mDbOpenHelper = new DbOpenHelper(this.getContext());
        mDbOpenHelper.open();
        mDbOpenHelper.create();
        showDatabase();
    }
    public void showDatabase() {
        Cursor iCursor = mDbOpenHelper.selectstart(selectTimestart, selectTimestart+(1000*60*60*24));
        arrayData.clear();
        arrayIndex.clear();

        while (iCursor.moveToNext()) {
            String tempIndex = iCursor.getString(iCursor.getColumnIndex("_id"));
            String schedule = iCursor.getString(iCursor.getColumnIndex("schedule"));
            String Result =  "일정\n" + schedule;
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
        binding.textView.setText(df.format(selectTimestart));
    }
    @Override
    public void refresh() {
        showDatabase();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.leftButton:
                selectTimestart -= 24*60*60*1000;
                showDatabase();
                break;
            case R.id.rightButton:
                selectTimestart += 24*60*60*1000;
                showDatabase();
                break;
        }
    }
}
