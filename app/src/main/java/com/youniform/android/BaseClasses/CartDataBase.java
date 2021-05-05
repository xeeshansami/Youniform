package com.youniform.android.BaseClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.youniform.android.Model.CartModel;

import java.util.ArrayList;

public class CartDataBase {
    myDbHelper myhelper;

    public CartDataBase(Context context) {
        myhelper = new myDbHelper(context);
    }

    public long insertData(int ProdID, int catId, String NAME, String MyPrice, String MyImage, int MyQUANTITY, String Size, String MyColor) {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.ProdID, ProdID);
        contentValues.put(myDbHelper.CatId, catId);
        contentValues.put(myDbHelper.NAME, NAME);
        contentValues.put(myDbHelper.MyPrice, MyPrice);
        contentValues.put(myDbHelper.MyImage, MyImage);
        contentValues.put(myDbHelper.COLOR, MyColor);
        contentValues.put(myDbHelper.Size, Size);
        contentValues.put(myDbHelper.MyQUANTITY, MyQUANTITY);
        long id = dbb.insert(myDbHelper.TABLE_NAME, null, contentValues);
        return id;
    }

    public ArrayList<CartModel> getData() {
        ArrayList<CartModel> list = new ArrayList<>();
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.UID, myDbHelper.ProdID, myDbHelper.CatId, myDbHelper.NAME, myDbHelper.MyPrice, myDbHelper.MyImage, myDbHelper.MyQUANTITY, myDbHelper.COLOR, myDbHelper.Size};
        Cursor cursor = db.query(myDbHelper.TABLE_NAME, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int cid = cursor.getInt(cursor.getColumnIndex(myDbHelper.UID));
            int ProdID = cursor.getInt(cursor.getColumnIndex(myDbHelper.ProdID));
            int CatId = cursor.getInt(cursor.getColumnIndex(myDbHelper.CatId));
            int MyQUANTITY = cursor.getInt(cursor.getColumnIndex(myDbHelper.MyQUANTITY));
            String name = cursor.getString(cursor.getColumnIndex(myDbHelper.NAME));
            String MyPrice = cursor.getString(cursor.getColumnIndex(myDbHelper.MyPrice));
            String MyImage = cursor.getString(cursor.getColumnIndex(myDbHelper.MyImage));
            String MySize = cursor.getString(cursor.getColumnIndex(myDbHelper.Size));
            String MyColor = cursor.getString(cursor.getColumnIndex(myDbHelper.COLOR));
            CartModel obj = new CartModel(cid, ProdID, CatId, MyQUANTITY, name, MyPrice, MyImage, MyColor, MySize);
            list.add(obj);
        }
        return list;
    }

    public void delete(String id) {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        db.delete(myDbHelper.TABLE_NAME, "_id = ?", new String[]{id});
    }

    public void update(String id, int newQuantity) {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.MyQUANTITY, newQuantity);
        db.update(myDbHelper.TABLE_NAME, contentValues, "_id = ?", new String[]{id});
    }

    public void deleteAll() {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        db.execSQL("delete from " + myDbHelper.TABLE_NAME);
        db.close();
    }

    public void Update(String id, int quantitiy, String Size) {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("Quantity", quantitiy);

        String strSQL = "UPDATE myTable SET Quantity = '" + quantitiy + "' WHERE Producdid = '" + id + "' AND Size = '" + Size + "'";

        db.execSQL(strSQL);

        //  db.update(myDbHelper.TABLE_NAME, cv, "Producdid = ?", new String[]{id});
        db.close();
    }

    public static class myDbHelper extends SQLiteOpenHelper {
        public static final String TABLE_NAME = "myTable";   // Table Name
        private static final String DATABASE_NAME = "carterDatabase";    // Database Name
        private static final int DATABASE_Version = 1;   // Database Version
        private static final String UID = "_id";     // Column I (Primary Key)
        private static final String ProdID = "Producdid";
        private static final String CatId = "CatId";
        private static final String NAME = "Name";    //Column II
        private static final String MyPrice = "Price";    // Column III
        private static final String MyImage = "Image";    // Column IIIb
        private static final String MyQUANTITY = "Quantity";
        private static final String Size = "Size";
        private static final String COLOR = "Color";
        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ProdID + " INTEGER, " + CatId + " INTEGER, " + NAME + " VARCHAR(255) ," +
                "" + MyPrice + " VARCHAR(225)," + MyQUANTITY + " INTEGER," + MyImage + " VARCHAR(225) ," + Size + " VARCHAR(225)," + COLOR + " VARCHAR(225) );";
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
