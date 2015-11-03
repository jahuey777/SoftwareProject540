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
public class Repairs extends ActionBarActivity
{
    private Button addRepair;
    private Button completedRepair;

    final Context context = this;
    private EditText completedSerialText;
    public static String completedSerialNum;


    // private Button deleteRepair;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repairs_layout);

        //Since we use this variable in the completedRepair class
        //We then reset it to be careful
        completedSerialNum = null;


        //linking buttons to xml file buttons
        addRepair = (Button) findViewById(R.id.AddRepair);
        completedRepair = (Button) findViewById(R.id.CompletedRepair);

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
