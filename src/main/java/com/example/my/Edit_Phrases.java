package com.example.my;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Edit_Phrases extends AppCompatActivity {
    DB_Connect cwDbConnect;
    EditText editText;
    ArrayList<String> display_Pha;
    ArrayList languageList;
    ArrayList wordsID;
    RadioGroup radioGroup;
    RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_edit__phrases );

        cwDbConnect = new DB_Connect(this);
        display_Pha = new ArrayList<>();
        editText = (EditText) findViewById(R.id.editTextWord);
        radioGroup = findViewById(R.id.radioGroup);
        editText = findViewById(R.id.editTextWord);
        languageList = new ArrayList();
        wordsID = new ArrayList();

        cwDbConnect = new DB_Connect(this);
        Cursor res = cwDbConnect.displayData();
        while (res.moveToNext()) {
            wordsID.add(res.getInt(0));
            languageList.add(res.getString(1));

        }

        createRadioButton();


    }

    //creating radio button dynamically
    public void createRadioButton() {
        for (int i = 0; i < languageList.size(); i++) {
            radioButton = new RadioButton(this);
            radioButton.setId((Integer) wordsID.get(i));
            radioButton.setText((CharSequence) languageList.get(i));
            radioGroup.addView(radioButton);
        }

    }

    //on edit button click
    public void onEditButtonClick(View view) {
        String userInputText = String.valueOf(editText.getText());
        int wordsID = radioGroup.getCheckedRadioButtonId();
        cwDbConnect.updatePhrase(String.valueOf(wordsID), userInputText);
        Intent intent = new Intent(Edit_Phrases.this, MainActivity.class);
        startActivity(intent);
    }

    //on upadte button click
    public void onSelectButtonClick(View view) {
        int wordsID = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(wordsID);
        editText.setText(radioButton.getText());

    }


}
