package com.example.jaimejahuey.bikes;

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
 * Created by jaimejahuey on 10/23/15.
 */
public class soldBike extends ActionBarActivity
{
    private EditText serialSaleInput;
    private EditText priceSaleInput;

    //for getting the date
    private ImageButton salesImageCalendarButton;
    private static String dataBasedayOfSale;
    private static String dataBasemonthOfSale;
    private static String dataBaseyearOfSale;
    private static EditText salesDateInput;

    private Button enterSaleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sold_bike);

        //reset values
        dataBasedayOfSale = null;
        dataBasemonthOfSale = null;
        dataBaseyearOfSale = null;

        //Linking to xlm edittext
        serialSaleInput = (EditText) findViewById(R.id.soldBikeSerialInput);
        priceSaleInput = (EditText) findViewById(R.id.soldBikePriceInput);

        //link calendar button from xml file
        salesImageCalendarButton = (ImageButton) findViewById(R.id.salesButtonImageCalendar);
        salesDateInput = (EditText) findViewById(R.id.saleBikeDate);
        salesImageCalendarButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        //link enter sale information button from xml file
        enterSaleButton = (Button) findViewById(R.id.addSalesEnterButton);
        enterSaleButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String SERIALNUM = serialSaleInput.getText().toString();
                String SALEPRICE = priceSaleInput.getText().toString();

                if(SERIALNUM!= null || SALEPRICE!=null)
                {
                    if (SERIALNUM.isEmpty() || SALEPRICE.isEmpty())
                    {
                        Toast.makeText(getApplicationContext(), "Please make sure that all input has been filled.", Toast.LENGTH_LONG).show();

                    }
                    else if (dataBasedayOfSale == null || dataBasemonthOfSale == null || dataBaseyearOfSale == null)
                    {
                        Toast.makeText(getApplicationContext(), "Please select a date.", Toast.LENGTH_LONG).show();

                    }
                    else
                    {
                        //Want a format of 02/01/2015
                        //So if the day or month is year is less than 10, then append a 0 to the beginning
                        if (Integer.parseInt(dataBasedayOfSale) < 10)
                        {
                            dataBasedayOfSale += "0";
                        }
                        if (Integer.parseInt(dataBasemonthOfSale) < 10)
                        {
                            dataBasemonthOfSale = "0" + dataBasemonthOfSale;
                        }

                        //Formatting the date for the database
                        String databaseSaleDate = dataBasemonthOfSale + "/" + dataBasedayOfSale + "/" + dataBaseyearOfSale;

                        Toast.makeText(getApplicationContext(), "checking date for format " + databaseSaleDate , Toast.LENGTH_LONG).show();

                        //inserting a new sale into the database.
                        Boolean didWeAddSale = MainActivity.DATABASE.addingSale(SERIALNUM, databaseSaleDate, SALEPRICE);

                        //Updating the inventory table by removing bike
                        //Boolean wasBikeRemoved = MainActivity.DATABASE.removeBike(SERIALNUM);

                        //Display a message if it was added or not to the database
                        if (didWeAddSale)
                        {
                            Toast.makeText(getApplicationContext(), "The bike has been sold.", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Can't sell bike. This bike doesn't exist in the inventory.", Toast.LENGTH_LONG).show();
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
            dataBasedayOfSale = String.valueOf(day);
            dataBasemonthOfSale = String.valueOf(month+1);
            dataBaseyearOfSale = String.valueOf(year);

            //displays in our editText in the xml Addingrepair
            salesDateInput.setText(String.valueOf(month + 1) + "/" + String.valueOf(day) + "/"
                    + String.valueOf(year));
        }
    }


}
