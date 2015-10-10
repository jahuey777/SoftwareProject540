package com.example.jaimejahuey.bikes;

/**
 * Created by jaimejahuey on 9/18/15.
 */
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by jaimejahuey on 9/16/15.
 */
public class Repairs extends ActionBarActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repairs_layout);


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