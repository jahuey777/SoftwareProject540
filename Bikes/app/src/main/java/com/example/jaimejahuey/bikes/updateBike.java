package com.example.jaimejahuey.bikes;

/**
 * Created by jaimejahuey on 10/12/15.
 */
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class updateBike extends ActionBarActivity
{

    //string that will have the value from whenever the user inputs the serial number in
    //the inventoryManagement class
    String updateSerialNum;

    //Buttons
    private Button colorUpdate;
    private Button conditionUpdate;
    private Button makeUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_bike);

        //linking buttons to actual xml file
        colorUpdate = (Button) findViewById(R.id.UpdateColor);
        conditionUpdate = (Button) findViewById(R.id.UpdateCondition);
        makeUpdate = (Button) findViewById(R.id.UpdateCondition);

        //Actually grabing the value from the static string in inventory managments class.
        //note that this variables are static so that they stay alive throughout the whole program
        //Have to be careful if we use them else where
        updateSerialNum = InventoryManagement.resultSerialNum;

        colorUpdate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //
                int num = 1;



            }
        });

        makeUpdate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int num =2;
            }
        });

        conditionUpdate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int num = 3;
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
