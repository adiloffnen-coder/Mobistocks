package com.ocp.consultationstocks;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StockSearchActivity extends Activity {

    private EditText searchEditText;
    private Button searchButton;

    private final List<StockRow> stockRows = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_search);

        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);

        loadStocks();

        searchButton.setOnClickListener(v -> searchStock());

        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            searchStock();
            return true;
        });
    }

    // ============================================================
    // CHARGEMENT DU FICHIER Stocks.csv
    // ============================================================

    private void loadStocks() {

        try {

            InputStream inputStream = getAssets().open("Stocks.csv");

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(inputStream, "UTF-8")
                    );

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] c = line.split(";", -1);

                if (c.length < 18) {
                    continue;
                }

                // Ignore la ligne d'en-tête
                String first = c[0].trim();

                if (first.equalsIgnoreCase("SAP")
                        || first.equalsIgnoreCase("Code SAP")
                        || first.toLowerCase(Locale.ROOT).contains("code sap")) {
                    continue;
                }

                StockRow row = new StockRow();

                row.sap = clean(c[0]);
                row.ocp = clean(c[1]);
                row.designation = clean(c[2]);

                row.magasin = clean(c[5]);
                row.bin = clean(c[6]);

                row.quantite = clean(c[8]);
                row.unite = clean(c[9]);

                row.prixUnitaire = clean(c[13]);
                row.valeur = clean(c[14]);
                row.devise = clean(c[15]);

                row.dateEM = clean(c[16]);
                row.derniereSortie = clean(c[17]);

                /*
                 * OL :
                 * Si ton fichier possède une colonne OL, elle pourra
                 * être ajoutée ici lorsque nous connaîtrons son numéro.
                 */
                row.ol = "";

                stockRows.add(row);
            }

            reader.close();

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Erreur de lecture de Stocks.csv",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    // ============================================================
    // RECHERCHE SAP OU OCP
    // ============================================================

    private void searchStock() {

        String recherche =
                searchEditText.getText()
                        .toString()
                        .trim();

        if (recherche.isEmpty()) {

            Toast.makeText(
                    this,
                    "Saisissez un Code SAP ou Code OCP",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        List<StockRow> resultats = new ArrayList<>();

        for (StockRow row : stockRows) {

            if (row.sap.equalsIgnoreCase(recherche)
                    || row.ocp.equalsIgnoreCase(recherche)) {

                resultats.add(row);
            }
        }

        if (resultats.isEmpty()) {

            new AlertDialog.Builder(this)
                    .setMessage(
                            "Aucun stock disponible pour cet article."
                    )
                    .setPositiveButton("OK", null)
                    .show();

            return;
        }

        showResultDialog(resultats);
    }

    // ============================================================
    // FENÊTRE DE RESULTAT
    // ============================================================

    private void showResultDialog(List<StockRow> resultats) {

        View view = getLayoutInflater()
                .inflate(R.layout.dialog_stock_result, null);

        TextView designation =
                view.findViewById(R.id.dialogDesignation);

        TextView contenu =
                view.findViewById(R.id.dialogContent);

        TextView totalStock =
                view.findViewById(R.id.dialogTotalStock);

        TextView valeurGlobale =
                view.findViewById(R.id.dialogGlobalValue);

        Button fermer =
                view.findViewById(R.id.dialogClose);

        // Désignation
        designation.setText(
                resultats.get(0).designation
        );

        StringBuilder texte = new StringBuilder();

        double total = 0;
        double valeurTotale = 0;

        for (int i = 0; i < resultats.size(); i++) {

            StockRow row = resultats.get(i);

            if (i > 0) {

                texte.append(
                        "\n\n────────────────────\n\n"
                );
            }

            texte.append("▥  Code SAP : ")
                    .append(row.sap)
                    .append("\n");

            texte.append("🏷  Code OCP : ")
                    .append(row.ocp)
                    .append("\n\n");

            texte.append("🏭  MAGASIN : ")
                    .append(row.magasin);

            if (!row.ol.isEmpty()) {

                texte.append("     🏢  OL : ")
                        .append(row.ol);
            }

            texte.append("\n");

            texte.append("📍  BIN : ")
                    .append(row.bin)
                    .append("\n");

            texte.append("📦  Quantité : ")
                    .append(row.quantite)
                    .append(" ")
                    .append(row.unite)
                    .append("\n");

            texte.append("🪙  Prix unitaire : ")
                    .append(row.prixUnitaire)
                    .append(" ")
                    .append(row.devise)
                    .append("\n");

            texte.append("💰  Valeur : ")
                    .append(row.valeur)
                    .append(" ")
                    .append(row.devise)
                    .append("\n");

            texte.append("📅  Date EM : ")
                    .append(row.dateEM)
                    .append("\n");

            texte.append("↩️  Dernière sortie : ")
                    .append(row.derniereSortie);

            total += parseNumber(row.quantite);
            valeurTotale += parseNumber(row.valeur);
        }

        contenu.setText(texte.toString());

        totalStock.setText(
                "📦  TOTAL EN STOCK : "
                        + formatNumber(total)
        );

        valeurGlobale.setText(
                "💰  VALEUR GLOBALE : "
                        + formatNumber(valeurTotale)
                        + " MAD"
        );

        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setView(view)
                        .create();

        fermer.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

        if (dialog.getWindow() != null) {

            dialog.getWindow()
                    .setBackgroundDrawableResource(
                            android.R.color.transparent
                    );
        }
    }

    // ============================================================
    // OUTILS
    // ============================================================

    private String clean(String value) {

        if (value == null) {
            return "";
        }

        return value.trim()
                .replace("\"", "");
    }

    private double parseNumber(String value) {

        if (value == null || value.trim().isEmpty()) {
            return 0;
        }

        try {

            String v = value
                    .replace(" ", "")
                    .replace(",", ".");

            return Double.parseDouble(v);

        } catch (Exception e) {

            return 0;
        }
    }

    private String formatNumber(double value) {

        return String.format(
                Locale.US,
                "%,.2f",
                value
        );
    }

    // ============================================================
    // CLASSE STOCK
    // ============================================================

    private static class StockRow {

        String designation = "";
        String sap = "";
        String ocp = "";

        String magasin = "";
        String ol = "";
        String bin = "";

        String quantite = "";
        String unite = "";

        String prixUnitaire = "";
        String valeur = "";
        String devise = "";

        String dateEM = "";
        String derniereSortie = "";
    }
}
