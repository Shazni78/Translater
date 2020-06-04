package com.example.my;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Display_Phrases extends AppCompatActivity {
    DB_Connect cwDbConnect;
    ArrayList<String> display_Pharase;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_display__phrases );
        cwDbConnect = new DB_Connect(this);
        ContentValues contentValues = new ContentValues();
        display_Pharase=new ArrayList<>();
        listView = (ListView)findViewById(R.id.li);
        displayData();
    }

    //dispaly the data
    public void displayData() {
        Cursor cur = cwDbConnect.displayData();
        if(cur.getCount() == 0) {
            Toast.makeText(Display_Phrases.this,"No Phrases to Display",Toast.LENGTH_LONG).show();
        }else{
            while(cur.moveToNext()) {
                display_Pharase.add(cur.getString(1));
            }

            ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,display_Pharase);
            listView.setAdapter(adapter);

            //sorth the data to alphetabical order
            Collections.sort(display_Pharase, new Comparator<String>(){
                @Override
                public int compare(String t1, String t2) {
                    return t1.compareToIgnoreCase(t2);
                }
            });

        }

    }


}
