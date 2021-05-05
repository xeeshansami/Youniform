package com.youniform.android.BaseClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.youniform.android.Model.WishListModel;

import java.util.ArrayList;

public class WishListDataBase {
    myDbHelper myhelper;

    public WishListDataBase(Context context) {
        myhelper = new myDbHelper(context);
    }

    public long insertData(String Product) {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.Product, Product);
        long id = dbb.insert(myDbHelper.TABLE_NAME, null, contentValues);
        return id;
    }

    public ArrayList<WishListModel> getData() {
        ArrayList<WishListModel> list = new ArrayList<>();
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.UID, myDbHelper.Product};
        Cursor cursor = db.query(myDbHelper.TABLE_NAME, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int cid = cursor.getInt(cursor.getColumnIndex(myDbHelper.UID));
            String Product = cursor.getString(cursor.getColumnIndex(myDbHelper.Product));
            WishListModel obj = new WishListModel(cid, Product);
            list.add(obj);
        }
        return list;
    }

    public void delete(String id) {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        db.delete(myDbHelper.TABLE_NAME, "_id = ?", new String[]{id});
    }


    static class myDbHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "wishlistDatabase";    // Database Name
        private static final String TABLE_NAME = "myTable";   // Table Name
        private static final int DATABASE_Version = 1;   // Database Version
        private static final String UID = "_id";     // Column I (Primary Key)
        private static final String Product = "Product";
        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Product + " );";
        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        private Context context;

        public myDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Version);
            this.context = context;
        }

        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE);
            } catch (Exception e) {
                Message.message(context, "" + e);
            }
        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                Message.message(context, "OnUpgrade");
                db.execSQL(DROP_TABLE);
                onCreate(db);
            } catch (Exception e) {
                Message.message(context, "" + e);
            }
        }
    }

}
