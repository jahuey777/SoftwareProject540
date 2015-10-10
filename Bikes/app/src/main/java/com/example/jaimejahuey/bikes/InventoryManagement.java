package com.example.jaimejahuey.bikes;

/**
 * Created by jaimejahuey on 9/18/15.
 */
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by jaimejahuey on 9/16/15.
 */
public class InventoryManagement extends ActionBarActivity
{
    //Buttons of the inventory interface menu
    private Button addBike;


    private Button removeBike;
    private EditText removeSerialText;
    final Context context = this;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_layout);

        //Linking to the XML file
        addBike = (Button) findViewById(R.id.AddBike);
        removeBike = (Button) findViewById(R.id.RemoveBike);

        removeSerialText = (EditText) findViewById(R.id.remove_bike_dialog_input);

        addBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(InventoryManagement.this , addingBike.class);

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
