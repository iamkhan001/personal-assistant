package com.csjbot.speechtotext;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView txtSpeechInput,txtAnswer;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private TextToSpeech toSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        txtSpeechInput =  findViewById(R.id.txtSpeechInput);
        txtAnswer = findViewById(R.id.txtAnswer);
        btnSpeak =  findViewById(R.id.btnSpeak);


        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });


        toSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    toSpeech.setLanguage(Locale.US);
                }
            }
        });

    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));
                    String answer = getAnswers(result.get(0).toLowerCase());
                    txtAnswer.setText(answer);
                    toSpeech.speak(answer, TextToSpeech.QUEUE_FLUSH, null,"");


                    //after some time set tap on mic text on txtSpeechInput
                    Handler handler = new Handler();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            txtSpeechInput.setText(getResources().getString(R.string.tap_on_mic));
                        }
                    };
                    // 3000 milli seconds = 3 seconds
                    handler.postDelayed(runnable,3000);

                }
                break;
            }

        }
    }

    private String getAnswers(String question) {


        switch (question){
            case "do you have a car":
            case "do you have car":
            case "What is your car name":
                return "I have Audi S5 car";

            case "what is color of your car":
            case "what is colour of your car":
            case "what is the colour of this car":
                return "I have red Audi S5 car";

            case "what is the speed of your car":
            case "what is the top speed of your car":
            case "what is top speed of your car":
                return "my car's top speed is 300 kilo miters per hour";

            case "do you like cats or dogs":
            case "do you like dogs or cats":
            case "do you like dogs":
            case "do you like cats":
                return "I like cats but i don't like dogs";

            case "what is your name":
              return "My name is robot";

            case "good morning":
                return "Good morning, Have a nice day";

            case "good afternoon":
                return "Good afternoon, Have a nice day";

            case "good night":
                return "Good night, Sweet Dreams";

            case "sing a song":
            case "sing a song for me":
            case "can you sing a song":
            case "can you sing song for me":
                return "sure! let me try. "+"Baa, baa black sheep\n" +
                        "Have you any wool\n" +
                        "Yes sir, yes sir\n" +
                        "Three bags full.\n" +
                        "\n" +
                        "One for my master\n" +
                        "And one for my dame\n" +
                        "And one for the little boy\n" +
                        "Who lives down the lane.";
            default:
                return "Sorry, I cannot understand your question";

        }


    }

    private List<String> getQuestions(){
        List<String> questions = new ArrayList<>();

        questions.add("what is your name");
        questions.add("Tell me my car name");
        questions.add("what is your name");
        questions.add("do yo like dogs");


        return questions;
    }


}
