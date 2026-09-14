package com.ocp.consultationstocks;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ImportFileActivity extends Activity {

    private static final int PICK_FILE = 100;

    private TextView fileNameText;
    private TextView progressPercent;
    private TextView progressStatus;
    private TextView rowsStatus;

    private Button importButton;
    private Button selectFileButton;

    private ProgressBar importProgress;

    private Uri selectedFileUri;

    private final ExecutorService executor =
            Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_import_file);

        fileNameText = findViewById(R.id.fileNameText);
        progressPercent = findViewById(R.id.progressPercent);
        progressStatus = findViewById(R.id.progressStatus);
        rowsStatus = findViewById(R.id.rowsStatus);

        importProgress = findViewById(R.id.importProgress);

        importButton = findViewById(R.id.importButton);
        selectFileButton = findViewById(R.id.selectFileButton);

        Button cancelButton = findViewById(R.id.cancelButton);

        selectFileButton.setOnClickListener(v -> choisirFichier());

        importButton.setOnClickListener(v -> {

            if (selectedFileUri != null) {
                commencerImport();
            }
        });

        cancelButton.setOnClickListener(v -> finish());
    }

    private void choisirFichier() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        intent.addCategory(Intent.CATEGORY_OPENABLE);

        intent.setType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        );

        startActivityForResult(intent, PICK_FILE);
    }

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

        if (requestCode == PICK_FILE
                && resultCode == RESULT_OK
                && data != null) {

            Uri uri = data.getData();

            if (uri != null) {

                selectedFileUri = uri;

                String fileName = getFileName(uri);

                fileNameText.setText(fileName);

                importButton.setEnabled(true);

                progressPercent.setText("0 %");

                progressStatus.setText(
                        "Fichier prêt pour l'importation"
                );

                rowsStatus.setText("0 / 0 lignes");
            }
        }
    }

    private void commencerImport() {

        importButton.setEnabled(false);
        selectFileButton.setEnabled(false);

        progressStatus.setText(
                "Importation en cours..."
        );

        progressPercent.setText("0 %");
        rowsStatus.setText("0 / 0 lignes");

        final Uri uri = selectedFileUri;

        executor.execute(() -> {

            try {

                importerExcel(uri);

            } catch (Exception e) {

                runOnUiThread(() -> {

                    importButton.setEnabled(true);
                    selectFileButton.setEnabled(true);

                    progressStatus.setText(
                            "Erreur pendant l'importation"
                    );

                    Toast.makeText(
                            ImportFileActivity.this,
                            "Erreur : " + e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                });
            }
        });
    }

    private void importerExcel(Uri uri) throws Exception {

        java.io.File tempFile =
                java.io.File.createTempFile(
                        "mobistock_",
                        ".xlsx",
                        getCacheDir()
                );

        try {

            copierFichierDansCache(uri, tempFile);

            int totalRows =
                    compterLignesExcel(tempFile);

            runOnUiThread(() -> {

                rowsStatus.setText(
                        "0 / " + totalRows + " lignes"
                );
            });

            StockDatabaseHelper dbHelper =
                    new StockDatabaseHelper(
                            ImportFileActivity.this
                    );

            dbHelper.clearStock();

            int importedRows = 0;

            ZipFile zipFile =
                    new ZipFile(tempFile);

            try {

                Map<String, String> sharedStrings =
                        lireSharedStrings(zipFile);

                ZipEntry sheetEntry =
                        zipFile.getEntry(
                                "xl/worksheets/sheet1.xml"
                        );

                if (sheetEntry == null) {
                    throw new Exception(
                            "Feuille Excel introuvable"
                    );
                }

                InputStream inputStream =
                        zipFile.getInputStream(sheetEntry);

                XmlPullParserFactory factory =
                        XmlPullParserFactory.newInstance();

                factory.setNamespaceAware(true);

                XmlPullParser parser =
                        factory.newPullParser();

                parser.setInput(
                        inputStream,
                        StandardCharsets.UTF_8.name()
                );

                ArrayList<String> row =
                        new ArrayList<>();

                int eventType = parser.getEventType();

                String currentCellReference = null;
                String currentCellType = null;
                String currentValue = "";

                while (eventType != XmlPullParser.END_DOCUMENT) {

                    if (eventType == XmlPullParser.START_TAG) {

                        String tag = parser.getName();

                        if ("row".equals(tag)) {

                            row.clear();

                        } else if ("c".equals(tag)) {

                            currentCellReference =
                                    parser.getAttributeValue(
                                            null,
                                            "r"
                                    );

                            currentCellType =
                                    parser.getAttributeValue(
                                            null,
                                            "t"
                                    );

                            currentValue = "";

                        } else if ("v".equals(tag)
                                || "t".equals(tag)) {

                            currentValue =
                                    parser.nextText();
                        }
                    }

                    else if (eventType == XmlPullParser.END_TAG) {

                        String tag = parser.getName();

                        if ("c".equals(tag)) {

                            String value =
                                    convertirValeurCellule(
                                            currentValue,
                                            currentCellType,
                                            sharedStrings
                                    );

                            int column =
                                    colonneDepuisReference(
                                            currentCellReference
                                    );

                            while (row.size() <= column) {
                                row.add("");
                            }

                            row.set(column, value);
                        }

                        else if ("row".equals(tag)) {

                            if (importedRows > 0) {

                                importerLigne(
                                        dbHelper,
                                        row
                                );
                            }

                            importedRows++;

                            final int current =
                                    importedRows;

                            final int total =
                                    totalRows;

                            int percent = 0;

                            if (total > 0) {
                                percent =
                                        (int)
                                        ((current * 100L)
                                                / total);
                            }

                            final int finalPercent =
                                    Math.min(
                                            percent,
                                            100
                                    );

                            runOnUiThread(() -> {

                                importProgress.setProgress(
                                        finalPercent
                                );

                                progressPercent.setText(
                                        finalPercent + " %"
                                );

                                rowsStatus.setText(
                                        current
                                                + " / "
                                                + total
                                                + " lignes"
                                );

                                progressStatus.setText(
                                        "Importation en cours..."
                                );
                            });
                        }
                    }

                    eventType = parser.next();
                }

                inputStream.close();

            } finally {

                zipFile.close();
            }

            dbHelper.close();

            final int result =
                    importedRows - 1;

            runOnUiThread(() -> {

                importProgress.setProgress(100);

                progressPercent.setText("100 %");

                rowsStatus.setText(
                        result + " / " + result + " lignes"
                );

                progressStatus.setText(
                        "Importation terminée"
                );

                importButton.setEnabled(true);
                selectFileButton.setEnabled(true);

                Toast.makeText(
                        ImportFileActivity.this,
                        result
                                + " lignes importées avec succès",
                        Toast.LENGTH_LONG
                ).show();
            });

        } finally {

            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    private void importerLigne(
            StockDatabaseHelper db,
            ArrayList<String> row) {

        String codeSap =
                get(row, 0);

        String codeOcp =
                get(row, 1);

        String description =
                get(row, 2);

        String numeroMagasin =
                get(row, 3);

        String division =
                get(row, 4);

        String magasin =
                get(row, 5);

        String bin =
                get(row, 6);

        String typeMagasin =
                get(row, 7);

        double quantite =
                convertirDouble(
                        get(row, 8)
                );

        String unite =
                get(row, 9);

        String typeStock =
                get(row, 10);

        String designationTypeStock =
                get(row, 11);

        String groupeValorisation =
                get(row, 12);

        double prix =
                convertirDouble(
                        get(row, 13)
                );

        double valeur =
                convertirDouble(
                        get(row, 14)
                );

        String devise =
                get(row, 15);

        String dateEm =
                get(row, 16);

        String derniereSortie =
                get(row, 17);

        db.insertStock(
                codeSap,
                codeOcp,
                description,
                numeroMagasin,
                division,
                magasin,
                bin,
                typeMagasin,
                quantite,
                unite,
                typeStock,
                designationTypeStock,
                groupeValorisation,
                prix,
                valeur,
                devise,
                dateEm,
                derniereSortie
        );
    }

    private String get(
            ArrayList<String> row,
            int index) {

        if (index < 0 || index >= row.size()) {
            return "";
        }

        String value = row.get(index);

        return value == null ? "" : value.trim();
    }

    private double convertirDouble(String value) {

        if (value == null || value.trim().isEmpty()) {
            return 0;
        }

        try {

            String propre =
                    value.trim()
                            .replace(" ", "")
                            .replace(",", ".");

            return Double.parseDouble(propre);

        } catch (Exception e) {

            return 0;
        }
    }

    private void copierFichierDansCache(
            Uri uri,
            java.io.File destination)
            throws Exception {

        InputStream input =
                getContentResolver()
                        .openInputStream(uri);

        if (input == null) {
            throw new Exception(
                    "Impossible de lire le fichier"
            );
        }

        java.io.FileOutputStream output =
                new java.io.FileOutputStream(
                        destination
                );

        byte[] buffer = new byte[8192];

        int length;

        while ((length = input.read(buffer)) != -1) {

            output.write(
                    buffer,
                    0,
                    length
            );
        }

        output.flush();

        output.close();
        input.close();
    }

    private int compterLignesExcel(
            java.io.File file)
            throws Exception {

        ZipFile zipFile =
                new ZipFile(file);

        try {

            ZipEntry sheetEntry =
                    zipFile.getEntry(
                            "xl/worksheets/sheet1.xml"
                    );

            if (sheetEntry == null) {
                throw new Exception(
                        "Feuille Excel introuvable"
                );
            }

            InputStream inputStream =
                    zipFile.getInputStream(
                            sheetEntry
                    );

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(
                                    inputStream,
                                    StandardCharsets.UTF_8
                            )
                    );

            int count = 0;

            String line;

            while ((line = reader.readLine())
                    != null) {

                int position = 0;

                while ((position =
                        line.indexOf(
                                "<row",
                                position
                        )) != -1) {

                    count++;
                    position += 4;
                }
            }

            reader.close();

            return Math.max(
                    count,
                    1
            );

        } finally {

            zipFile.close();
        }
    }

    private Map<String, String> lireSharedStrings(
            ZipFile zipFile)
            throws Exception {

        Map<String, String> result =
                new HashMap<>();

        ZipEntry entry =
                zipFile.getEntry(
                        "xl/sharedStrings.xml"
                );

        if (entry == null) {
            return result;
        }

        InputStream input =
                zipFile.getInputStream(entry);

        XmlPullParserFactory factory =
                XmlPullParserFactory.newInstance();

        factory.setNamespaceAware(true);

        XmlPullParser parser =
                factory.newPullParser();

        parser.setInput(
                input,
                StandardCharsets.UTF_8.name()
        );

        StringBuilder text =
                new StringBuilder();

        int index = 0;

        boolean insideSi = false;

        int eventType =
                parser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {

            if (eventType == XmlPullParser.START_TAG) {

                if ("si".equals(parser.getName())) {

                    text.setLength(0);
                    insideSi = true;

                } else if ("t".equals(parser.getName())
                        && insideSi) {

                    text.append(
                            parser.nextText()
                    );
                }
            }

            else if (eventType == XmlPullParser.END_TAG) {

                if ("si".equals(parser.getName())
                        && insideSi) {

                    result.put(
                            String.valueOf(index),
                            text.toString()
                    );

                    index++;

                    insideSi = false;
                }
            }

            eventType = parser.next();
        }

        input.close();

        return result;
    }

    private String convertirValeurCellule(
            String value,
            String type,
            Map<String, String> sharedStrings) {

        if (value == null) {
            return "";
        }

        if ("s".equals(type)) {

            String result =
                    sharedStrings.get(value);

            return result != null
                    ? result
                    : "";
        }

        return value;
    }

    private int colonneDepuisReference(
            String reference) {

        if (reference == null
                || reference.isEmpty()) {

            return 0;
        }

        int column = 0;

        for (int i = 0;
             i < reference.length();
             i++) {

            char c =
                    reference.charAt(i);

            if (c >= 'A' && c <= 'Z') {

                column =
                        column * 26
                                + (c - 'A' + 1);

            } else {
                break;
            }
        }

        return column - 1;
    }

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
                : "Fichier sélectionné";
    }

    @Override
    protected void onDestroy() {

        executor.shutdownNow();

        super.onDestroy();
    }
}
