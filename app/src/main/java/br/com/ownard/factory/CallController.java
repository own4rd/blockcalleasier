package br.com.ownard.factory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.com.ownard.db.BlockCallDbHelper;
import br.com.ownard.db.PhoneTable;
import br.com.ownard.models.Contact;


public class CallController {
    private SQLiteDatabase db;
    private SQLiteOpenHelper dbHelper;

    public CallController(Context context) {
        dbHelper = new BlockCallDbHelper(context);
    }

    public boolean insert(String name, String phone){
        ContentValues values = new ContentValues();
        db = dbHelper.getWritableDatabase();

        values.put(PhoneTable.COLUMN_NAME_NAME, name);
        values.put(PhoneTable.COLUMN_NAME_PHONE, phone);
        values.put(PhoneTable.COLUMN_NAME_BLOCKEDS, 0);

        long count = db.insert(PhoneTable.TABLE_NAME, null, values);

        db.close();

        return count > 0;
    }

    public List<Contact> load(){
        Cursor cursor;
        List<Contact> contacts = new ArrayList<>();
        String[] fields = { PhoneTable._ID, PhoneTable.COLUMN_NAME_NAME, PhoneTable.COLUMN_NAME_PHONE, PhoneTable.COLUMN_NAME_BLOCKEDS };
        db = dbHelper.getReadableDatabase();

        cursor = db.query(PhoneTable.TABLE_NAME, fields, null, null, null, null, null, null);

        if(cursor == null){
            return contacts;
        }

        while(cursor.moveToNext()){
            Contact contact = new Contact(cursor.getString( cursor.getColumnIndex(PhoneTable.COLUMN_NAME_NAME) ),
                    cursor.getString( cursor.getColumnIndex(PhoneTable.COLUMN_NAME_PHONE) ),
                    cursor.getLong( cursor.getColumnIndex(PhoneTable.COLUMN_NAME_BLOCKEDS) ));
            contacts.add(contact);
        }

        db.close();

        return contacts;
    }

    public boolean checkPhone(String phone){
        Cursor cursor;
        String[] projection = {
                PhoneTable._ID
        };

        String selection = PhoneTable.COLUMN_NAME_PHONE + "= ?";
        String[] selectionArgs = { phone };

        db = dbHelper.getReadableDatabase();

        cursor = db.query(
                PhoneTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        boolean isPhone = cursor.getCount() > 0;

        db.close();

        return isPhone;

    }

    public boolean delete(String phone){
        db = dbHelper.getWritableDatabase();
        String selection = PhoneTable.COLUMN_NAME_PHONE + "= ?";
        String[] selectionArgs = { phone };

        long count = db.delete(
                PhoneTable.TABLE_NAME,
                selection,
                selectionArgs
        );

        return count > 0;
    }

    public void addBlockCount(String phone){
        Cursor cursor;
        String[] projection = {
                PhoneTable._ID,
                PhoneTable.COLUMN_NAME_BLOCKEDS
        };

        String selection = PhoneTable.COLUMN_NAME_PHONE + "= ?";
        String[] selectionArgs = { phone };

        db = dbHelper.getReadableDatabase();

        cursor = db.query(
                PhoneTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );



        if(cursor.moveToNext()){
            db = dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            long id = cursor.getLong(cursor.getColumnIndex(PhoneTable._ID));
            cv.put(PhoneTable.COLUMN_NAME_BLOCKEDS, cursor.getLong( cursor.getColumnIndex(PhoneTable.COLUMN_NAME_BLOCKEDS) ) + 1);
            db.update(PhoneTable.TABLE_NAME, cv, PhoneTable._ID + "=" + id, null);
        }

    }

    public List<Contact> search(String phone){
        Cursor cursor;
        List<Contact> contacts = new ArrayList<>();
        String[] fields = { PhoneTable._ID, PhoneTable.COLUMN_NAME_NAME, PhoneTable.COLUMN_NAME_PHONE, PhoneTable.COLUMN_NAME_BLOCKEDS };

        String selection = PhoneTable.COLUMN_NAME_PHONE + " like ? OR " + PhoneTable.COLUMN_NAME_NAME + " like ?";
        String[] selectionArgs = new String[]{"%" + phone + "%", "%" + phone + "%"};

        db = dbHelper.getReadableDatabase();

        cursor = db.query(
                PhoneTable.TABLE_NAME,
                fields,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if(cursor == null){
            return contacts;
        }

        while(cursor.moveToNext()){
            Contact contact = new Contact(cursor.getString( cursor.getColumnIndex(PhoneTable.COLUMN_NAME_NAME) ),
                    cursor.getString( cursor.getColumnIndex(PhoneTable.COLUMN_NAME_PHONE) ),
                    cursor.getLong( cursor.getColumnIndex(PhoneTable.COLUMN_NAME_BLOCKEDS) ));
            contacts.add(contact);
        }

        db.close();

        return contacts;
    }
}
