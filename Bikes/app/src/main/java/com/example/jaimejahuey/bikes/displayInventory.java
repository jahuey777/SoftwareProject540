package com.example.jaimejahuey.bikes;

/**
 * Created by Alfonso on 10/11/2015.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;


public class displayInventory extends Activity {

    //creates variables
    private TableLayout table_layout;
    MySQLiteHelper mySQL;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_inventory); //sets which xml to set
        mySQL = MainActivity.DATABASE; //sets the database to the one already created
        table_layout = (TableLayout)findViewById(R.id.displayInventoryTable);
        BuildTable();   //builds the dynamic table
    }

    private void BuildTable(){


        Cursor c = mySQL.readInventoryEntry();   //a pointer for the database table
        int rows = c.getCount();        //gets the number of rows in the table
        int columns = c.getColumnCount();   //gets the number of columns in the table


        c.moveToFirst();    //move the pointer to the first in the table

        //outer FOR loop
        for(int i = 0 ; i<rows; i++){
            TableRow row = new TableRow(this); //creates new table row
            row.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                //sets the layout parameters for the new row

            //inner FOR loop
            for(int j = 0; j<columns;j++){
                TextView textV = new TextView(this); //creates a text view for each of the columns
                // the following lines set the layout parameters for each text view


                if (i==0 && j==0)
                {   formatEntry(textV,15);
                    textV.setText("Serial");
                    row.addView(textV);

                    textV = new TextView(this);
                    formatEntry(textV,15);
                    textV.setText("Make");
                    row.addView(textV);

                    textV = new TextView(this);
                    formatEntry(textV,15);
                    textV.setText("Color");
                    row.addView(textV);

                    textV = new TextView(this);
                    formatEntry(textV,15);
                    textV.setText("Condition");
                    row.addView(textV);

                    textV = new TextView(this); //THis is a workaround to make the table show all needed headers, it shouldn't show up as the table doesnt like showing the last of the headers.
                    textV.setText("");
                    row.addView(textV);

                    table_layout.addView(row);
                    textV.setText("");
                    row = new TableRow(this);
                }

                formatEntry(textV,18);
                //sets what the text view says
                textV.setText(c.getString(j));

                if(textV.getText().equals("0"))
                { //if its unavailable, skip the row in the database
                    textV.setText("");
                    j = columns + 1;
                }
                else if(textV.getText().equals("1"))
                {//if its available, dont print out the "1"
                    textV.setText("");
                }
                else
                {
                row.addView(textV); //adds the text to the column space
                }
            }
            //end inner FOR loop

            c.moveToNext(); //moves to the next entry in the database
            table_layout.addView(row);  //adds the row to the dynamic table
        }
        //end outer FOR loop
        c.close();
        mySQL.close();
    }

//creates a loading screen, for slower phones,
    private class MyAsync extends AsyncTask<Void, Void, Void>{

    //do the following before displaying the table
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            table_layout.removeAllViews();
            progressDialog = new ProgressDialog(displayInventory.this);
            progressDialog.setTitle("Please Wait");
            progressDialog.setMessage("Loading all bikes");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

    //do the following
        @Override
        protected Void doInBackground(Void... params){
            return null;
        }

    //once the table loads, dismiss the loading screen
        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            BuildTable();
            progressDialog.dismiss();
        }
    }//End MyAsyn class

private void formatEntry ( TextView textV, int textSize){ //method to format the entries
    textV.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    textV.setGravity(Gravity.CENTER);
    textV.setTextSize(textSize);
    textV.setPadding(0, 5, 0, 5);
}

}//End display Inventory class
