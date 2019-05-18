package com.example.android_calendar.DB;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android_calendar.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DBTest extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Main";
    int mYear, mMonth, mDay;
    TextView mTxtDate;


    long selectStart = 0;
    Button btnChangeDate;
    Button btn_Insert;
    EditText edit_String;

    long nowIndex;
    Long DAY;
    String STR;
    String sort = "DAY";

    ArrayAdapter<String> arrayAdapter;

    static ArrayList<String> arrayIndex =  new ArrayList<String>();
    static ArrayList<String> arrayData = new ArrayList<String>();
    private DbOpenHelper mDbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_schedule);
        btn_Insert = (Button) findViewById(R.id.button);
        btnChangeDate = (Button) findViewById(R.id.btnchangedate);
        btnChangeDate.setOnClickListener(this);
        btn_Insert.setOnClickListener(this);
        edit_String = (EditText) findViewById(R.id.edit_string);
        Calendar cal = new GregorianCalendar();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
        mTxtDate = (TextView)findViewById(R.id.txtdate);
        DAY = new Date().getTime();

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        ListView listView = (ListView) findViewById(R.id.db_list_view);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(onClickListener);

        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();
        mDbOpenHelper.create();

        showDatabase(sort);

        btn_Insert.setEnabled(true);
        UpdateNow();
    }

    public void setInsertMode(){
        mTxtDate.setText("");
        edit_String.setText("");
        btn_Insert.setEnabled(true);
    }
    void UpdateNow(){
        mTxtDate.setText(String.format("%d/%d/%d", mYear, mMonth + 1, mDay));
    }
    private AdapterView.OnItemClickListener onClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.e("On Click", "position = " + position);
            nowIndex = Long.parseLong(arrayIndex.get(position));
            Log.e("On Click", "nowIndex = " + nowIndex);
            Log.e("On Click", "Data: " + arrayData.get(position));
            String[] tempData = arrayData.get(position).split("\\s+");
            Log.e("On Click", "Split Result = " + tempData);
            mTxtDate.setText(tempData[0].trim());
            edit_String.setText(tempData[1].trim());
            btn_Insert.setEnabled(false);
        }
    };

//    private AdapterView.OnItemLongClickListener longClickListener = new AdapterView.OnItemLongClickListener() {
////        @Override
//        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//            Log.d("Long Click", "position = " + position);
//            nowIndex = Long.parseLong(arrayIndex.get(position));
//            String[] nowData = arrayData.get(position).split("\\s+");
//            String viewData = nowData[0] + ", " + nowData[1] + ", " + nowData[2] + ", " + nowData[3];
//            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//            dialog.setTitle("데이터 삭제")
//                    .setMessage("해당 데이터를 삭제 하시겠습니까?" + "\n" + viewData)
//                    .setPositiveButton("네", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Toast.makeText(this, "데이터를 삭제했습니다.", Toast.LENGTH_SHORT).show();
//                            mDbOpenHelper.deleteColumn(nowIndex);
//                            showDatabase(sort);
//                            setInsertMode();
//                        }
//                    })
//                    .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Toast.makeText(this, "삭제를 취소했습니다.", Toast.LENGTH_SHORT).show();
//                            setInsertMode();
//                        }
//                    })
//                    .create()
//                    .show();
//            return false;
//        }
//    };

    public void showDatabase(String sort){
//        Cursor iCursor = mDbOpenHelper.sortColumn(sort);
        Cursor iCursor = mDbOpenHelper.selectstart(0,999999999999999L);
        Log.d("showDatabase", "DB Size: " + iCursor.getCount());
        arrayData.clear();
        arrayIndex.clear();
        while(iCursor.moveToNext()){
            String tempIndex = iCursor.getString(iCursor.getColumnIndex("_id"));
            String tempID = iCursor.getString(iCursor.getColumnIndex("myday"));
            tempID = setTextLength(tempID,10);
            String tempName = iCursor.getString(iCursor.getColumnIndex("str"));
            String Result = tempID + tempName;
            arrayData.add(Result);
            arrayIndex.add(tempIndex);
        }
        arrayAdapter.clear();
        arrayAdapter.addAll(arrayData);
        arrayAdapter.notifyDataSetChanged();
    }

    public String setTextLength(String text, int length){
        if(text.length()<length){
            int gap = length - text.length();
            for (int i=0; i<gap; i++){
                text = text + " ";
            }
        }
        return text;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                STR = edit_String.getText().toString();
                mDbOpenHelper.open();
                mDbOpenHelper.insertColumn(DAY, STR);
                showDatabase(sort);
                setInsertMode();
//                edit_Day.requestFocus();
//                edit_Day.setCursorVisible(true);
                break;
            case R.id.btnchangedate :
                Log.i("시간입력","누름");
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
