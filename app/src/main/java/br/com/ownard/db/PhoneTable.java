package br.com.ownard.db;

import android.provider.BaseColumns;

public class PhoneTable implements BaseColumns {
    public static final String TABLE_NAME = "phones";
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_PHONE = "phone";
    public static final String COLUMN_NAME_BLOCKEDS = "blockeds";

    public static final String CREATE_QUERY = "create table " + TABLE_NAME + " (" +
            PhoneTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME_NAME + " TEXT, " +
            COLUMN_NAME_PHONE + " TEXT, " +
            COLUMN_NAME_BLOCKEDS + " INTEGER)";


    public static final String DROP_QUERY = "drop table " + TABLE_NAME;
    public static final String SElECT_QUERY = "select * from " + TABLE_NAME;
}
