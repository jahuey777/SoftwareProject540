package com.example.jaimejahuey.bikes;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by jaimejahuey on 11/16/15.
 */
public class profitSummary extends ActionBarActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_profit_summary);

        String startDate = getIntent().getExtras().getString("First Date");
        String endDate = getIntent().getExtras().getString("End Date");

        String profit = calculateSummary(startDate,endDate);

        Toast.makeText(getApplicationContext(), "Profit is " + profit, Toast.LENGTH_LONG).show();

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

    //Where we calculate the profit.
    private String calculateSummary (String start, String end)
    {
        //calculate the bike profit from the sales table.
      //  Log.w("start", start);
        // Log.w("end", end);

        double profit = MainActivity.DATABASE.bikeProfit(start, end);


        String PROFIT = "" + profit;


        return PROFIT;
    }

}

