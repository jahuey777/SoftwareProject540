package com.example.jaimejahuey.bikes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by jaimejahuey on 9/30/15.
 * I edited this file, this sucks
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
    public static final String DELETED_BIT = "deleted";

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
    public static final String DELETEDREPAIR_BIT= "deleted";
    public static final String REPAIREMAIL = "email";

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
                + KEY_CONDITION + " TEXT," + KEY_AVAILABLE + " INTEGER, " + DELETED_BIT + " INTEGER )";

        String CREATE_REPAIRS_TABLE = "CREATE TABLE " + TABLE_repairs + "( " + COLUMN_ID_REPAIRS
                + " integer primary key, " + REPAIR_SERIAL + " TEXT, "+ CUST_NAME + " TEXT, " + CUST_PHONE + " TEXT," + REPAIR_DUE_DATE +
                " TEXT," + COST_REPAIR+ " TEXT," + AMOUNT_CHARGED + " TEXT," + DATE_COMPLETED
                + " TEXT,"+ STATUS_BIT + " INTEGER," + DELETEDREPAIR_BIT +" INTEGER, " + REPAIREMAIL + " TEXT)";

        String CREATE_SALES_TABLE = "CREATE TABLE " + TABLE_sales + "( " + COLUMN_ID_SALES + " integer primary key,"
                 + SALE_DATE + " TEXT," + SALE_PRICE + " TEXT," + SALES_FKEY
                + " TEXT, "+ " FOREIGN KEY (" + SALES_FKEY +") REFERENCES " + TABLE_inventory + "(" + COLUMN_ID_INVENTORY + "))";

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
        Cursor cursor2 = null;
        //Use first query to see if that serial even exists.
        //deleted, a 0 means its not (off) and 1 means it is (on)
        String sql = "SELECT * FROM inventory WHERE serial = '" + bike.getInventory_serial() + "' and deleted = " + 0 ;
        //Second query, if the serial exists, but if the deleted bit is 1, then we can still add it.
        String sql2 = "SELECT * FROM inventory WHERE serial = '" + bike.getInventory_serial() + "' and deleted = " + 1
                + " and available = " + 0;
        cursor = DB.rawQuery(sql, null);
        cursor2 = DB.rawQuery(sql2,null);

        //If the cursor is less then 0 then its not in the table yet
        //If the deleted bit is has a 1 or more, then we can re-add the serial number
        if(cursor.getCount()<=0 && cursor2.getCount() >=0)
        {

            ContentValues values = new ContentValues();
            values.put(MySQLiteHelper.KEY_make, bike.getInventory_make());
            values.put(MySQLiteHelper.KEY_COLOR, bike.getInventory_color());
            values.put(MySQLiteHelper.KEY_CONDITION, bike.getInventory_condition());
            values.put(MySQLiteHelper.KEY_SERIALCODE, bike.getInventory_serial());
            values.put(MySQLiteHelper.KEY_AVAILABLE, 1);
            values.put(MySQLiteHelper.DELETED_BIT,0);


            DB.insert(MySQLiteHelper.TABLE_inventory, null, values);

            cursor.close();
            cursor2.close();
            DB.close();
            return true;
        }

        else
        {
            cursor.close();
            cursor2.close();
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

            ContentValues newVal= new ContentValues();

            //set available to 0 since we don't want it to show in the show inventory list
            //set deleted to 1, that means we can add this serial again in the future. It's deleted,
            //but still exists in the database
            newVal.put(KEY_AVAILABLE,0);
            newVal.put(DELETED_BIT,1);

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
                new String[]{ MySQLiteHelper.STATUS_BIT, MySQLiteHelper.DELETEDREPAIR_BIT, MySQLiteHelper.CUST_PHONE, MySQLiteHelper.REPAIR_DUE_DATE, MySQLiteHelper.REPAIR_SERIAL};
        //has KEY_AVAILABLE first to make sure its available before adding it to table
        Cursor c = getWritableDatabase().query(MySQLiteHelper.TABLE_repairs, allColumns,null, null, null, null, null);

        if(c!=null)
        {
            c.moveToFirst();
        }
        return c;
    }

    //CHecking to see if there are any active repairs. So any with a deleted bit of 0 and
    //a status bit of 1
    public boolean checkActiveRepairs ()
    {
        //To be able to read from the database
        SQLiteDatabase DB = this.getWritableDatabase();

        Cursor cursor= null;
        String sql = "SELECT * FROM repairs WHERE deleted = " + 0 + " and status =" + 1;
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

    //Grabs the info for the completed repairs, and sends the cursor through.
    public Cursor readCompletedRepairEntry()
    {

        String[] allColumns =
                new String[]{ MySQLiteHelper.STATUS_BIT,MySQLiteHelper.DELETEDREPAIR_BIT, MySQLiteHelper.DATE_COMPLETED, MySQLiteHelper.COST_REPAIR, MySQLiteHelper.REPAIR_SERIAL};
        //has KEY_AVAILABLE first to make sure its available before adding it to table

        Cursor c = getWritableDatabase().query(MySQLiteHelper.TABLE_repairs, allColumns,null, null, null, null, null);

        if(c!=null)
        {
            c.moveToFirst();
        }

        return c;
    }

    //CHecking to see if there are any active repairs. So any with a deleted bit of 0 and
    //a status bit of 1
    public boolean checkCompletedRepairs ()
    {
        //To be able to read from the database
        SQLiteDatabase DB = this.getWritableDatabase();

        Cursor cursor= null;
        String sql = "SELECT * FROM repairs WHERE deleted = " + 0 + " and status =" + 0;
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


    public Cursor readSoldBikesEntry()
    {
        String[] allColumns =
                new String[]{ MySQLiteHelper.SALE_DATE, MySQLiteHelper.SALE_PRICE, MySQLiteHelper.SALES_FKEY};
        //has KEY_AVAILABLE first to make sure its available before adding it to table

        Cursor c = getWritableDatabase().query(MySQLiteHelper.TABLE_sales, allColumns,null, null, null, null, null);

        if(c!=null)
        {
            c.moveToFirst();
        }

        return c;
    }

    public boolean addRepair(String serial, String phoneNum, String custName, String dueDate, String email)
    {

        //To be able to write to database
        SQLiteDatabase DB = this.getWritableDatabase();

        //To make sure that the serial does not already exist.
        //Make sure you put quotes around the string. In this case serial
        Cursor cursor = null;
        Cursor cursor2= null;
        String sql = "SELECT * FROM repairs WHERE serial = '" + serial + "' and deleted = " + 0 ;
        String sql2 = "SELECT * FROM repairs WHERE serial = '" + serial + "' and deleted=" + 1;
        cursor = DB.rawQuery(sql, null);
        cursor2= DB.rawQuery(sql2,null);

        //If the cursor is less then 0 then its not in the table yet
        if(cursor.getCount()<=0 && cursor2.getCount()>=0)
        {

            ContentValues values = new ContentValues();

            //Putting the values to each column.
            values.put(MySQLiteHelper.REPAIR_SERIAL, "" + serial);
            values.put(MySQLiteHelper.CUST_PHONE, "" + phoneNum);
            values.put(MySQLiteHelper.CUST_NAME, "" + custName);
            values.put(MySQLiteHelper.REPAIR_DUE_DATE,"" + dueDate);
            values.put(MySQLiteHelper.STATUS_BIT, 1);
            values.put(MySQLiteHelper.DELETEDREPAIR_BIT, 0);
            values.put(MySQLiteHelper.REPAIREMAIL, ""+ email);


            DB.insert(MySQLiteHelper.TABLE_repairs, null, values);

            cursor.close();
            cursor2.close();
            DB.close();
            return true;
        }

        else
        {
            cursor.close();
            cursor2.close();
            DB.close();
            return false;
        }
    }

    //repair will not actually be deleted from the database, a bit is just set to 1 which means its deleted.
    public boolean removeRepair(String bikeSerial)
    {
        //To be able to read from the database
        SQLiteDatabase DB = this.getWritableDatabase();

        //Using a cursor to see if the bikeSerial exists
        Cursor cursor= null;
        String sql = "SELECT * FROM repairs WHERE serial = '" + bikeSerial +"' and deleted = " + 0;
        cursor = DB.rawQuery(sql,null);

        if(cursor.getCount()<=0)
        {
            cursor.close();
            DB.close();
            return false;
        }
        else
        {
            //We don't actually want to delete the repair, so we created a new column deleted
            //0 means its not, and 1 means it is deleted.
            ContentValues newVal= new ContentValues();

            //set available to 0 since we don't want it to show in the show inventory list
            //set deleted to 1, that means we can add this serial again in the future. It's deleted,
            //but still exists in the database
            //also set status to -1, this means that it won't show on completed or active repairs.
            newVal.put(DELETEDREPAIR_BIT,1);
            newVal.put(STATUS_BIT,-1);

            DB.update(TABLE_repairs, newVal, "serial = '" + bikeSerial + "'", null);


            cursor.close();
            DB.close();
            return true;
        }

    }

    //To check if a repair exists or not.
    public boolean repairExists (String serial)
    {
        //To be able to read from the database
        SQLiteDatabase DB = this.getWritableDatabase();

        //Using a cursor to see if the bikeSerial exists
        //If it does, then getCount will atleast be 1. In our case, it should be exactly 1
        //also put single quotes around the string you are searching for. In this case bikeSerial
        Cursor cursor= null;
        String sql = "SELECT * FROM repairs WHERE serial = '" + serial + "' and deleted = " + 0 ;
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

    //Method for when the user has an active repair that is now completed.
    //Updates the values for that repair in the database
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
            newVal.put(DATE_COMPLETED, completedDate);
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
        String sql = "SELECT * FROM inventory WHERE serial = '" + serialNum + "' and available = " + 1 +
                " and deleted = " + 0;

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

            //Updating the availability bit to 0 in the inventory table.
            ContentValues newVal= new ContentValues();
            newVal.put(KEY_AVAILABLE, 0);

            DB.update(TABLE_inventory, newVal, "serial = '" + serialNum + "'", null);

            cursor.close();
            DB.close();
            return true;
        }
    }

    public void bikeProfit(String start, String end, double [] profitSum)
    {
        double profitAmount=0;
        double amountChargedRepairs =0;
        double costAmountRepairs =0;
        double bikeSalesTotal=0;

        //To be able to write to database
        SQLiteDatabase DB = this.getWritableDatabase();

        //To make sure that the serial does not already exist.
        //Make sure you put quotes around the string. In this case serial
        Cursor cursor = null;
        Cursor cursor2 = null;
        String sql = "SELECT salePrice, saleDate FROM sales";
        String slq2 = "SELECT costOfRepair, amountCharged, dateCompleted From repairs Where status = " + 0;

        cursor = DB.rawQuery(sql, null);
        cursor2= DB.rawQuery(slq2,null);

        //If the cursor is less then 0 then its not in the table yet
        if(cursor.getCount()<=0 && cursor2.getCount()<=0)
        {
            profitAmount = 0;
            cursor.close();
            DB.close();
        }
        else
        {

            //Breaking the dates into the day, month, and year.
            //So parsing the start date and end date that the user chose.
            int startDay = Integer.parseInt(start.substring(0,2));
            int startMonth = Integer.parseInt(start.substring(3,5));
            int startYear = Integer.parseInt(start.substring(6,10));

            int endDay= Integer.parseInt(end.substring(0,2));
            int endMonth = Integer.parseInt(end.substring(3,5));
            int endYear = Integer.parseInt(end.substring(6,10));


            //This moves the cursor to the first one, so get this one and then into the while loop.
            if(cursor.getCount()>0)
            {
                //Doesn't make sense to check for null since we are in the if statement
                //but the program crashes for some reason if we do not check
                if (cursor != null)
                {
                    cursor.moveToFirst();

                    //Check the date, if it falls between the previous 2 dates then we grab the sale price as well
                    String date = cursor.getString(1);

                    //Parse through the actual date that is grabbed from the database
                    int day = Integer.parseInt(date.substring(0, 2));
                    int month = Integer.parseInt(date.substring(3, 5));
                    int year = Integer.parseInt(date.substring(6, 10));


                    //add to profit if the date from the database falls between the other 2 dates
                    //So rest of the code is just logic to make sure it falls in those 2 dates
                    if (startYear < year && endYear > year)
                    {
                        double bikePrice = Double.parseDouble(cursor.getString(cursor.getColumnIndex(SALE_PRICE)));
                        bikeSalesTotal += Double.parseDouble(cursor.getString(cursor.getColumnIndex(SALE_PRICE)));
                        profitAmount += bikePrice * .2;
                    }
                    else if (startYear < year && endYear >= year)
                    {
                        if (endMonth > month && endDay >= day)
                        {
                            double bikePrice = Double.parseDouble(cursor.getString(cursor.getColumnIndex(SALE_PRICE)));
                            bikeSalesTotal += Double.parseDouble(cursor.getString(cursor.getColumnIndex(SALE_PRICE)));
                            profitAmount += bikePrice * .2;
                        }
                    }
                    else if (startYear == year && endYear >= year)
                    {
                        if (endYear > year && month > startMonth)
                        {
                            double bikePrice = Double.parseDouble(cursor.getString(cursor.getColumnIndex(SALE_PRICE)));
                            bikeSalesTotal += Double.parseDouble(cursor.getString(cursor.getColumnIndex(SALE_PRICE)));
                            profitAmount += bikePrice * .2;
                        }
                        else if (endYear > year && month == startMonth && startDay <= day)
                        {
                            double bikePrice = Double.parseDouble(cursor.getString(cursor.getColumnIndex(SALE_PRICE)));
                            bikeSalesTotal += Double.parseDouble(cursor.getString(cursor.getColumnIndex(SALE_PRICE)));
                            profitAmount += bikePrice * .2;
                        }
                        else if (endYear == year)
                        {
                            if (startMonth <= month && month <= endMonth && startDay <= day && endDay >= day)
                            {
                                double bikePrice = Double.parseDouble(cursor.getString(cursor.getColumnIndex(SALE_PRICE)));
                                bikeSalesTotal += Double.parseDouble(cursor.getString(cursor.getColumnIndex(SALE_PRICE)));
                                profitAmount += bikePrice * .2;
                            }
                        }
                    }

                    //If theres more then one sold bike then we move through those.
                    //If theres only one sold bike then this is skipped.
                    while (cursor.moveToNext())
                    {
                        date = cursor.getString(1);
                        day = Integer.parseInt(date.substring(0, 2));
                        month = Integer.parseInt(date.substring(3, 5));
                        year = Integer.parseInt(date.substring(6, 10));

                        if (startYear < year && endYear > year)
                        {
                            double bikePrice = Double.parseDouble(cursor.getString(cursor.getColumnIndex(SALE_PRICE)));
                            bikeSalesTotal += Double.parseDouble(cursor.getString(cursor.getColumnIndex(SALE_PRICE)));
                            profitAmount += bikePrice * .2;
                        }
                        else if (startYear < year && endYear >= year)
                        {
                            if (endMonth > month && endDay >= day)
                            {
                                double bikePrice = Double.parseDouble(cursor.getString(cursor.getColumnIndex(SALE_PRICE)));
                                bikeSalesTotal += Double.parseDouble(cursor.getString(cursor.getColumnIndex(SALE_PRICE)));
                                profitAmount += bikePrice * .2;
                            }
                        }
                        else if (startYear == year && endYear >= year)
                        {
                            if (endYear > year && month > startMonth)
                            {
                                double bikePrice = Double.parseDouble(cursor.getString(cursor.getColumnIndex(SALE_PRICE)));
                                bikeSalesTotal += Double.parseDouble(cursor.getString(cursor.getColumnIndex(SALE_PRICE)));
                                profitAmount += bikePrice * .2;
                            }
                            else if (endYear > year && month == startMonth && startDay <= day)
                            {
                                double bikePrice = Double.parseDouble(cursor.getString(cursor.getColumnIndex(SALE_PRICE)));
                                bikeSalesTotal += Double.parseDouble(cursor.getString(cursor.getColumnIndex(SALE_PRICE)));
                                profitAmount += bikePrice * .2;
                            }
                            else if (endYear == year)
                            {
                                if (startMonth <= month && month <= endMonth && startDay <= day && endDay >= day)
                                {
                                    double bikePrice = Double.parseDouble(cursor.getString(cursor.getColumnIndex(SALE_PRICE)));
                                    bikeSalesTotal += Double.parseDouble(cursor.getString(cursor.getColumnIndex(SALE_PRICE)));
                                    profitAmount += bikePrice * .2;
                                }
                            }
                        }
                    }

                }

            }

            if(cursor2.getCount()>0)
            {
                if (cursor2 != null)
                {
                    cursor2.moveToFirst();

                    //Check the date, if it falls between the previous 2 dates then we grab the sale price as well
                    String date = cursor2.getString(2);

                    //profitAmount = Double.parseDouble(date);
                    int day = Integer.parseInt(date.substring(0, 2));
                    int month = Integer.parseInt(date.substring(3, 5));
                    int year = Integer.parseInt(date.substring(6, 10));


                    //add to profit
                    if (startYear < year && endYear > year)
                    {
                        double amountCharged = Double.parseDouble(cursor2.getString(cursor2.getColumnIndex(AMOUNT_CHARGED)));
                        double cost = Double.parseDouble(cursor2.getString(cursor2.getColumnIndex(COST_REPAIR)));
                        amountChargedRepairs = amountCharged;
                        cost = costAmountRepairs;
                        profitAmount += (amountCharged - cost);
                    }
                    else if (startYear < year && endYear >= year)
                    {
                        if (endMonth > month && endDay >= day)
                        {
                            double amountCharged = Double.parseDouble(cursor2.getString(cursor2.getColumnIndex(AMOUNT_CHARGED)));
                            double cost = Double.parseDouble(cursor2.getString(cursor2.getColumnIndex(COST_REPAIR)));
                            amountChargedRepairs = amountCharged;
                            cost = costAmountRepairs;
                            profitAmount += (amountCharged - cost);
                        }
                    }

                    else if (startYear == year && endYear >= year)
                    {
                        if (endYear > year && month > startMonth)
                        {
                            double amountCharged = Double.parseDouble(cursor2.getString(cursor2.getColumnIndex(AMOUNT_CHARGED)));
                            double cost = Double.parseDouble(cursor2.getString(cursor2.getColumnIndex(COST_REPAIR)));
                            amountChargedRepairs = amountCharged;
                            cost = costAmountRepairs;
                            profitAmount += (amountCharged - cost);
                        }
                        else if (endYear > year && month == startMonth && startDay <= day)
                        {
                            double amountCharged = Double.parseDouble(cursor2.getString(cursor2.getColumnIndex(AMOUNT_CHARGED)));
                            double cost = Double.parseDouble(cursor2.getString(cursor2.getColumnIndex(COST_REPAIR)));
                            amountChargedRepairs = amountCharged;
                            cost = costAmountRepairs;
                            profitAmount += (amountCharged - cost);
                        }
                        else if (endYear == year)
                        {
                            if (startMonth <= month && month <= endMonth && startDay <= day && endDay >= day)
                            {
                                double amountCharged = Double.parseDouble(cursor2.getString(cursor2.getColumnIndex(AMOUNT_CHARGED)));
                                double cost = Double.parseDouble(cursor2.getString(cursor2.getColumnIndex(COST_REPAIR)));
                                amountChargedRepairs = amountCharged;
                                cost = costAmountRepairs;
                                profitAmount += (amountCharged - cost);
                            }
                        }
                    }


                    while (cursor.moveToNext())
                    {
                        date = cursor2.getString(2);
                        day = Integer.parseInt(date.substring(0, 2));
                        month = Integer.parseInt(date.substring(3, 5));
                        year = Integer.parseInt(date.substring(6, 10));

                        if (startYear < year && endYear > year)
                        {
                            double amountCharged = Double.parseDouble(cursor2.getString(cursor2.getColumnIndex(AMOUNT_CHARGED)));
                            double cost = Double.parseDouble(cursor2.getString(cursor2.getColumnIndex(COST_REPAIR)));
                            amountChargedRepairs = amountCharged;
                            cost = costAmountRepairs;
                            profitAmount += (amountCharged - cost);
                        }

                        else if (startYear < year && endYear >= year)
                        {
                            if (endMonth > month && endDay >= day)
                            {
                                double amountCharged = Double.parseDouble(cursor2.getString(cursor2.getColumnIndex(AMOUNT_CHARGED)));
                                double cost = Double.parseDouble(cursor2.getString(cursor2.getColumnIndex(COST_REPAIR)));
                                amountChargedRepairs = amountCharged;
                                cost = costAmountRepairs;
                                profitAmount += (amountCharged - cost);
                            }
                        }

                        else if (startYear == year && endYear >= year)
                        {
                            if (endYear > year && month > startMonth)
                            {
                                double amountCharged = Double.parseDouble(cursor2.getString(cursor2.getColumnIndex(AMOUNT_CHARGED)));
                                double cost = Double.parseDouble(cursor2.getString(cursor2.getColumnIndex(COST_REPAIR)));
                                amountChargedRepairs = amountCharged;
                                cost = costAmountRepairs;
                                profitAmount += (amountCharged - cost);
                            }
                            else if (endYear > year && month == startMonth && startDay <= day)
                            {
                                double amountCharged = Double.parseDouble(cursor2.getString(cursor2.getColumnIndex(AMOUNT_CHARGED)));
                                double cost = Double.parseDouble(cursor2.getString(cursor2.getColumnIndex(COST_REPAIR)));
                                amountChargedRepairs = amountCharged;
                                cost = costAmountRepairs;
                                profitAmount += (amountCharged - cost);
                            }
                            else if (endYear == year)
                            {
                                if (startMonth <= month && month <= endMonth && startDay <= day && endDay >= day)
                                {
                                    double amountCharged = Double.parseDouble(cursor2.getString(cursor2.getColumnIndex(AMOUNT_CHARGED)));
                                    double cost = Double.parseDouble(cursor2.getString(cursor2.getColumnIndex(COST_REPAIR)));
                                    amountChargedRepairs = amountCharged;
                                    cost = costAmountRepairs;
                                    profitAmount += (amountCharged - cost);
                                }
                            }
                        }
                    }


                }
            }

            cursor2.close();
            cursor.close();
            DB.close();
        }

        profitSum[0]= profitAmount;
        profitSum[1]= bikeSalesTotal;
        profitSum[2]= costAmountRepairs;
        profitSum[3]= amountChargedRepairs;

    }

    //For when the user wants the profit, we return the information
    public String [] repairInfo(String serialNum, String [] info)
    {
        //To be able to write to database
        SQLiteDatabase DB = this.getWritableDatabase();

        //To make sure that the serial does not already exist.
        //Make sure you put quotes around the string. In this case serial
        Cursor cursor = null;
        String sql = "SELECT customerName, customerPhone, dueDate, costOfRepair, amountCharged," +
                " dateCompleted, email FROM repairs WHERE serial = '" + serialNum + "' and deleted = " + 0;

        cursor = DB.rawQuery(sql, null);

        String information[] = new String[7];

        //If the cursor is less then 0 then its not in the table yet
        if(cursor.getCount()<=0)
        {
            cursor.close();
            DB.close();
        }
        else
        {
            if(cursor!=null)
            {
                cursor.moveToFirst();

                //The order of these are the order of in which we selected in the query
                //So customer name is first, then phone etc.
                information[0] = cursor.getString(0);
                information[1] = cursor.getString(1);
                information[2] = cursor.getString(2);
                information[3] = cursor.getString(3);
                information[4] = cursor.getString(4);
                information[5] = cursor.getString(5);
                information[6] = cursor.getString(6);
            }
            cursor.close();
            DB.close();
        }

        for(int i = 0; i<7; i++)
        {
            info [i] = information[i];
        }

        return info;

    }
}


