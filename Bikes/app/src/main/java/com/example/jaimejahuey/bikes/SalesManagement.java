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

public class SalesManagement extends ActionBarActivity
{
    private Button ViewProfitButton;
    private Button SoldBikeButton;

    Context context= this;
    EditText soldSerial;
    private static String soldSerialString;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_layout);

        SoldBikeButton = (Button) findViewById(R.id.SoldBike);
        ViewProfitButton = (Button) findViewById(R.id.ViewProfit);

        //Takes us to the view profit page.
        ViewProfitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SalesManagement.this, viewProfit.class);

                startActivity(i);
            }
        });


        //Check for the serial of the bike that is sold.
        //If it exists, we will allow the user to add it to the sold bikes database
        SoldBikeButton.setOnClickListener(new View.OnClickListener()
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
                        .setPositiveButton("Enter",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {

                                        //Had to add this line, otherwise it will throw an error whenever
                                        //the user inputs anything to the pop dialog
                                        Dialog f = (Dialog) dialog;

                                        soldSerial = (EditText) f.findViewById(R.id.inputValueDialog);

                                        //Getting the text from the dialog
                                        soldSerialString = soldSerial.getText().toString();
                                        Boolean didWeDelete = MainActivity.DATABASE.existsOrNot(soldSerialString);

                                        //Will tell the user if it deleted or not
                                        if (didWeDelete)
                                        {
                                            //Toast.makeText(getApplicationContext(), "The Bike has been removed from inventory.", Toast.LENGTH_LONG).show();
                                            Intent i = new Intent(SalesManagement.this, soldBike.class);

                                            startActivity(i);

                                        }
                                        else
                                        {
                                            Toast.makeText(getApplicationContext(), "The bike can not be sold since it does not exist in the inventory.", Toast.LENGTH_LONG).show();
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