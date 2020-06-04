package com.example.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    DB_Connect cwDbConnect;
    public Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        button = findViewById(R.id.AddPhrases);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Add_Phrases();
            }
        });

        button = findViewById(R.id.DisplayPhrases);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Display_Phrases();
            }
        });

        button = findViewById(R.id.EditPhrases);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Edit_Phrases();
            }
        });

        button = findViewById(R.id.LanguageSubscription);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Language_Subscription();
            }
        });

        button = findViewById(R.id.Translate);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Translate();
            }
        });
    }

    public void Add_Phrases(){
        Intent intent = new Intent(MainActivity.this,Add_phrases.class);
        startActivity(intent);
    }

    public void Display_Phrases(){
        Intent intent = new Intent (MainActivity.this,Display_Phrases.class);
        startActivity(intent);
    }

    public void Edit_Phrases(){
        Intent intent = new Intent (MainActivity.this,Edit_Phrases.class);
        startActivity(intent);
    }

    public void Language_Subscription(){
        Intent intent = new Intent(MainActivity.this,Language_Subscription.class);
        startActivity(intent);
    }

    public void Translate(){
        Intent intent = new Intent(MainActivity.this,Translate.class);
        startActivity(intent);
    }
}
