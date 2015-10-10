package com.example.jaimejahuey.bikes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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


    //Columns for for the inventory table
    public static final String COLUMN_ID = "id";
    //public static final String KEY_MODEL = "model";
    public static final String KEY_make= "make";
    //public static final String KEY_quantity = "quantity";
    public static final String KEY_SERIALCODE = "serial";
    public static final String KEY_COLOR= "color";
    public static final String KEY_CONDITION= "condition";


    //Columns for the repairs table


    //Column for the sales Tables

    public MySQLiteHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    //Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //Creating inventory table
        String CREATE_INVENTORY_TABLE = "CREATE TABLE " + TABLE_inventory + " ( " + COLUMN_ID + " integer primary key,"
         + KEY_SERIALCODE+ " TEXT,"+ KEY_make + " TEXT," + KEY_COLOR + " TEXT, "
         + KEY_CONDITION + " TEXT" + ")";

        //Create the other table


        //create other table;



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



    //To be able to add a bike to the database
    public boolean addBike(Bike bike)
    {
        //To be able to write to database
        SQLiteDatabase DB = this.getWritableDatabase();

        //CHeck to see if this make and model of the bike doesn't already exist.
        //String Query = "Select * from " + MySQLiteHelper.TABLE_inventory + " where " + MySQLiteHelper.KEY_MODEL + "=" + bike.getInventory_make();
        //Cursor cursor = DB.rawQuery(Query, null);

        Cursor cursor = null;
        String sql = "SELECT * FROM inventory WHERE serial = '" + bike.getInventory_serial() + "'" ;
        cursor = DB.rawQuery(sql, null);

        //If the cursor is less then 0 then its not in the table yet
        if(cursor.getCount()<=0)
        {

            ContentValues values = new ContentValues();
            values.put(MySQLiteHelper.KEY_make, bike.getInventory_make());
            values.put(MySQLiteHelper.KEY_COLOR, bike.getInventory_color());
            values.put(MySQLiteHelper.KEY_CONDITION, bike.getInventory_condition());
            values.put(MySQLiteHelper.KEY_SERIALCODE, bike.getInventory_make());



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


    //To be able to delete a bike
    public boolean deleteBike(Bike bike)
    {
        //To be able to read from the databse
        SQLiteDatabase DB = this.getReadableDatabase();



        DB.close();
        return false;
    }

    //To list the bikes and models and the quanity of bikes.

    //Other functions for the remainder of the project

}


