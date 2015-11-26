package com.example.jaimejahuey.bikes;

/**
 * Created by jaimejahuey on 10/2/15.
 */

//This class is a bike class for bikes in the inventory tables which is linked with the sales
public class Bike
{
    //The attributes of a Bike
    public String inventory_make;
    //public String inventory_model;
    public String inventory_serial;
    //public int inventory_quantity;
    public String inventory_color;
    public String inventory_condition;

    Bike(String SERIAL, String MAKE, String COLOR, String CONDITION)
    {
        inventory_make = MAKE;
        inventory_serial= SERIAL;
        inventory_color = COLOR;
        inventory_condition = CONDITION;
    }

    public String getInventory_make()
    {
        return inventory_make;
    }

    public String getInventory_serial()
    {
        return inventory_serial;
    }

    public String getInventory_color()
    {
        return inventory_color;
    }

    public String getInventory_condition()
    {
        return inventory_condition;
    }
}
