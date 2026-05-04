package com.example.lab4_inclass;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText searchInput;
    private Button searchButton;
    private TextView resultLabel;
    private TextView resultText;
    private DictionaryOpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DictionaryOpenHelper(this);

        searchInput = findViewById(R.id.searchInput);
        searchButton = findViewById(R.id.searchButton);
        resultLabel = findViewById(R.id.resultLabel);
        resultText = findViewById(R.id.resultText);

        searchButton.setOnClickListener(v -> performSearch());

        searchInput.setOnEditorActionListener((v, actionId, event) -> {
            performSearch();
            return true;
        });
    }

    private void performSearch() {
        String query = searchInput.getText().toString().trim().toLowerCase();

        if (query.isEmpty()) {
            Toast.makeText(this, "Please enter a word", Toast.LENGTH_SHORT).show();
            return;
        }

        hideKeyboard();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor exactCursor = db.query(
                DictionaryOpenHelper.TABLE_NAME,
                null,
                "LOWER(word) = ?",
                new String[]{query},
                null, null, null
        );

        if (exactCursor.moveToFirst()) {
            String definition = exactCursor.getString(
                    exactCursor.getColumnIndexOrThrow(DictionaryOpenHelper.COL_DEFINITION)
            );
            resultLabel.setText("Definition:");
            resultText.setText(exactCursor.getString(
                    exactCursor.getColumnIndexOrThrow(DictionaryOpenHelper.COL_WORD)
            ) + "\n\n" + definition);
            exactCursor.close();
        } else {
            exactCursor.close();

            Cursor substringCursor = db.query(
                    DictionaryOpenHelper.TABLE_NAME,
                    new String[]{DictionaryOpenHelper.COL_WORD},
                    "LOWER(word) LIKE ?",
                    new String[]{"%" + query + "%"},
                    null, null,
                    DictionaryOpenHelper.COL_WORD + " ASC"
            );

            if (substringCursor.moveToFirst()) {
                StringBuilder results = new StringBuilder();
                int count = 0;
                int wordIndex = substringCursor.getColumnIndexOrThrow(DictionaryOpenHelper.COL_WORD);

                do {
                    count++;
                    results.append(count).append(". ").append(
                            substringCursor.getString(wordIndex)
                    ).append("\n");
                } while (substringCursor.moveToNext());

                resultLabel.setText("Words containing \"" + query + "\" (" + count + " found):");
                resultText.setText(results.toString());
            } else {
                resultLabel.setText("");
                resultText.setText("No words found for \"" + query + "\"");
            }
            substringCursor.close();
        }

        db.close();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
    }
}
