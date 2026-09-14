package com.ocp.consultationstocks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bouton Synthèse des stocks
        View synthese = findViewById(R.id.btnSynthese);

        if (synthese != null) {
            synthese.setOnClickListener(v -> {
                Intent intent = new Intent(
                        MainActivity.this,
                        StockSearchActivity.class
                );
                startActivity(intent);
            });
        }

        // Bouton Importer des fichiers
        View importer = findViewById(R.id.btnImporter);

        if (importer != null) {
            importer.setOnClickListener(v -> {
                Intent intent = new Intent(
                        MainActivity.this,
                        ImportFileActivity.class
                );
                startActivity(intent);
            });
        }
    }
}
