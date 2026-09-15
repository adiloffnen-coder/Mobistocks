package com.ocp.consultationstocks;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class ImportFileActivity extends Activity {

    private static final int PICK_EXCEL = 1001;

    private TextView fileNameText;
    private TextView progressText;

    private Button selectFileButton;
    private Button importButton;
    private Button cancelButton;

    private CircularProgressView circularProgress;

    private Uri selectedFileUri;

    private final Handler handler =
            new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_import_file
        );

        fileNameText =
                findViewById(
                        R.id.fileNameText
                );

        progressText =
                findViewById(
                        R.id.progressText
                );

        selectFileButton =
                findViewById(
                        R.id.selectFileButton
                );

        importButton =
                findViewById(
                        R.id.importButton
                );

        cancelButton =
                findViewById(
                        R.id.cancelButton
                );

        circularProgress =
                findViewById(
                        R.id.circularProgress
                );

        circularProgress.setProgress(0);

        selectFileButton.setOnClickListener(
                v -> selectFile()
        );

        importButton.setOnClickListener(
                v -> importFile()
        );

        cancelButton.setOnClickListener(
                v -> finish()
        );
    }

    private void selectFile() {

        Intent intent =
                new Intent(
                        Intent.ACTION_OPEN_DOCUMENT
                );

        intent.addCategory(
                Intent.CATEGORY_OPENABLE
        );

        intent.setType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        );

        startActivityForResult(
                intent,
                PICK_EXCEL
        );
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

        if (requestCode != PICK_EXCEL
                || resultCode != RESULT_OK
                || data == null) {

            return;
        }

        selectedFileUri =
                data.getData();

        if (selectedFileUri == null) {
            return;
        }

        String fileName =
                getFileName(
                        selectedFileUri
                );

        fileNameText.setText(
                fileName
        );

        circularProgress.setProgress(0);

        progressText.setText(
                "Fichier sélectionné — prêt pour l'importation"
        );

        importButton.setEnabled(true);
    }

    private String getFileName(Uri uri) {

        String result = null;

        if ("content".equals(
                uri.getScheme())) {

            android.database.Cursor cursor =
                    null;

            try {

                cursor =
                        getContentResolver()
                                .query(
                                        uri,
                                        null,
                                        null,
                                        null,
                                        null
                                );

                if (cursor != null
                        && cursor.moveToFirst()) {

                    int nameIndex =
                            cursor.getColumnIndex(
                                    "_display_name"
                            );

                    if (nameIndex >= 0) {

                        result =
                                cursor.getString(
                                        nameIndex
                                );
                    }
                }

            } catch (Exception ignored) {

            } finally {

                if (cursor != null) {
                    cursor.close();
                }
            }
        }

        if (result == null) {

            result =
                    uri.getLastPathSegment();
        }

        if (result == null
                || result.trim().isEmpty()) {

            result =
                    "Stocks.xlsx";
        }

        return result;
    }

    private void importFile() {

        if (selectedFileUri == null) {

            Toast.makeText(
                    this,
                    "Sélectionnez d'abord un fichier Excel.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        selectFileButton.setEnabled(false);
        importButton.setEnabled(false);
        cancelButton.setEnabled(false);

        circularProgress.setProgress(0);

        progressText.setText(
                "Importation en cours..."
        );

        new Thread(() -> {

            boolean success = false;

            try {

                InputStream inputStream =
                        getContentResolver()
                                .openInputStream(
                                        selectedFileUri
                                );

                if (inputStream == null) {
                    throw new Exception(
                            "Impossible d'ouvrir le fichier."
                    );
                }

                long totalBytes =
                        getFileSize(
                                selectedFileUri
                        );

                File destination =
                        new File(
                                getFilesDir(),
                                "Stocks.xlsx"
                        );

                FileOutputStream outputStream =
                        new FileOutputStream(
                                destination
                        );

                byte[] buffer =
                        new byte[8192];

                long copiedBytes = 0;

                int bytesRead;

                while (
                        (bytesRead =
                                inputStream.read(buffer))
                                != -1
                ) {

                    outputStream.write(
                            buffer,
                            0,
                            bytesRead
                    );

                    copiedBytes += bytesRead;

                    int progress;

                    if (totalBytes > 0) {

                        progress =
                                (int) (
                                        copiedBytes
                                                * 100
                                                / totalBytes
                                );

                    } else {

                        progress = 0;
                    }

                    if (progress > 100) {
                        progress = 100;
                    }

                    int finalProgress =
                            progress;

                    handler.post(() -> {

                        circularProgress
                                .setProgress(
                                        finalProgress
                                );

                        progressText.setText(
                                "Importation en cours... "
                                        + finalProgress
                                        + " %"
                        );
                    });
                }

                outputStream.flush();
                outputStream.close();
                inputStream.close();

                success = true;

            } catch (Exception e) {

                e.printStackTrace();
            }

            boolean finalSuccess =
                    success;

            handler.post(() -> {

                if (finalSuccess) {

                    circularProgress
                            .setProgress(100);

                    progressText.setText(
                            "Importation terminée — 100 %"
                    );

                    Toast.makeText(
                            ImportFileActivity.this,
                            "Fichier Excel importé avec succès.",
                            Toast.LENGTH_SHORT
                    ).show();

                    handler.postDelayed(
                            () -> finish(),
                            800
                    );

                } else {

                    progressText.setText(
                            "Erreur lors de l'importation"
                    );

                    selectFileButton
                            .setEnabled(true);

                    importButton
                            .setEnabled(true);

                    cancelButton
                            .setEnabled(true);

                    Toast.makeText(
                            ImportFileActivity.this,
                            "Échec de l'importation du fichier.",
                            Toast.LENGTH_LONG
                    ).show();
                }
            });

        }).start();
    }

    private long getFileSize(Uri uri) {

        android.database.Cursor cursor =
                null;

        try {

            cursor =
                    getContentResolver()
                            .query(
                                    uri,
                                    new String[]{
                                            android.provider.OpenableColumns.SIZE
                                    },
                                    null,
                                    null,
                                    null
                            );

            if (cursor != null
                    && cursor.moveToFirst()) {

                int sizeIndex =
                        cursor.getColumnIndex(
                                android.provider.OpenableColumns.SIZE
                        );

                if (sizeIndex >= 0) {

                    return cursor.getLong(
                            sizeIndex
                    );
                }
            }

        } catch (Exception ignored) {

        } finally {

            if (cursor != null) {
                cursor.close();
            }
        }

        return -1;
    }
}
