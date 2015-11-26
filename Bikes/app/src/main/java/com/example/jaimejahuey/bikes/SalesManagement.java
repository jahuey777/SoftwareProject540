package com.example.jaimejahuey.bikes;

/**
 * Created by jaimejahuey on 9/18/15.
 */
import android.app.Activity;
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

public class SalesManagement extends Activity
{
    private ImageButton ViewProfitButton;
    private ImageButton SoldBikeButton;
    private ImageButton ViewSoldBikes;

    Context context= this;
    EditText soldSerial;
    private static String soldSerialString;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_layout);

        SoldBikeButton = (ImageButton) findViewById(R.id.SoldBike);
        ViewProfitButton = (ImageButton) findViewById(R.id.ViewProfit);
        ViewSoldBikes = (ImageButton) findViewById(R.id.ViewSoldBikes);

        //Takes us to the sell a bike page.
        ViewProfitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.d("here", "here");
                Intent i = new Intent(SalesManagement.this, viewProfit.class);

                startActivity(i);
            }
        });

        ViewSoldBikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.d("here", "here");
                Intent i = new Intent(SalesManagement.this, viewSoldBikes.class);

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
                Intent i = new Intent(SalesManagement.this , soldBike.class);

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