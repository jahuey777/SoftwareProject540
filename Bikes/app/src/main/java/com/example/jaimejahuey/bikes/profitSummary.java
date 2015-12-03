package com.example.jaimejahuey.bikes;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * Created by jaimejahuey on 11/16/15.
 */
public class profitSummary extends Activity
{
    TextView totalProfit;
    TextView totalBikeProfitJ;
    TextView spentRepairsJ;
    TextView chargedRepairsJ;
    TextView totalBikeAmountJ;
    TextView repairsProfitJ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_profit_summary);

        String startDate = getIntent().getExtras().getString("First Date");
        String endDate = getIntent().getExtras().getString("End Date");
        double [] information= new double [4];

        MainActivity.DATABASE.bikeProfit(startDate, endDate, information);

        Log.w("Amount ", " " + information[0]);

        NumberFormat numFormat = NumberFormat.getCurrencyInstance();

        totalProfit = (TextView)findViewById(R.id.ProfitAmount);
        totalProfit.setText("" + numFormat.format(information[0]));

        totalBikeProfitJ = (TextView)findViewById(R.id.TotalBikeProfit);
        totalBikeProfitJ.setText("" + numFormat.format(information[1]*.2));

        spentRepairsJ = (TextView)findViewById(R.id.SPENTREPAIRS);
        spentRepairsJ.setText("" + numFormat.format(information[2]));

        chargedRepairsJ = (TextView)findViewById(R.id.CHARGEDREPAIRS);
        chargedRepairsJ.setText("" + numFormat.format(information[3]));

        totalBikeAmountJ = (TextView)findViewById(R.id.TOTALBIKEAMOUNT);
        totalBikeAmountJ.setText("" + numFormat.format(information[1]));

        repairsProfitJ = (TextView)findViewById(R.id.RepairsProfit);
        repairsProfitJ.setText("" + numFormat.format(information[3] - information[2] ));

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
}

