package com.example.jaimejahuey.bikes;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
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
 * Created by DanielEspina on 10/21/15.
 */
public class viewProfit extends Activity
{
    //for getting the beggining date of the profit
    private ImageButton profitBegImageCalendarButton;
    private static String dataBaseBeginningDayProfit;
    private static String dataBaseBeginningMonthProfit;
    private static String dataBaseBeginningYearProfit;
    private static EditText profitBeginningDate;

    //for getting the ending date
    private ImageButton profitEndImageCalendarButton;
    private static String dataBaseEndDayProfit;
    private static String dataBaseEndMonthProfit;
    private static String dataBaseEndYearProfit;
    private static EditText profitEndDate;

    private Button enterProfitDates;

    //use this for when we get the dates.
    //set to 0 when grabbing the beginning date and set to 1 to grab ending
    private static int whichDate = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_profit);

        dataBaseBeginningDayProfit= null;
        dataBaseBeginningMonthProfit= null;
        dataBaseBeginningYearProfit= null;

        dataBaseEndDayProfit= null;
        dataBaseEndMonthProfit= null;
        dataBaseEndYearProfit=null;

        profitBegImageCalendarButton = (ImageButton) findViewById(R.id.startProfitImageButton);
        profitBeginningDate = (EditText) findViewById(R.id.startProfitDate);
        profitBegImageCalendarButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                whichDate = 0;
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        profitEndImageCalendarButton = (ImageButton) findViewById(R.id.endProfitImageButton);
        profitEndDate = (EditText) findViewById(R.id.endProfitDate);
        profitEndImageCalendarButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                whichDate=1;
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");

            }
        });

        enterProfitDates = (Button) findViewById((R.id.enterProfitButton));
        enterProfitDates.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //first checking to make sure that none of them are null
                if(dataBaseBeginningDayProfit == null || dataBaseBeginningMonthProfit ==null
                        || dataBaseBeginningYearProfit==null || dataBaseEndDayProfit==null ||
                        dataBaseEndMonthProfit ==null || dataBaseEndYearProfit==null)
                {
                    Toast.makeText(getApplicationContext(), "Please select 2 dates.", Toast.LENGTH_LONG).show();

                }

                //if the beginning date is bigger then the ending date,then display toast for new input
                else if(Integer.parseInt(dataBaseBeginningYearProfit)>Integer.parseInt(dataBaseEndYearProfit))
                {
                    Toast.makeText(getApplicationContext(), "Please select proper dates.", Toast.LENGTH_LONG).show();

                }

                //If same year and month but the beginning day is bigger than the end day then display toast
                else if(Integer.parseInt(dataBaseBeginningYearProfit)==Integer.parseInt(dataBaseEndYearProfit) &&
                        Integer.parseInt(dataBaseBeginningMonthProfit)==Integer.parseInt(dataBaseEndMonthProfit) &&
                        Integer.parseInt(dataBaseBeginningDayProfit)>Integer.parseInt(dataBaseEndDayProfit))
                {
                    Toast.makeText(getApplicationContext(), "Please select proper dates.", Toast.LENGTH_LONG).show();

                }

                else if (Integer.parseInt(dataBaseBeginningYearProfit)==Integer.parseInt(dataBaseEndYearProfit) &&
                        Integer.parseInt(dataBaseBeginningMonthProfit)>Integer.parseInt(dataBaseEndMonthProfit))
                {
                    Toast.makeText(getApplicationContext(), "Please select proper dates.", Toast.LENGTH_LONG).show();

                }
                else
                {
                    if (Integer.parseInt(dataBaseBeginningDayProfit) < 10 )
                    {
                        dataBaseBeginningDayProfit = "0" + dataBaseBeginningDayProfit;
                    }
                    if (Integer.parseInt(dataBaseEndDayProfit) < 10)
                    {
                        dataBaseEndDayProfit = "0" + dataBaseEndDayProfit;
                    }
                    if(Integer.parseInt(dataBaseEndMonthProfit)<10)
                    {
                        dataBaseEndMonthProfit = "0" + dataBaseEndMonthProfit;
                    }
                    if(Integer.parseInt(dataBaseBeginningMonthProfit)<10)
                    {
                        dataBaseBeginningMonthProfit = "0" + dataBaseBeginningMonthProfit;
                    }


                    String startDate = dataBaseBeginningMonthProfit + " " + dataBaseBeginningDayProfit
                            + " " + dataBaseBeginningYearProfit;

                    //creating endDate to pass through
                    String endDate = dataBaseEndMonthProfit + " " + dataBaseEndDayProfit +
                            " " + dataBaseEndYearProfit;

                    Intent i = new Intent(viewProfit.this, profitSummary.class);
                    i.putExtra("First Date",startDate );
                    i.putExtra("End Date", endDate);

                    startActivity(i);

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

            if(whichDate == 0)
            {
                dataBaseBeginningDayProfit = String.valueOf(day);
                dataBaseBeginningMonthProfit = String.valueOf(month+1);
                dataBaseBeginningYearProfit = String.valueOf(year);

                //displays in our editText in the xml Addingrepair
                profitBeginningDate.setText(String.valueOf(month + 1) + "/" + String.valueOf(day) + "/"
                        + String.valueOf(year));

            }
            else if(whichDate == 1)
            {
                dataBaseEndDayProfit = String.valueOf(day);
                dataBaseEndMonthProfit = String.valueOf(month+1);
                dataBaseEndYearProfit = String.valueOf(year);

                //displays in our editText in the xml Addingrepair
                profitEndDate.setText(String.valueOf(month + 1) + "/" + String.valueOf(day) + "/"
                        + String.valueOf(year));

            }
        }
    }
}
