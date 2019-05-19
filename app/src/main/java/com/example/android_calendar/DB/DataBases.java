package com.example.android_calendar.DB;

import android.provider.BaseColumns;

public final class DataBases {

    public static final class CreateDB implements BaseColumns {
        public static final String DAY = "myday";
        public static final String STR = "schedule";
        public static final String _TABLENAME0 = "scheduletable";
        public static final String _CREATE0 = "create table if not exists "+_TABLENAME0+"("
                +_ID+" integer primary key autoincrement, "
                +DAY+" long not null , "
                +STR+" text not null );";
    }
}