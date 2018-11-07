package com.example.tan.tts;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ImageButton mic;
    TextView resultText;

    TextToSpeech toSpeech;
    int result;
    EditText speakerText;
    String text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mic = findViewById(R.id.btnMic);
        resultText = findViewById(R.id.resultText);
        speakerText = findViewById(R.id.speakerText);

        toSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS)
                {
                    result = toSpeech.setLanguage(Locale.getDefault());
                }
                else {
                    Toast.makeText(MainActivity.this,
                            "Feature not support in your device", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onMic(View view) {
        promptSpeechInput();
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something!");
        try{
            startActivityForResult(intent, 100);
        }
        catch (ActivityNotFoundException ax){
            Toast.makeText(this, "Sorry, your device doesn't support speach language",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void onActivityResult(int request_code, int result_code, Intent intent){
        super.onActivityResult(request_code, result_code, intent);
        switch(request_code){
            case 100: if(result_code == RESULT_OK && intent != null)
            {
                ArrayList<String> result = intent.getStringArrayListExtra
                        (RecognizerIntent.EXTRA_RESULTS);
                resultText.setText(result.get(0));
            }
            break;
        }
    }

    public void TTS(View view) {
        switch (view.getId())
        {
            case R.id.btnSpeak:
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
                {
                    Toast.makeText(this, "Feature not supported in device", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    text = speakerText.getText().toString();
                    toSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                }
                break;
            case R.id.btnStop:
                if(toSpeech != null)
                {
                    toSpeech.stop();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (toSpeech != null)
        {
            toSpeech.stop();
            toSpeech.shutdown();
        }
    }

    public void onClear(View view) {
    }
}
