package com.example.jaimejahuey.bikes;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;



public class MainActivity extends ActionBarActivity
{

    //Buttons on main menu of interface
    private Button inventoryButton;
    private Button salesButton;
    private Button repairsButton;

    //The database is public so we can access it in the other files.
    public static MySQLiteHelper DATABASE;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DATABASE = new MySQLiteHelper(this);
        //inventoryButton is object created in this class and refers to the actual InventoryButton
        //in the interface.
        inventoryButton = (Button) findViewById(R.id.InventoryButton);

        inventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(MainActivity.this, InventoryManagement.class);

                startActivity(i);
            }
        });

        salesButton = (Button) findViewById(R.id.SalesButton);

        salesButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(MainActivity.this, SalesManagement.class);

                startActivity(i);
            }
        });

        repairsButton = (Button) findViewById(R.id.RepairsButton);

        repairsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(MainActivity.this, Repairs.class);

                startActivity(i);
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
}

