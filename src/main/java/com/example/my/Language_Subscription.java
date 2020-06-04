package com.example.my;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class Language_Subscription extends AppCompatActivity {
    DB_Connect cwDbConnect;
    CheckBox checkBox;
    List<String> languages;
    List<CheckBox> items;
    LinearLayout linearLayout;
    Cursor cursor;
    ListView listView;

    ArrayList Language_Subscription;
    ArrayList all_Languages_List;
    ArrayList data_From_Db_List;
    ArrayList checked_Item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_language__subscription );

        linearLayout = findViewById(R.id.layout_for_check_boxes);
        cwDbConnect = new DB_Connect(this);
        cursor = cwDbConnect.getLanguageData();
        Language_Subscription = new ArrayList();
        data_From_Db_List = new ArrayList();
        all_Languages_List = new ArrayList();

        //creating the array list to assign to check box
        languages = new ArrayList();
        languages.add( "Afrikaans" );
        languages.add( "Albanian" );
        languages.add( "Armenian" );
        languages.add( "Azerbaijani" );
        languages.add( "Arabic" );
        languages.add( "Bashkir" );
        languages.add( "Basque" );
        languages.add( "Belarusian");
        languages.add( "Bengali" );
        languages.add( "Bosnian" );
        languages.add( "Bulgarian" );
        languages.add( "Catalan" );
        languages.add( "Central Khmer" );
        languages.add( "Chinese (Simplified)" );
        languages.add( "Chinese (Traditional)" );
        languages.add( "Chuvash" );
        languages.add( "Croatian" );
        languages.add( "Czech" );
        languages.add( "Danish" );
        languages.add( "Dutch" );
        languages.add( "English" );
        languages.add( "Esperanto" );
        languages.add( "Estonian" );
        languages.add( "Finnish" );
        languages.add( "French" );
        languages.add( "Georgian" );
        languages.add( "German" );
        languages.add( "Greek" );
        languages.add( "Gujarati" );
        languages.add( "Haitian" );
        languages.add( "Hebrew" );
        languages.add( "Hindi" );
        languages.add( "Hungarian" );
        languages.add( "Icelandic" );
        languages.add( "Indonesian" );
        languages.add( "Irish" );
        languages.add( "Spanish" );

        //creating the checkbox
        createCheckBox(languages);
        checked_Item = clickListenerForCheckBox();
        data_From_Db_List = retrieveDataFromDB();
        all_Languages_List = gatherAllLanguages();
        onCreateCheckInitially();

    }

    /**
     * creting the checkbox dynamically
     * @param arrLang
     */
    public void createCheckBox(List<String> arrLang) {
        items = new ArrayList<CheckBox>();
        for (int i = 0; i < arrLang.size(); i++) {
            checkBox = new CheckBox(getApplicationContext());
            checkBox.setId(i);
            checkBox.setText(arrLang.get(i));
            items.add(checkBox);
            linearLayout.addView(checkBox);
        }
    }


    //set on onclick listner to assign to checkbox
    public ArrayList clickListenerForCheckBox() {
        final ArrayList checkedItemsArray = new ArrayList();
        for (CheckBox item : items) {
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckBox checkBox = findViewById(view.getId());
                    if (checkBox.isChecked()) {
                        checked_Item.add(checkBox.getText().toString());
                        Toast.makeText(Language_Subscription.this, checkBox.getText().toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        return checkedItemsArray;
    }

    //retrieve data from database
    public ArrayList retrieveDataFromDB() {
        ArrayList dataFromDb = new ArrayList();
        cursor = cwDbConnect.getLanguageData();
        while (cursor.moveToNext()) {
            dataFromDb.add(cursor.getString(1));
        }
        return dataFromDb;
    }

    //passing languages list to all languages list
    public ArrayList gatherAllLanguages() {
        ArrayList allLanguages = new ArrayList();
        for (int i = 0; i < languages.size(); i++) {
            allLanguages.add(languages.get(i));
        }
        return allLanguages;
    }

    //Retrieve the data and assign the to checkboxes
    public void onCreateCheckInitially() {
        for (CheckBox item : items) {
            CheckBox checkBox = item;
            if (data_From_Db_List.contains(checkBox.getText())) {
                System.out.println(item.getText());
                checkBox.setChecked(true);
            }
        }
    }

    //unchecked checkboxes data in database will be deleted by this method
    public void deleteUserUncheckedCheckBox() {
        for (CheckBox item : items) {
            CheckBox checkBox = item;
            if (item.isChecked() == false) {
                cwDbConnect.deleteDataLanguages(item.getText().toString());
            }
        }
    }

    /**
     * On Subscribe Button Click
     * @param view
     */
    //this will write the current checked checkbox items to the database
    public void onButtonClick(View view) {
        for (int i = 0; i < checked_Item.size(); i++) {
            if (data_From_Db_List.contains(checked_Item.get(i))) {
                continue;
            } else {
                cwDbConnect.addedLanguageData(checked_Item.get(i).toString());
            }
        }

        deleteUserUncheckedCheckBox();
        Intent intent = new Intent(Language_Subscription.this, MainActivity.class);
        startActivity(intent);
    }

}
