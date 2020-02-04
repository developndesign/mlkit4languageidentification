package com.developndesign.mlkit4languageidentification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
import com.google.firebase.ml.naturallanguage.languageid.IdentifiedLanguage;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    TextView text; //String whose language is need to be identified
    TextView languageOfText; // language of string
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.text);
        languageOfText = findViewById(R.id.textlanguage);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                identifyLanguage(text.getText().toString());
            }
        });
    }

    private void identifyLanguage(String string) {
        //1.Create instance of firebase language identifier
        FirebaseLanguageIdentification languageIdentifier = FirebaseNaturalLanguage.getInstance().getLanguageIdentification();
       //2.Pass string to the identify Language Method
        languageIdentifier.identifyLanguage(string)
                .addOnSuccessListener(new OnSuccessListener<String>() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onSuccess(@Nullable String languageCode) {
                                if (languageCode != null) {
                                    if (!languageCode.equals("und")) {
                                        languageOfText.append("Language With High Probability:" + "\n\n");
                                        languageOfText.append("Language Code: " + languageCode + "\n\n\n");
                                    } else {
                                        languageOfText.setText("Can't identify language.");
                                    }
                                }
                            }
                        });
        //3. List of all possible language
        FirebaseLanguageIdentification allPossiblelanguageIdentifier =
                FirebaseNaturalLanguage.getInstance().getLanguageIdentification();
        allPossiblelanguageIdentifier.identifyPossibleLanguages(string).addOnCompleteListener(new OnCompleteListener<List<IdentifiedLanguage>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<List<IdentifiedLanguage>> task) {
                List<IdentifiedLanguage> identifiedLanguageList = task.getResult();
                if (identifiedLanguageList != null) {
                    languageOfText.append("All Possible Language Identified: " + "\n\n");
                    for (IdentifiedLanguage identifiedLanguage : identifiedLanguageList) {
                        String language = identifiedLanguage.getLanguageCode();
                        float confidence = identifiedLanguage.getConfidence();
                        languageOfText.append("Language Code: " + language + " (" + ("" + confidence * 100).subSequence(0, 4) + "%" + ")" + "\n\n");
                    }
                }
            }
        });
    }
}
