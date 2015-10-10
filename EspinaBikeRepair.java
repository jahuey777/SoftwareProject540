package com.example.jaimejahuey.espinasbikes;

/**
 * Created by Daniel Espina on 10/9/15.
 */
//This class is a bike class for bike objects that are in the repairs table, so
//they aren't necessarily in the inventory table


public class BikeRepair
{
    //Only 4 attributes, cost, date sold, serial, phone number, and 
    //custumer's name
    public double repair_cost;
    public String repair_serial;
    public String repair_due;
    public int    repair_phoneNum;
    public String custumer_name;

    	BikeRepair(double cost, String serial, String dueDate, int phoneNum, String custumerName)
    	{
    		repair_cost = cost;
    		repair_serial = serial;
    		repair_due = dueDate;
    		repair_phoneNum = phoneNum;
    		custumer_name = custumerName;
    	}

   public double getRepair_cost()
   {
   		return repair_cost;
   }

   public String getRepair_serial()
   {
   		return repair_serial;
   }

   public String getRepair_due()
   {
   		return repair_due;
   }

   public int getRepair_phoneNum()
   {
   		return repair_phoneNum;
   }

   public String getCustumer_name()
   {
   		return custumer_name;
   }
}
