package com.example.jaimejahuey.bikes;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by jaimejahuey on 10/3/15.
 */
public class addingBike extends ActionBarActivity
{
    private Button addbikeEnter;
    EditText bikeMake;
    EditText bikeColor;
    EditText bikeSerial;

    RadioGroup bikeCondition;
    RadioButton bikeUsed;
    RadioButton bikeNew;
    String bikeCond = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bike);

        bikeSerial= (EditText)findViewById(R.id.addBikeSerialInput);
        bikeMake= (EditText) findViewById(R.id.addBikeMakeInput);
        bikeColor= (EditText) findViewById(R.id.addBikeColor);

        //Setting up radio buttons
        bikeCondition = (RadioGroup)findViewById(R.id.addBikeCondition);
        bikeUsed = (RadioButton)findViewById(R.id.addBikeUsed);
        bikeNew = (RadioButton)findViewById(R.id.addBikeNew);

        addbikeEnter = (Button) findViewById(R.id.addEnterButton);

        //When enter button is pressed we will grab info in the textboxes
        //Check to see if the input is valid and not empty as well.
        addbikeEnter.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Connecting the text boxes from the xml to the editexts in this file

                int selectedCondition = bikeCondition.getCheckedRadioButtonId();
                int check=-1;

                if(selectedCondition==bikeNew.getId())
                {
                    bikeCond = "new";
                    check=0;
                }
                else if(selectedCondition==bikeUsed.getId())
                {
                    bikeCond= "used";
                    check=0;
                }

                String MAKE= bikeMake.getText().toString();
                String SERIAL= bikeSerial.getText().toString();
                String COLOR = bikeColor.getText().toString();

                if(MAKE!=null || SERIAL!=null || COLOR!=null)
                {
                    //Toast.makeText(getApplicationContext(), "Input values are missing.", Toast.LENGTH_LONG).show();

                    if(MAKE.isEmpty()|| COLOR.isEmpty() || SERIAL.isEmpty())
                    {
                        Toast.makeText(getApplicationContext(), "Input values are missing. 1", Toast.LENGTH_LONG).show();
                    }
                    else if(check==-1)
                    {
                        Toast.makeText(getApplicationContext(), "Please select a condition for the bike", Toast.LENGTH_LONG).show();

                    }

                    else
                    {
                        Log.d("HERE", SERIAL);


                        Bike addingThisBike =  new Bike(SERIAL,MAKE, COLOR, bikeCond);
                            Boolean didWeAdd =  MainActivity.DATABASE.addBike(addingThisBike);

                            if(didWeAdd)
                               Toast.makeText(getApplicationContext(), "The Bike has been added.", Toast.LENGTH_LONG).show();
                            else
                               Toast.makeText(getApplicationContext(), "The Bike was not added since it already exists", Toast.LENGTH_LONG).show();


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

}
