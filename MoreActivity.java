package com.example.myapplicationapp;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MoreActivity extends AppCompatActivity {

    TextView infoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        // Initialize TextViews
        TextView frenchText = findViewById(R.id.frenchText);
        TextView germanText = findViewById(R.id.germanText);
        TextView spanishText = findViewById(R.id.spanishText);
        TextView japaneseText = findViewById(R.id.japaneseText);
        infoText = findViewById(R.id.infoText);

        // Set Click Listeners
        frenchText.setOnClickListener(view -> showLanguageInfo("French"));
        germanText.setOnClickListener(view -> showLanguageInfo("German"));
        spanishText.setOnClickListener(view -> showLanguageInfo("Spanish"));
        japaneseText.setOnClickListener(view -> showLanguageInfo("Japanese"));
    }

    private void showLanguageInfo(String language) {
        String info;
        switch (language) {
            case "French":
                info = "• Spoken by over 300 million people worldwide, French is an official language in 29 countries.\n" +
                        "• Originates from Latin and has influences from Germanic and English languages.\n" +
                        "• Used in diplomacy, the UN, EU, and the Olympic Games.\n" +
                        "• Uses Latin script with accents such as é, è, à, ô, and ç.";
                break;
            case "German":
                info = "• Spoken by over 130 million people, German is the most widely spoken native language in Europe.\n" +
                        "• Official language of Germany, Austria, Switzerland, and parts of Italy and Belgium.\n" +
                        "• Known for long compound words like 'Donaudampfschifffahrtsgesellschaft'.\n" +
                        "• Uses Latin script with umlauts (ä, ö, ü) and Eszett (ß).";
                break;
            case "Spanish":
                info = "• Spanish is spoken by over 500 million people and is the official language in 21 countries.\n" +
                        "• Evolved from Latin with strong Arabic influences during the Moorish rule in Spain.\n" +
                        "• Known for phonetic spelling and simple pronunciation.\n" +
                        "• Uses Latin script with accents such as á, é, í, ó, ú, and ñ.";
                break;
            case "Japanese":
                info = "• Spoken by over 125 million people, primarily in Japan.\n" +
                        "• Uses three writing systems: Hiragana, Katakana, and Kanji.\n" +
                        "• Has a complex honorific system reflecting social hierarchy.\n" +
                        "• Borrowed many words from Chinese and English.";
                break;
            default:
                info = "Language information not available.";
        }
        infoText.setText(info);
    }
}
