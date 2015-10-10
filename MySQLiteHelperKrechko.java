package com.example.jaimejahuey.espinasbikes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.lang.Override;
import java.lang.String;

/**
 * Created by jaimejahuey on 9/30/15.
 */
public class MySQLiteHelper extends SQLiteOpenHelper
{

    //All static variables
    //Database Version

    private static final int DATABASE_VERSION= 1;

    //Database name
    private static final String DATABASE_NAME = "BikeManagement";

    //Names of our tables
    public static final String TABLE_inventory= "inventory";
    public static final String TABLE_repairs = "repairs";
    public static final String TABLE_sales = "sales"; 

    //Columns for the inventory table
    public static final String COLUMN_ID = "id";
    public static final String KEY_MODEL = "model";
    public static final String KEY_make= "make";
    public static final String KEY_quantity = "quantity";

    //*********** David added this*************//
    //Columns for the repairs table
    public static final String CUST_NAME = "customer_name";
    public static final String CUST_PHONE = "customer_phone";
    public static final String DUE_DATE = "due_date";

    //Columns for the sales table
    public static final String SERIAL_NUM = "serial";
    public static final String SALE_DATE = "sale_date"; // Date of sale
    public static final String SALE_PRICE = "sale_price";
    //*********** David added this*************//

    public MySQLiteHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    //Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_INVENTORY_TABLE = "CREATE TABLE " + TABLE_inventory + " ( " + COLUMN_ID + " integer primary key,"
                + KEY_make + " TEXT," + KEY_MODEL + " TEXT," + KEY_quantity + " INTEGER" + ")";

        db.execSQL(CREATE_INVENTORY_TABLE);

    }

    //This will upgrade the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //Drop older table if it exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_inventory);

        //Create tables again
        onCreate(db);
    }


    public boolean addBike(Bike bike)
    {
        //To be able to write to database
        SQLiteDatabase DB = this.getWritableDatabase();

        //CHeck to see if this make and model of the bike doesn't already exist.
        //String Query = "Select * from " + MySQLiteHelper.TABLE_inventory + " where " + MySQLiteHelper.KEY_MODEL + "=" + bike.getInventory_make();
        //Cursor cursor = DB.rawQuery(Query, null);

        Cursor cursor = null;
        String sql = "SELECT * FROM inventory WHERE model = '" + bike.getInventory_model() + "'";
        cursor = DB.rawQuery(sql, null);

        //If the cursor is less then 0 then its not in the table yet
        if(cursor.getCount()<=0)
        {

            ContentValues values = new ContentValues();
            values.put(MySQLiteHelper.KEY_make, bike.getInventory_make());
            values.put(MySQLiteHelper.KEY_MODEL, bike.getInventory_model());
            values.put(MySQLiteHelper.KEY_quantity, bike.getInventory_quantity());

            DB.insert(MySQLiteHelper.TABLE_inventory, null, values);

            cursor.close();
            DB.close();
            return true;
        }

        else
        {
            cursor.close();
            DB.close();
            return false;
        }
    }



}


