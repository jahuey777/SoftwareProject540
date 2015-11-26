package com.example.jaimejahuey.bikes;

import android.app.Activity;
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
import android.widget.Toast;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by David on 10/27/2015.
 */
public class AddingRepair extends Activity
{
    //Getting phone number input
    private EditText phoneNumInput;
    private EditText custNameInput;
    private EditText SerialRepairInput;
    private EditText emailInput;

    //for getting the date
    private ImageButton ButtonCal;
    private static String dataBaseday;
    private static String dataBasemonth;
    private static String dataBaseyear;
    private static EditText repairCalendar;

    private Button enterRepair;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adding_repair);

        dataBaseday = null;
        dataBasemonth = null;
        dataBaseyear = null;

        //For the phone number, this addTextChangeListener will automatically add the hyphen
        phoneNumInput = (EditText) findViewById(R.id.addRepairCustomerPhone);
        phoneNumInput.addTextChangedListener(new PhoneNumberFormattingTextWatcher());


        //This is all for the repair date.
        ButtonCal = (ImageButton) findViewById(R.id.imageButtonCalendar);
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

        //link to xml editext for customer name input
        custNameInput= (EditText) findViewById(R.id.addRepairCustomerName);
        SerialRepairInput = (EditText) findViewById(R.id.addRepairSerial);
        emailInput = (EditText)findViewById(R.id.addRepairEmail);

        //link to xml enter in addingrepair
        enterRepair= (Button) findViewById(R.id.addRepairEnterButton);

        enterRepair.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                String PHONUM= phoneNumInput.getText().toString();
                String CUSTNAME = custNameInput.getText().toString();
                String REPAIRSERIAL = SerialRepairInput.getText().toString();
                String EMAIL = emailInput.getText().toString();

               if(PHONUM!= null || CUSTNAME!=null || REPAIRSERIAL !=null)
               {
                   if(PHONUM.isEmpty() || CUSTNAME.isEmpty() || REPAIRSERIAL.isEmpty())
                   {
                       Toast.makeText(getApplicationContext(), "Please make sure that all input has been filled.", Toast.LENGTH_LONG).show();

                   }
                   else if(dataBaseday== null || dataBasemonth== null|| dataBaseyear== null)
                   {
                       Toast.makeText(getApplicationContext(), "Please select a date.", Toast.LENGTH_LONG).show();
                   }

                   else
                   {
                       //Want a format of 02/01/2015
                       //So if the day or month is year is less than 10, then append a 0 to the beginning
                       if(Integer.parseInt(dataBaseday)<10)
                       {
                           dataBaseday = "0" + dataBaseday;
                       }
                       if(Integer.parseInt(dataBasemonth)<10)
                       {
                            dataBasemonth = "0" + dataBasemonth;
                       }

                        //Formatting the date for the database
                       String databaseDate = dataBasemonth + "/" + dataBaseday + "/" + dataBaseyear;

                       //Last part is getting the email. It can be blank, so if it is we will add an empty string.
                       if(EMAIL.isEmpty())
                       {
                           EMAIL = "";

                           //To add the repair to the database. Will return true if it succeeds.
                           Boolean didWeAddRepair = MainActivity.DATABASE.addRepair(REPAIRSERIAL, PHONUM, CUSTNAME, databaseDate, EMAIL);

                           //Display a message if it was added or not to the database
                           if(didWeAddRepair)
                           {
                               Toast.makeText(getApplicationContext(), "The repair has been added.", Toast.LENGTH_LONG).show();
                               //finish();
                           }
                           else
                           {
                               Toast.makeText(getApplicationContext(), "The repair was not added since it already exists.", Toast.LENGTH_LONG).show();
                           }

                       }
                       //Email is not blank. So we will verify the email now.
                       else
                       {
                           if(isEmailValid(EMAIL))
                           {
                               //To add the repair to the database. Will return true if it succeeds.
                               Boolean didWeAddRepair = MainActivity.DATABASE.addRepair(REPAIRSERIAL, PHONUM, CUSTNAME, databaseDate, EMAIL);

                               //Display a message if it was added or not to the database
                               if(didWeAddRepair)
                               {
                                   Toast.makeText(getApplicationContext(), "The repair has been added.", Toast.LENGTH_LONG).show();
                                   finish();
                               }
                               else
                               {
                                   Toast.makeText(getApplicationContext(), "The repair was not added since it already exists.", Toast.LENGTH_LONG).show();
                               }
                           }
                           else
                           {
                               Toast.makeText(getApplicationContext(), "The email is invalid. Please try again.", Toast.LENGTH_LONG).show();
                           }
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

    /**
     * method is used for checking valid email id format.
     * Got the method from a stackoverflow user.
     * http://stackoverflow.com/questions/6119722/how-to-check-edittexts-text-is-email-address-or-not
     */
    public static boolean isEmailValid(String email)
    {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches())
        {
            isValid = true;
        }
        return isValid;
    }



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
            dataBaseday = String.valueOf(day);
            dataBasemonth = String.valueOf(month+1);
            dataBaseyear = String.valueOf(year);

            //displays in our editText in the xml Addingrepair
            repairCalendar.setText(String.valueOf(month + 1)+ "/" + String.valueOf(day) + "/"
                      + String.valueOf(year));
        }
    }
   }