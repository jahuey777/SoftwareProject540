package com.example.jaimejahuey.bikes;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by David on 10/27/2015.
 */
public class AddingRepair extends ActionBarActivity
{


    //Getting phone number input
    private EditText phoneNumInput;

    //for getting the date
    private ImageButton ButtonCal;
    private Calendar cal;
    private int day;
    private int month;
    private int year;
    private static EditText repairCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adding_repair);

        //For the phone number, this addTextChangeListener will automatically add the hyphen
        phoneNumInput = (EditText) findViewById(R.id.addRepairCustomerPhone);
        phoneNumInput.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        //String PhonNum= phoneNumInput.getText().toString();

        ButtonCal = (ImageButton) findViewById(R.id.imageButtonCalendar);
        cal= Calendar.getInstance();
        day= cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        repairCalendar = (EditText) findViewById(R.id.addRepairDate);
        ButtonCal.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DialogFragment newFragment= new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");


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

    //To create a new datePicker
    //resused this code from stack overflow http://stackoverflow.com/questions/16990151/how-to-make-simple-datepicker-dialog-with-buttons-and
    //made minor changes for our project
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            //Will also save ints so that we can save to database
            //Decided to create separate ones so that it doesn't get confusing in the code
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {

            //displays in our editText in the xml Addingrepair
            repairCalendar.setText(String.valueOf(day) + "/"
                    + String.valueOf(month + 1) + "/" + String.valueOf(year));
        }
    }


}
