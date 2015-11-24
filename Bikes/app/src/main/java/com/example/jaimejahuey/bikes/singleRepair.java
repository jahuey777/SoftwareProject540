package com.example.jaimejahuey.bikes;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by jaimejahuey on 11/24/15.
 */
public class singleRepair extends Activity
{
    TextView custName;
    TextView custPhone;
    TextView dueDate;
    TextView costRepair;
    TextView costAmount;
    TextView dateCompleted;

    Button CALL;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_repair);

        String serial = getIntent().getExtras().getString("Serial");

        final String [] CustInfo = new String [6];

        MainActivity.DATABASE.repairInfo(serial, CustInfo);

        custName = (TextView) findViewById(R.id.CUSTOMERNAME);
        custPhone = (TextView) findViewById(R.id.CUSTOMERPHONE);
        dueDate = (TextView) findViewById(R.id.DUEDATE);
        costRepair = (TextView) findViewById(R.id.COSTREPAIR);
        costAmount = (TextView) findViewById(R.id.AMOUNTCHARGED);
        dateCompleted = (TextView) findViewById(R.id.DATECOMPLETED);

        CALL = (Button) findViewById(R.id.callButton);

        custName.setText("" + CustInfo[0]);
        custPhone.setText("" + CustInfo[1]);
        dueDate.setText(""+ CustInfo[2]);

        if(CustInfo[3] == null)
        {
            costRepair.setText("Uncompleted");
        }
        else
        costRepair.setText("" + CustInfo[3]);

        if(CustInfo[4] == null)
        {
            costAmount.setText("Uncompleted");
        }
        else
        costAmount.setText("" + CustInfo[4]);

        if(CustInfo[5] == null)
        {
            dateCompleted.setText("Uncompleted");
        }
        else
            dateCompleted.setText("" + CustInfo[5]);

        CALL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + CustInfo[1]));
                startActivity(callIntent);
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void call(String num)
    {

        Intent in = new Intent(Intent.ACTION_CALL, Uri.parse("8643812982"));

        try
        {
            startActivity(in);

        }
        catch (ActivityNotFoundException ex)
        {
            Toast.makeText(getApplicationContext(), "yourActivity is not founded", Toast.LENGTH_SHORT).show();
        }

    }
}

