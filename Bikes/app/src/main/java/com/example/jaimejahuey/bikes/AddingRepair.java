package com.example.jaimejahuey.bikes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by David on 10/27/2015.
 */
public class AddingRepair extends ActionBarActivity
{
    EditText phoneNumInput;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adding_repair);

        //For the phone number, this addTextChangeListener will automatically add the hyphen
        phoneNumInput = (EditText) findViewById(R.id.addRepairCustomerPhone);
        phoneNumInput.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

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
