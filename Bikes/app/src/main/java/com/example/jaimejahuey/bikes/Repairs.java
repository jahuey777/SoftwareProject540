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
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by jaimejahuey on 9/16/15.
 */
public class Repairs extends ActionBarActivity
{
    private ImageButton addRepair;
    private ImageButton completedRepair;
    private ImageButton viewActiveRepairButton;
    private ImageButton viewCompletedRepairButton;
    private ImageButton deleteRepairButton;

    final Context context = this;
    private EditText completedSerialText;
    public static String completedSerialNum;

    //Use for the removing a bike to link with the pop up dialog serial text box
    private EditText removeRepairSerialText;
    private String repairSerial;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repairs_layout);

        //Since we use this variable in the completedRepair class
        //We then reset it to be careful
        completedSerialNum = null;


        //linking buttons to xml file buttons
        addRepair = (ImageButton) findViewById(R.id.AddRepair);
        completedRepair = (ImageButton) findViewById(R.id.CompletedRepair);
        viewActiveRepairButton = (ImageButton) findViewById(R.id.ViewActiveRepairs);
        viewCompletedRepairButton = (ImageButton) findViewById(R.id.ViewPendingRepairs);
        deleteRepairButton = (ImageButton) findViewById(R.id.DeleteRepair);

        //WHen the user clikcs on the button. Go to addingRepair class
        addRepair.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(Repairs.this, AddingRepair.class);

                startActivity(i);
            }
        });

        //WHen the user clicks on the completed repair.
        //first we check to see that the repair exists.
        completedRepair.setOnClickListener(new View.OnClickListener()
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

                                        completedSerialText = (EditText) f.findViewById(R.id.inputValueDialog);

                                        //Getting the text from the dialog
                                        completedSerialNum = completedSerialText.getText().toString();
                                        Boolean exists = MainActivity.DATABASE.repairExists(completedSerialNum);

                                        //Launch the completed xml if the repair exists.
                                        if (exists)
                                        {
                                            Intent i = new Intent(Repairs.this, completedRepair.class);

                                            startActivity(i);

                                        }
                                        else
                                            Toast.makeText(getApplicationContext(), "The bike can't be set to complete since it doesn't exist.", Toast.LENGTH_LONG).show();

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

        deleteRepairButton.setOnClickListener(new View.OnClickListener()
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

                                        removeRepairSerialText = (EditText) f.findViewById(R.id.inputValueDialog);

                                        //Getting the text from the dialog
                                        repairSerial = removeRepairSerialText.getText().toString();
                                        Boolean didWeDelete = MainActivity.DATABASE.removeRepair(repairSerial);

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


        viewCompletedRepairButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(Repairs.this, viewCompletedRepairs.class);

                startActivity(i);
            }
        });

        viewActiveRepairButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Repairs.this, viewActiveRepairs.class);

                startActivity(i);
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
