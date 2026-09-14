package com.ocp.consultationstocks;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.database.Cursor;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ImportFileActivity extends Activity {

    private static final int PICK_FILE = 100;

    private TextView fileNameText;
    private Button importButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_file);

        fileNameText = findViewById(R.id.fileNameText);
        importButton = findViewById(R.id.importButton);

        Button selectFileButton = findViewById(R.id.selectFileButton);
        Button cancelButton = findViewById(R.id.cancelButton);

        selectFileButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            startActivityForResult(intent, PICK_FILE);
        });

        importButton.setOnClickListener(v -> {
            // L'import réel sera ajouté à l'étape suivante.
        });

        cancelButton.setOnClickListener(v -> finish());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE && resultCode == RESULT_OK && data != null) {

            Uri fileUri = data.getData();

            if (fileUri != null) {
                String fileName = getFileName(fileUri);

                fileNameText.setText(fileName);
                importButton.setEnabled(true);
            }
        }
    }

    private String getFileName(Uri uri) {

        String result = null;

        if ("content".equals(uri.getScheme())) {

            try (Cursor cursor = getContentResolver().query(
                    uri,
                    null,
                    null,
                    null,
                    null)) {

                if (cursor != null && cursor.moveToFirst()) {

                    int nameIndex =
                            cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);

                    if (nameIndex >= 0) {
                        result = cursor.getString(nameIndex);
                    }
                }
            }
        }

        if (result == null) {
            result = uri.getLastPathSegment();
        }

        return result != null ? result : "Fichier sélectionné";
    }
}
