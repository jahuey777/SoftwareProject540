package com.example.jaimejahuey.bikes;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by jaimejahuey on 11/3/15.
 */
public class completedRepair extends Activity
{
    //Getting phone number input
    private EditText amountChargedInput;
    private EditText amountCostInput;

    //for getting the date
    private ImageButton ButtonCompletedCal;
    private static String dataBasedayCompleted;
    private static String dataBasemonthCompleted;
    private static String dataBaseyearCompleted;
    private static EditText repairCompletedCalendar;

    private Button enterRepair;

    //Getting string from the repairs class whenever the user
    //inputted the serail number for a compelted repair.
    String completeSerial;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.completed_repair);

        completeSerial= Repairs.completedSerialNum;

        //reset of values
        dataBasedayCompleted = null;
        dataBasemonthCompleted = null;
        dataBaseyearCompleted = null;

        //Linking to xlm edittext
        amountChargedInput = (EditText) findViewById(R.id.completedAmountChargedRepair);
        amountCostInput = (EditText) findViewById(R.id.completedCostRepair);


        //This is all for the repair date.
        ButtonCompletedCal = (ImageButton) findViewById(R.id.completedimageButtonCalendar);
        repairCompletedCalendar = (EditText) findViewById(R.id.completedRepairDate);
        ButtonCompletedCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");

            }
        });

        //link to xml enter in addingrepair
        enterRepair= (Button) findViewById(R.id.completedRepairEnterButton);

        enterRepair.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String AMOUNTCHARGED= amountChargedInput.getText().toString();
                String AMOUNTCOST = amountCostInput.getText().toString();

                if(AMOUNTCHARGED!= null || AMOUNTCOST!=null) {
                    if (AMOUNTCHARGED.isEmpty() || AMOUNTCOST.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please make sure that all input has been filled.", Toast.LENGTH_LONG).show();

                    } else if (dataBasedayCompleted == null || dataBasemonthCompleted == null || dataBaseyearCompleted == null) {
                        Toast.makeText(getApplicationContext(), "Please select a date.", Toast.LENGTH_LONG).show();

                    } else {
                        //Want a format of 02/01/2015
                        //So if the day or month is year is less than 10, then append a 0 to the beginning
                        if (Integer.parseInt(dataBasedayCompleted) < 10) {
                            dataBasedayCompleted += "0";
                        }
                        if (Integer.parseInt(dataBasemonthCompleted) < 10) {
                            dataBasemonthCompleted += "0" + dataBasemonthCompleted;
                        }

                        //Formatting the date for the database
                        String databaseCompletedDate = dataBasemonthCompleted + "/" + dataBasedayCompleted + "/" + dataBaseyearCompleted;

                        //Toast.makeText(getApplicationContext(), "checking date for format " + databaseCompletedDate , Toast.LENGTH_LONG).show();

                        //inserting the new repair into the database.
                        Boolean didWeAddRepair = MainActivity.DATABASE.completedRepair(completeSerial, AMOUNTCOST, AMOUNTCHARGED, databaseCompletedDate);

                        //Display a message if it was added or not to the database
                        if (didWeAddRepair) {
                            Toast.makeText(getApplicationContext(), "The repair has been updated.", Toast.LENGTH_LONG).show();
                            //Finish the activity, otherwise the user can update as many times as he wants
                            finish();
                        }
                    }
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
    public boolean onOptionsItemSelected(MenuItem item)
    {
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

    //copy this

    //To create a new datePicker
    //resused this code from stack overflow http://stackoverflow.com/questions/16990151/how-to-make-simple-datepicker-dialog-with-buttons-and
    //made minor changes for our project
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener
    {
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            // Use the current date as the default date in the picker
            //Will also save ints so that we can save to database
            //Decided to create separate ones so that it doesn't get confusing in the code
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day)
        {
            //Save the days here to store in the database.
            dataBasedayCompleted = String.valueOf(day);
            dataBasemonthCompleted = String.valueOf(month+1);
            dataBaseyearCompleted = String.valueOf(year);

            //displays in our editText in the xml Addingrepair
            repairCompletedCalendar.setText(String.valueOf(month + 1) + "/" + String.valueOf(day) + "/"
                    + String.valueOf(year));
        }
    }
}