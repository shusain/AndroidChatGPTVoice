package com.shauncore.chatbotinterface;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private OpenAIApi openAIApi;
    private SpeechRecognizer speechRecognizer;
    private AIRequest aiRequest;
    private TextView textOutput;
    public static final Integer RecordAudioRequestCode = 1;

    private static final String OPENAI_API_KEY = "SCRUBBED";
    private TextToSpeech textToSpeech;


    private Interceptor createHeaderInterceptor() {
        return new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request requestWithAuthHeader = originalRequest.newBuilder()
                        .header("Authorization", "Bearer " + OPENAI_API_KEY)
                        .build();
                return chain.proceed(requestWithAuthHeader);
            }
        };
    }

    private OkHttpClient createOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(createHeaderInterceptor())
                .callTimeout(30, TimeUnit.SECONDS)
                .build();
    }


    private RecognitionListener recognitionListener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            // Called when the SpeechRecognizer is ready to receive speech input
        }

        @Override
        public void onBeginningOfSpeech() {
            // Called when the user starts speaking
        }

        @Override
        public void onRmsChanged(float rmsdB) {
            // Called to provide an update on the sound level (RMS) in decibels
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            // Called when more sound has been received
        }

        @Override
        public void onEndOfSpeech() {
            // Called when the user stops speaking
        }

        @Override
        public void onError(int error) {
            // Called when an error occurs during speech recognition
            // You can handle specific errors using error codes (e.g., SpeechRecognizer.ERROR_NO_MATCH)
        }

        @Override
        public void onResults(Bundle results) {
            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if (matches != null && !matches.isEmpty()) {
                String text = matches.get(0);
                // Send the text to the OpenAI API
                sendTextToOpenAI(text);
            }
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
            // Called when partial recognition results are available
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
            // Called when a miscellaneous event occurs during speech recognition
        }

        // Implement other necessary methods
    };

    public void initTTS() {
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.getDefault());
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TextToSpeech", "Language not supported");
                    } else {
                        Log.i("TextToSpeech", "TextToSpeech initialized successfully");
                    }
                } else {
                    Log.e("TextToSpeech", "Initialization failed");
                }
            }
        });
    }
    private void speak(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }

        initTTS();

        // Initialize OpenAI API with your API key
        OkHttpClient okHttpClient = createOkHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openai.com/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openAIApi = retrofit.create(OpenAIApi.class);

        // Initialize aiRequest with an initial prompt
        aiRequest = new AIRequest("Assume you are going to be responding to voice to text in a text to voice way so keep responses concise: ", 0.5);

        // Initialize SpeechRecognizer
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(recognitionListener);

        // Set up the button click listener
        Button button = findViewById(R.id.speech_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startListening();
            }
        });


        // Set up the button click listener
        textOutput = findViewById(R.id.text_display);
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},RecordAudioRequestCode);
        }
    }

    private void startListening() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        speechRecognizer.startListening(intent);
    }

    private void sendTextToOpenAI(String text) {
        aiRequest.appendToPrompt(new AIMessage(text));
        Log.i("OpenAIApi", "Prompt received: " + text);
        openAIApi.getCompletion(aiRequest).enqueue(new Callback<AIResponse>() {
            @Override
            public void onResponse(Call<AIResponse> call, Response<AIResponse> response) {
                if (response.isSuccessful()) {
                    AIResponse aiResponse = response.body();
                    // Process the AI response
                    // ...
                    // Append the AI response to the conversation history
                    AIResponseChoices choice = aiResponse.getChoices().get(0);
                    String aiResponseText = choice.getMessage().getContent();
                    speak(aiResponseText);
                    aiRequest.appendToPrompt(choice);
                    textOutput.setText(aiResponseText);
                } else {
                    // Handle failure
                    textOutput.setText("Error in API call" + response.errorBody().toString());
                    Log.e("OpenAIApi", "Error return from OpenAI API call: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<AIResponse> call, Throwable t) {
                // Handle network failure, e.g., display an error message
                Log.e("OpenAIApi", "Error communicating with the API", t);
            }
        });
    }
}
