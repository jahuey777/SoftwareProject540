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
    public static final String TABLE_repairs = "repairs";
    public static final String TABLE_sales = "sales";


    //Columns for for the inventory table
    public static final String COLUMN_ID = "id";
    //public static final String KEY_MODEL = "model";
    public static final String KEY_make= "make";
    //public static final String KEY_quantity = "quantity";
    public static final String KEY_SERIALCODE = "serial";
    public static final String KEY_COLOR= "color";
    public static final String KEY_CONDITION= "condition";

    //*********** David added this*************//
    //Columns for the repairs table
    public static final String CUST_NAME = "customer_name";
    public static final String CUST_PHONE = "customer_phone";
    public static final String DUE_DATE = "due_date";

    //Columns for the sales table
    //public static final String SERIAL_NUM = "serial";
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
        //Creating the table we need for our database
        String CREATE_INVENTORY_TABLE = "CREATE TABLE " + TABLE_inventory + " ( " + COLUMN_ID + " integer primary key,"
         + KEY_SERIALCODE+ " TEXT,"+ KEY_make + " TEXT," + KEY_COLOR + " TEXT, "
         + KEY_CONDITION + " TEXT" + ")";

        String CREATE_REPAIRS_TABLE = "CREATE TABLE " + TABLE_repairs + "( " + COLUMN_ID + "integer primary key,"
        + CUST_NAME + " TEXT, " + CUST_PHONE + " TEXT," + DUE_DATE + " TEXT)";

        String CREATE_SALES_TABLE = "CREATE TABLE " + TABLE_sales + "( " + COLUMN_ID + "integer primary key,"
                + KEY_SERIALCODE + " TEXT, " + SALE_DATE + " TEXT," + SALE_PRICE + " TEXT)";



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
            values.put(MySQLiteHelper.KEY_SERIALCODE, bike.getInventory_serial());



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
    public boolean deleteBike(String bikeSerial)
    {
        //To be able to read from the database
        SQLiteDatabase DB = this.getWritableDatabase();

        //Using a cursor to see if the bikeSerial exists
        //If it does, then getCount will atleast be 1. In our case, it should be exactly 1
        Cursor cursor= null;
        String sql = "SELECT * FROM inventory WHERE serial = '" + bikeSerial + "'" ;
        cursor = DB.rawQuery(sql,null);

        if(cursor.getCount()<=0)
        {
            cursor.close();
            DB.close();
            return false;
        }
        else
        {
            //Delete is an already created function, the parameters are listed as the following:
            //tablename, where statement and make sure you use ?, the value of what you are deleting
            DB.delete(TABLE_inventory, KEY_SERIALCODE + "= ? ", new String[] {String.valueOf(bikeSerial)} );
            cursor.close();
            DB.close();
            return true;
        }

    }

    public boolean existsOrNot(String bikeSerial)
    {
        //To be able to read from the database
        SQLiteDatabase DB = this.getWritableDatabase();

        //Using a cursor to see if the bikeSerial exists
        //If it does, then getCount will atleast be 1. In our case, it should be exactly 1
        Cursor cursor= null;
        String sql = "SELECT * FROM inventory WHERE serial = '" + bikeSerial + "'" ;
        cursor = DB.rawQuery(sql,null);

        //If the bike exits, we return true, otherwise return false
        if(cursor.getCount()<=0)
        {
            cursor.close();
            DB.close();
            return false;
        }
        else
        {
            cursor.close();
            DB.close();
            return true;
        }
    }
    //To list the bikes and models and the quantity of bikes.

    //Other functions for the remainder of the project

    //reading the inventory database
public Cursor readEntry(){

    String[] allColumns =
            new String[]{ MySQLiteHelper.KEY_SERIALCODE, MySQLiteHelper.KEY_make, MySQLiteHelper.KEY_COLOR, MySQLiteHelper.KEY_CONDITION };
    Cursor c = getWritableDatabase().query(MySQLiteHelper.TABLE_inventory, allColumns,null, null, null, null, null);
    if(c!=null){
        c.moveToFirst();
    }
    return c;
}
}


