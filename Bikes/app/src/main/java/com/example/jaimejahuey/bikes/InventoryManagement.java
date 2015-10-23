package com.example.jaimejahuey.bikes;

/**
 * Created by jaimejahuey on 9/18/15.
 */
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by jaimejahuey on 9/16/15.
 */
public class InventoryManagement extends ActionBarActivity
{
    //Buttons of the inventory interface menu
    private Button addBike;
    private Button displayBikes;
    private Button removeBike;
    private Button updateBikes;

    private EditText removeSerialText;
    final Context context = this;

    //made it public and static so that the upgrade class can use it
    public static String resultSerialNum;

    //for the updatebutton, to see if we need to launch the new intent or not.
    public boolean check;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_layout);

        //Linking to the XML file
        addBike = (Button) findViewById(R.id.AddBike);
        removeBike = (Button) findViewById(R.id.RemoveBike);
        displayBikes = (Button) findViewById(R.id.ViewInventory);
        updateBikes = (Button) findViewById(R.id.UpdateBike);

        //removeSerialText = (EditText) findViewById(R.id.remove_bike_dialog_input);

        addBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(InventoryManagement.this , addingBike.class);

                startActivity(i);

            }
        });


        //If the user is going to remove a bike

        removeBike.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                LayoutInflater RemoveInflator = LayoutInflater.from(context);
                View RemoveView  = RemoveInflator.inflate(R.layout.remove_bike_dialog, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                //sets remove_bike_dialog.xml to alertdialog builder
                alertDialogBuilder.setView(RemoveView);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Remove",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id)
                                    {

                                        //Had to add this line, otherwise it will throw an error whenever
                                        //the user inputs anything to the pop dialog
                                        Dialog f = (Dialog) dialog;

                                        removeSerialText = (EditText) f.findViewById(R.id.inputValueDialog);

                                        //Getting the text from the dialog
                                        resultSerialNum = removeSerialText.getText().toString();
                                        Log.d("input" , "" + resultSerialNum);
                                        Boolean didWeDelete = MainActivity.DATABASE.removeBike(resultSerialNum);

                                        //Will tell the user if it deleted or not
                                        if(didWeDelete)
                                            Toast.makeText(getApplicationContext(), "The Bike has been removed from inventory.", Toast.LENGTH_LONG).show();
                                        else
                                            Toast.makeText(getApplicationContext(), "The bike can not be removed since it is not in the inventory or it has already been removed.", Toast.LENGTH_LONG).show();

                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id)
                                    {
                                        dialog.cancel();
                                    }
                                });

                //To actually create it
                AlertDialog alertDialog = alertDialogBuilder.create();

                //show it
                alertDialog.show();


            }
        });


        //sets what happens when you press the Display Inventory button
        displayBikes.setOnClickListener(new View.OnClickListener(){
            @Override
        public void onClick(View v)
            {
                Intent i = new Intent(InventoryManagement.this, displayInventory.class);
                startActivity(i);
            }
        });


        //For the update bike
        //Reusing part of the remove bike code.
        //Get the user's
        updateBikes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                LayoutInflater RemoveInflator = LayoutInflater.from(context);
                View RemoveView  = RemoveInflator.inflate(R.layout.remove_bike_dialog, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                //sets remove_bike_dialog.xml to alertdialog builder
                alertDialogBuilder.setView(RemoveView);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Update",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {

                                        //Had to add this line, otherwise it will throw an error whenever
                                        //the user inputs anything to the pop dialog
                                        Dialog f = (Dialog) dialog;

                                        removeSerialText = (EditText) f.findViewById(R.id.inputValueDialog);

                                        //Getting the text from the dialog
                                        resultSerialNum = removeSerialText.getText().toString();
                                        //Log.d("input" , "" + resultSerialNum);
                                        Boolean doesItExist = MainActivity.DATABASE.existsOrNot(resultSerialNum);

                                        //if it exist we will update it or let the user update it
                                        if (doesItExist)
                                        {
                                            Intent i = new Intent(InventoryManagement.this, updateBike.class);

                                            startActivity(i);
                                           check = true;
                                        }
                                        else
                                        {
                                            Toast.makeText(getApplicationContext(), "Bike can't be updated since it doesn't exist.", Toast.LENGTH_LONG).show();
                                            check = false;
                                        }

                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                //To actually create it
                AlertDialog alertDialog = alertDialogBuilder.create();

                //show it
                alertDialog.show();

                if(check==true)
                {


                    check=false;
                }


            }

        });


        


    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
