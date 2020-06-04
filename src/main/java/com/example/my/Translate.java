package com.example.my;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ibm.cloud.sdk.core.http.HttpMediaType;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.developer_cloud.android.library.audio.StreamPlayer;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.language_translator.v3.model.TranslationResult;
import com.ibm.watson.language_translator.v3.util.Language;
import com.ibm.watson.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.text_to_speech.v1.model.SynthesizeOptions;

import java.util.ArrayList;
import java.util.List;


public class Translate extends AppCompatActivity {

    private LanguageTranslator translationService;
    private StreamPlayer player = new StreamPlayer();
    private TextToSpeech textService;
    Languages languages;
    DB_Connect cwDbConnect;
    Spinner spinnerUserSelection;
    ArrayList arrayForSpinner;
    String[] languageWithCodes;
    Cursor cursor;
    String translateThisWord;
    ListView listViewAllWords;
    List<String> allWords = new ArrayList<String>();
    TextView textView;
    Button btn_T;
    Button btn_P;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        languages = new Languages();
        cwDbConnect = new DB_Connect(this);
        spinnerUserSelection = findViewById(R.id.spinnerLanguages);
        listViewAllWords = findViewById(R.id.listViewWords);
        btn_T = findViewById(R.id.btnTranslate);
        btn_P = findViewById(R.id.btnPronounce);
        textView = findViewById(R.id.textViewFinalResult);
        arrayForSpinner = new ArrayList();
        cursor = cwDbConnect.getLanguageData();
        while (cursor.moveToNext()) {
            arrayForSpinner.add(cursor.getString(1));
        }
        assignItemsToSpinnerFromLanguagesDB();


        Cursor res = cwDbConnect.displayData();
        while (res.moveToNext()) {
            allWords.add(res.getString(1));

        }

        showListView();

        translationService = initLanguageTranslatorService();
        textService = initTextToSpeechService();

    }

    //assign subscribe languges to spinner from database
    public void assignItemsToSpinnerFromLanguagesDB() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arrayForSpinner);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUserSelection.setAdapter(spinnerArrayAdapter);
    }


    //view the add words
    public void showListView() {
        listViewAllWords = (ListView) findViewById(R.id.listViewWords);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.words, allWords);
        listViewAllWords.setAdapter(adapter);
        listViewAllWords.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) { btn_T.setEnabled(true);
                        translateThisWord = (String) listViewAllWords.getItemAtPosition(position);
                        Toast.makeText(Translate.this, translateThisWord, Toast.LENGTH_SHORT).show();

                    }
                }
        );
    }

    //assign from correct data from language class
    public String findingLanguageCode() {
        languageWithCodes = languages.addLanguageWithCodes();
        String[] languagesFromClass = languageWithCodes;
        String userSelectedValueFromSpinner = spinnerUserSelection.getSelectedItem().toString();
        String code = "";
        for (int i = 0; i < languagesFromClass.length; i++) {
            if ((languagesFromClass[i].split(",")[0]).equals(userSelectedValueFromSpinner)) {
                code = languagesFromClass[i].split(",")[1];
                break;
            }
        }
        return code;
    }

    //assign from ibm key and url and authenticating the api
    private LanguageTranslator initLanguageTranslatorService() {
        Authenticator authenticator = new IamAuthenticator(getString(R.string.language_translator_apikey));
        LanguageTranslator service = new LanguageTranslator("2018-05-01", authenticator);
        service.setServiceUrl(getString(R.string.language_translator_url));
        return service;
    }

    //on click to translation button
    public void onTranslateButtonClick(View view) {
//        System.out.println(translateThisWord);
        new TranslationTask().execute(translateThisWord);
        btn_P.setEnabled(true);
    }

    //on click to pronounce button
    public void onPronounceClick(View view) {
        Translate translate = new Translate();
        new Translate.SynthesisTask().execute( translateThisWord );
    }

    //assign from ibm key and url and authenticating the api
    private com.ibm.watson.text_to_speech.v1.TextToSpeech initTextToSpeechService() {
        Authenticator authenticator = new IamAuthenticator(getString(R.string.text_speech_apikey));
        com.ibm.watson.text_to_speech.v1.TextToSpeech service = new com.ibm.watson.text_to_speech.v1.TextToSpeech( authenticator );
        service.setServiceUrl(getString(R.string.text_speech_url));
        return service;
    }

    //pronounce the word
    private class SynthesisTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            SynthesizeOptions synthesizeOptions = new SynthesizeOptions.Builder().text(params[0]).
                    voice(SynthesizeOptions.Voice.EN_US_LISAVOICE) .accept(HttpMediaType.AUDIO_WAV).build();
            player.playStream(initTextToSpeechService().synthesize(synthesizeOptions).execute()
                    .getResult());
            return "Did synthesize";
        } }

    // translate the word
    private class TranslationTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String firstTranslation = "";
            TranslateOptions translateOptions = new TranslateOptions.Builder().addText(params[0]).source(Language.ENGLISH).target(findingLanguageCode()).build();
            try {
                TranslationResult result = translationService.translate(translateOptions).execute().getResult();
                firstTranslation = result.getTranslations().get(0).getTranslation();
            }catch (Exception ex){
                return " ERROR !!! ";
            }

            return firstTranslation;
        }

        //after all task
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            textView.setText(s);
            Toast.makeText(Translate.this, s, Toast.LENGTH_SHORT).show();
            translateThisWord = s;


        }
    }




}
