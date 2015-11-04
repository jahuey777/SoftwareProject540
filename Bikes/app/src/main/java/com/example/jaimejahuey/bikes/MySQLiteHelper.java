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
    public static final String COLUMN_ID_INVENTORY = "idI";
    public static final String KEY_make= "make";
    public static final String KEY_SERIALCODE = "serial";
    public static final String KEY_COLOR= "color";
    public static final String KEY_CONDITION= "condition";
    public static final String KEY_AVAILABLE = "available";

    //Columns for the repairs table
    public static final String COLUMN_ID_REPAIRS= "idP";
    public static final String CUST_NAME = "customerName";
    public static final String CUST_PHONE = "customerPhone";
    public static final String REPAIR_DUE_DATE = "dueDate";
    public static final String STATUS_BIT = "status";
    public static final String COST_REPAIR = "costOfRepair";
    public static final String AMOUNT_CHARGED= "amountCharged";
    public static final String DATE_COMPLETED= "dateCompleted";
    public static final String REPAIR_SERIAL = "serial";

    //Columns for the sales table
    //public static final String SERIAL_NUM = "serial";
    public static final String COLUMN_ID_SALES = "idS";
    public static final String SALES_FKEY = "idINum";
    public static final String SALE_DATE = "saleDate"; // Date of sale
    public static final String SALE_PRICE = "salePrice";
    

    public MySQLiteHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    //Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //Creating the table we need for our database
        String CREATE_INVENTORY_TABLE = "CREATE TABLE " + TABLE_inventory + " ( " + COLUMN_ID_INVENTORY
         + " integer primary key," + KEY_SERIALCODE+ " TEXT, "+ KEY_make + " TEXT," + KEY_COLOR + " TEXT, "
         + KEY_CONDITION + " TEXT," + KEY_AVAILABLE + " INTEGER)";

        String CREATE_REPAIRS_TABLE = "CREATE TABLE " + TABLE_repairs + "( " + COLUMN_ID_REPAIRS
        + " integer primary key, " + REPAIR_SERIAL + " TEXT, "+ CUST_NAME + " TEXT, " + CUST_PHONE + " TEXT," + REPAIR_DUE_DATE +
                " TEXT," + COST_REPAIR+ " TEXT," + AMOUNT_CHARGED + " TEXT," + DATE_COMPLETED
                + " TEXT,"+ STATUS_BIT + " INTEGER)";

        String CREATE_SALES_TABLE = "CREATE TABLE " + TABLE_sales + "( " + COLUMN_ID_SALES + " integer primary key,"
        + KEY_SERIALCODE + " TEXT, " + SALE_DATE + " TEXT," + SALE_PRICE + " TEXT," + SALES_FKEY
        + " INTEGER, "+ " FOREIGN KEY (" + SALES_FKEY +") REFERENCES " + TABLE_inventory + "(" + COLUMN_ID_INVENTORY + "))";

        db.execSQL(CREATE_INVENTORY_TABLE);
        db.execSQL(CREATE_REPAIRS_TABLE);
        db.execSQL(CREATE_SALES_TABLE);

    }

    //This will upgrade the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //Drop older table if it exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_inventory);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_repairs);



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
            values.put(MySQLiteHelper.KEY_AVAILABLE, 1);


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


    //Bike will not actually be deleted from the database, just modified to not be available.
    public boolean removeBike(String bikeSerial)
    {
        //To be able to read from the database
        SQLiteDatabase DB = this.getWritableDatabase();

        //Using a cursor to see if the bikeSerial exists
        //If it does, then getCount will atleast be 1. In our case, it should be exactly 1
        Cursor cursor= null;
        String sql = "SELECT * FROM inventory WHERE serial = '" + bikeSerial + "' and available =" + 1;
        cursor = DB.rawQuery(sql,null);

        if(cursor.getCount()<=0)
        {
            cursor.close();
            DB.close();
            return false;
        }
        else
        {
            //**** IGNORE THIS COMMENT, but leave for future reference.
            //Delete is an already created function, the parameters are listed as the following:
            //tablename, where statement and make sure you use ?, the value of what you are deleting
            //DB.delete(TABLE_inventory, KEY_SERIALCODE + "= ? ", new String[] {String.valueOf(bikeSerial)} );


            //We don't actually want to delete the bike, so we created a new column available
            //0 means its not, and 1 means its still in inventory.
            ContentValues newVal= new ContentValues();

            newVal.put(KEY_AVAILABLE,0);
            DB.update(TABLE_inventory, newVal, "serial = '" + bikeSerial + "'", null);


            cursor.close();
            DB.close();
            return true;
        }

    }

    //To check if bike serial already exists.
    public boolean existsOrNot(String bikeSerial)
    {
        //To be able to read from the database
        SQLiteDatabase DB = this.getWritableDatabase();

        //Using a cursor to see if the bikeSerial exists
        //If it does, then getCount will atleast be 1. In our case, it should be exactly 1
        //also put single quotes around the string you are searching for. In this case bikeSerial
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
    public Cursor readInventoryEntry()
    {

        String[] allColumns =
            new String[]{ MySQLiteHelper.KEY_AVAILABLE, MySQLiteHelper.KEY_SERIALCODE, MySQLiteHelper.KEY_make, MySQLiteHelper.KEY_COLOR, MySQLiteHelper.KEY_CONDITION,};
//has KEY_AVAILABLE first to make sure its available before adding it to table
        Cursor c = getWritableDatabase().query(MySQLiteHelper.TABLE_inventory, allColumns,null, null, null, null, null);

        if(c!=null)
        {
        c.moveToFirst();
         }
        return c;
    }

    public Cursor readActiveRepairEntry()
    {

        String[] allColumns =
                new String[]{ MySQLiteHelper.STATUS_BIT, MySQLiteHelper.CUST_PHONE, MySQLiteHelper.REPAIR_DUE_DATE, MySQLiteHelper.REPAIR_SERIAL};
//has KEY_AVAILABLE first to make sure its available before adding it to table
        Cursor c = getWritableDatabase().query(MySQLiteHelper.TABLE_repairs, allColumns,null, null, null, null, null);

        if(c!=null)
        {
            c.moveToFirst();
        }
        return c;
    }
    public Cursor readCompletedRepairEntry()
    {

        String[] allColumns =
                new String[]{ MySQLiteHelper.STATUS_BIT, MySQLiteHelper.DATE_COMPLETED, MySQLiteHelper.COST_REPAIR, MySQLiteHelper.REPAIR_SERIAL};
//has KEY_AVAILABLE first to make sure its available before adding it to table
        Cursor c = getWritableDatabase().query(MySQLiteHelper.TABLE_repairs, allColumns,null, null, null, null, null);

        if(c!=null)
        {
            c.moveToFirst();
        }
        return c;
    }

    //method for updating a bike.
    //The integer will tell us what attribute the user is wanting to update
    //1 means color, 2 means make, and 3 means condition
    public void updateBike(int choice, String serial, String updateValue)
    {




    }

    public boolean addRepair(String serial, String phoneNum, String custName, String dueDate)
    {

        //To be able to write to database
        SQLiteDatabase DB = this.getWritableDatabase();

        //To make sure that the serial does not already exist.
        //Make sure you put quotes around the string. In this case serial
        Cursor cursor = null;
        String sql = "SELECT * FROM repairs WHERE serial = '" + serial + "'" ;

        cursor = DB.rawQuery(sql, null);

        //If the cursor is less then 0 then its not in the table yet
        if(cursor.getCount()<=0)
        {

            ContentValues values = new ContentValues();

            //Putting the values to each column.
            values.put(MySQLiteHelper.REPAIR_SERIAL, "" + serial);
            values.put(MySQLiteHelper.CUST_PHONE, "" + phoneNum);
            values.put(MySQLiteHelper.CUST_NAME, "" + custName);
            values.put(MySQLiteHelper.REPAIR_DUE_DATE,"" + dueDate);
            values.put(MySQLiteHelper.STATUS_BIT, 1);
            //values.put(MySQLiteHelper.COST_REPAIR,"hey");
           // values.put(MySQLiteHelper.AMOUNT_CHARGED, "hey");
            //values.put(MySQLiteHelper.REPAIR_DUE_DATE, "hi");


            DB.insert(MySQLiteHelper.TABLE_repairs, null, values);

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

    public boolean repairExists (String serial)
    {
        //To be able to read from the database
        SQLiteDatabase DB = this.getWritableDatabase();

        //Using a cursor to see if the bikeSerial exists
        //If it does, then getCount will atleast be 1. In our case, it should be exactly 1
        //also put single quotes around the string you are searching for. In this case bikeSerial
        Cursor cursor= null;
        String sql = "SELECT * FROM repairs WHERE serial = '" + serial + "' and status = " + 1 ;
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

    public boolean completedRepair(String serialCompleted, String costToOwner , String chargedCustomer, String completedDate)
    {
        //To be able to read from the database
        SQLiteDatabase DB = this.getWritableDatabase();

        //Using a cursor to see if the bikeSerial exists
        //If it does, then getCount will atleast be 1. In our case, it should be exactly 1
        Cursor cursor= null;
        String sql = "SELECT * FROM repairs WHERE serial = '" + serialCompleted + "' and status = " + 1;
        cursor = DB.rawQuery(sql,null);

        if(cursor.getCount()<=0)
        {
            cursor.close();
            DB.close();
            return false;
        }
        else
        {
            //**** IGNORE THIS COMMENT, but leave for future reference.
            //Delete is an already created function, the parameters are listed as the following:
            //tablename, where statement and make sure you use ?, the value of what you are deleting
            //DB.delete(TABLE_inventory, KEY_SERIALCODE + "= ? ", new String[] {String.valueOf(bikeSerial)} );


            //We don't actually want to delete the bike, so we created a new column available
            //0 means its not, and 1 means its still in inventory.
            ContentValues newVal= new ContentValues();

            newVal.put(STATUS_BIT,0);
            newVal.put(REPAIR_DUE_DATE, completedDate);
            newVal.put(AMOUNT_CHARGED, chargedCustomer);
            newVal.put(COST_REPAIR, costToOwner);
            DB.update(TABLE_repairs, newVal, "serial = '" + serialCompleted + "'", null);


            cursor.close();
            DB.close();
            return true;
        }


    }

    public boolean addingSale(String serialNum, String saleDate, String salePrice)
    {
        //To be able to write to database
        SQLiteDatabase DB = this.getWritableDatabase();

        //To make sure that the serial does not already exist.
        //Make sure you put quotes around the string. In this case serial
        Cursor cursor = null;
        String sql = "SELECT * FROM inventory WHERE serial = '" + serialNum + "'" ;

        cursor = DB.rawQuery(sql, null);

        //If the cursor is less then 0 then its not in the table yet
        if(cursor.getCount()<=0)
        {
            cursor.close();
            DB.close();
            return false;
        }

        else
        {
            ContentValues values = new ContentValues();

            //Putting the values to each column.
            values.put(MySQLiteHelper.SALES_FKEY, "" + serialNum);
            values.put(MySQLiteHelper.SALE_DATE, "" + saleDate);
            values.put(MySQLiteHelper.SALE_PRICE, "" + salePrice);

            DB.insert(MySQLiteHelper.TABLE_sales, null, values);

            cursor.close();
            DB.close();
            return true;
        }
    }

}


