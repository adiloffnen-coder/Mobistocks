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
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class ImportFileActivity extends Activity {

    private static final int PICK_EXCEL = 100;

    private TextView fileNameText;
    private Button importButton;

    private Uri selectedFileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_file);

        fileNameText = findViewById(R.id.fileNameText);
        importButton = findViewById(R.id.importButton);

        Button selectFileButton =
                findViewById(R.id.selectFileButton);

        Button cancelButton =
                findViewById(R.id.cancelButton);

        importButton.setEnabled(false);

        // ==========================================
        // CHOISIR LE FICHIER EXCEL
        // ==========================================

        selectFileButton.setOnClickListener(v -> {

            Intent intent =
                    new Intent(Intent.ACTION_OPEN_DOCUMENT);

            intent.addCategory(Intent.CATEGORY_OPENABLE);

            intent.setType(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            );

            startActivityForResult(
                    intent,
                    PICK_EXCEL
            );
        });

        // ==========================================
        // IMPORTER
        // ==========================================

        importButton.setOnClickListener(v -> {

            if (selectedFileUri == null) {

                Toast.makeText(
                        this,
                        "Veuillez sélectionner un fichier Excel.",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            importExcelFile();
        });

        // ==========================================
        // ANNULER
        // ==========================================

        cancelButton.setOnClickListener(v -> finish());
    }

    // ==============================================
    // RETOUR DU SÉLECTEUR DE FICHIER
    // ==============================================

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data) {

        super.onActivityResult(
                requestCode,
                resultCode,
                data
        );

        if (requestCode == PICK_EXCEL
                && resultCode == RESULT_OK
                && data != null) {

            selectedFileUri = data.getData();

            if (selectedFileUri != null) {

                String fileName =
                        getFileName(selectedFileUri);

                fileNameText.setText(
                        fileName
                );

                importButton.setEnabled(true);
            }
        }
    }

    // ==============================================
    // IMPORTATION DU FICHIER
    // ==============================================

    private void importExcelFile() {

        try {

            File destination =
                    new File(
                            getFilesDir(),
                            "Stocks.xlsx"
                    );

            InputStream inputStream =
                    getContentResolver()
                            .openInputStream(
                                    selectedFileUri
                            );

            if (inputStream == null) {

                Toast.makeText(
                        this,
                        "Impossible d'ouvrir le fichier.",
                        Toast.LENGTH_LONG
                ).show();

                return;
            }

            FileOutputStream outputStream =
                    new FileOutputStream(destination);

            byte[] buffer =
                    new byte[8192];

            int length;

            while ((length =
                    inputStream.read(buffer)) > 0) {

                outputStream.write(
                        buffer,
                        0,
                        length
                );
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();

            Toast.makeText(
                    this,
                    "Fichier Excel importé avec succès.",
                    Toast.LENGTH_LONG
            ).show();

            // Retour à l'écran précédent
            finish();

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Erreur lors de l'importation du fichier Excel.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    // ==============================================
    // NOM DU FICHIER
    // ==============================================

    private String getFileName(Uri uri) {

        String result = null;

        if ("content".equals(uri.getScheme())) {

            try (Cursor cursor =
                         getContentResolver().query(
                                 uri,
                                 null,
                                 null,
                                 null,
                                 null)) {

                if (cursor != null
                        && cursor.moveToFirst()) {

                    int nameIndex =
                            cursor.getColumnIndex(
                                    OpenableColumns.DISPLAY_NAME
                            );

                    if (nameIndex >= 0) {

                        result =
                                cursor.getString(
                                        nameIndex
                                );
                    }
                }
            }
        }

        if (result == null) {

            result =
                    uri.getLastPathSegment();
        }

        return result != null
                ? result
                : "Fichier Excel";
    }
}
