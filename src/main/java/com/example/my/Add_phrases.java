package com.example.my;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Add_phrases extends AppCompatActivity {
    DB_Connect cwDbConnect;
    EditText editText;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_phrases );

        editText = (EditText)findViewById(R.id.editText);
        save = (Button)findViewById(R.id.save_01);
        cwDbConnect = new DB_Connect(this);
        savePharse();
    }

    public void savePharse(){
        save.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isSaved;
                        String textVal = editText.getText().toString();
                        //checking the user input null values or not
                        if (textVal.equals("") || textVal.equals(null)){
                            isSaved = false;
                        }else{
                            isSaved = cwDbConnect.savePhrase(editText.getText().toString() );
                        }

                        if(isSaved){
                            Toast.makeText(Add_phrases.this,"Add Phrases Sucessfully",Toast.LENGTH_LONG).show();}
                        else {
                            Toast.makeText(Add_phrases.this,"Phrases Not Added Sucessfully ",Toast.LENGTH_LONG).show();}

                    }
                }
        );
    }


}
