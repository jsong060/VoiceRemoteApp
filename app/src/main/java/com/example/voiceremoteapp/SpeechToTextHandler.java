package com.example.voiceremoteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.StringJoiner;

public class SpeechToTextHandler extends AppCompatActivity {

    private ImageView micIcon;
    private TextView commandText;
    private SpeechRecognizer speechRecognizer;
    private String commandTextStr;
    private String cmdToTranslate = "null";
    private CountDownTimer speechTimer;
    private boolean isTranscriptionDone = false;
    private String lastElem;

    public static final int REQUEST_RECORD_AUDIO_PERMISSION = 1;
    public static final char DEFAULT_BT_VALUE = '4';
    public HashMap<String, Character> commandMap = new HashMap<>();
    public HashMap<String, Character> numberMap = new HashMap<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        commandMap.put("up", '0');
        commandMap.put("down", '1');

        numberMap.put("1", '1');
        numberMap.put("2", '2');
        numberMap.put("3", '3');
        numberMap.put("4", '4');
        numberMap.put("5",'5');
        numberMap.put("6",'6');
        numberMap.put("7", '7');
        numberMap.put("8",'8');
        numberMap.put("9",'9');
        numberMap.put("one", '1');
        numberMap.put("two",'2');
        numberMap.put("three",'3');
        numberMap.put("four",'4');
        numberMap.put("five", '5');
        numberMap.put("six",'6');
        numberMap.put("seven", '7');
        numberMap.put("eight", '8');
        numberMap.put("nine", '9');

        //grant permission to mic
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            checkPermission();
        }

        micIcon = (ImageView) findViewById(R.id.imgMic);
        commandText = (TextView) findViewById(R.id.textCommand);
        commandTextStr = commandText.toString();
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                speechIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
                speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                commandText.setText("Ready for transcription!");
            }

            @Override
            public void onBeginningOfSpeech() {
                speechTimer.start();
            }

            @Override
            public void onRmsChanged(float rmsdB) {}

            @Override
            public void onBufferReceived(byte[] buffer) {}

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {}

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> resultsStringArrayList = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                commandTextStr = resultsStringArrayList.get(0);
                commandText.setText(commandTextStr);
                isTranscriptionDone = true;
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                ArrayList<String> partialResultsStringArrayList = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                commandTextStr = partialResultsStringArrayList.get(0);
                commandText.setText(commandTextStr);
            }

            @Override
            public void onEvent(int eventType, Bundle params) {}
        });


        micIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechRecognizer.startListening(speechIntent);

                speechTimer = new CountDownTimer(5000, 100) {

                    public void onTick(long millisUntilFinished) {
                        // Do nothing
                    }

                    public void onFinish() {
                        speechRecognizer.stopListening();
                        transformCommandTextStr(commandTextStr);
                    }
                };
            }
        });
    }

    private void checkPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults != null) {
                // Permission has been granted
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                // Permission denied, handle the case accordingly
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public char[] transformCommandTextStr(String commandTextStr) {
        char[] res = new char[]{'0','0','0'};

        String[] processedStr = commandTextStr.trim().toLowerCase().split(" ");

        if(processedStr.length == 1){
            if(commandMap.containsKey(processedStr[0])){
                res[0] = commandMap.get(processedStr[0]).charValue();
                res[1] = ' ';
                res[2] = DEFAULT_BT_VALUE;
            }
        }else if( processedStr.length > 1){
            for(int i = processedStr.length-2; i>=0; i--){
                if (commandMap.containsKey(processedStr[i])){
                    if(numberMap.containsKey(processedStr[i+1])){
                        res[0] = commandMap.get(processedStr[i]);
                        res[1] = ' ';
                        res[2] = numberMap.get(processedStr[i+1]);

                        break;
                    }else{
                        res[0] = commandMap.get(processedStr[0]);
                        res[1] = ' ';
                        res[2] = DEFAULT_BT_VALUE;

                        break;
                    }
                }
            }
        }
        return res;
    }

    private boolean contains(String[] arr, String target){
        for( String str : arr){
            if (str.equals(target)) return true;
        }

        return false;
    }

    private void resetCmdVars() {
        this.commandTextStr = null;
        this.cmdToTranslate = "null";
        this.speechTimer = null;
        this.lastElem = null;
    }
}