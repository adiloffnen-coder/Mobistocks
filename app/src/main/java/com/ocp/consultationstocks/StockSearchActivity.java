package com.ocp.consultationstocks;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StockSearchActivity extends Activity {

    private EditText searchEditText;
    private Button searchButton;

    private List<ExcelDatabase.StockRow> stockRows =
            new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_stock_search
        );

        searchEditText =
                findViewById(R.id.searchEditText);

        searchButton =
                findViewById(R.id.searchButton);

        // Charger le fichier Excel importé
        loadExcel();

        // Bouton rechercher
        searchButton.setOnClickListener(v ->
                searchStock()
        );
    }

    // =========================================================
    // CHARGER EXCEL
    // =========================================================

    private void loadExcel() {

        ExcelDatabase database =
                new ExcelDatabase(this);

        stockRows =
                database.readExcel();

        if (stockRows.isEmpty()) {

            Toast.makeText(
                    this,
                    "Aucun fichier Excel importé.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    // =========================================================
    // RECHERCHE SAP / OCP
    // =========================================================

    private void searchStock() {

        String recherche =
                searchEditText
                        .getText()
                        .toString()
                        .trim();

        if (recherche.isEmpty()) {

            Toast.makeText(
                    this,
                    "Saisissez un Code SAP ou Code OCP.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        List<ExcelDatabase.StockRow> resultats =
                new ArrayList<>();

        for (ExcelDatabase.StockRow row : stockRows) {

            if (row.sap.equalsIgnoreCase(recherche)
                    || row.ocp.equalsIgnoreCase(recherche)) {

                resultats.add(row);
            }
        }

        // Aucun résultat
        if (resultats.isEmpty()) {

            new AlertDialog.Builder(this)
                    .setMessage(
                            "Aucun stock disponible pour cet article."
                    )
                    .setPositiveButton(
                            "OK",
                            null
                    )
                    .show();

            return;
        }

        // Afficher les résultats
        showResultDialog(resultats);
    }

    // =========================================================
    // FENÊTRE RESULTAT
    // =========================================================

    private void showResultDialog(
            List<ExcelDatabase.StockRow> resultats) {

        View view =
                getLayoutInflater().inflate(
                        R.layout.dialog_stock_result,
                        null
                );

        TextView designation =
                view.findViewById(
                        R.id.dialogDesignation
                );

        TextView contenu =
                view.findViewById(
                        R.id.dialogContent
                );

        TextView totalStock =
                view.findViewById(
                        R.id.dialogTotalStock
                );

        TextView valeurGlobale =
                view.findViewById(
                        R.id.dialogGlobalValue
                );

        Button fermer =
                view.findViewById(
                        R.id.dialogClose
                );

        // Désignation
        designation.setText(
                resultats.get(0).designation
        );

        StringBuilder texte =
                new StringBuilder();

        double totalQuantite = 0;
        double totalValeur = 0;

        // =====================================================
        // AFFICHER TOUTES LES LIGNES
        // =====================================================

        for (int i = 0;
             i < resultats.size();
             i++) {

            ExcelDatabase.StockRow row =
                    resultats.get(i);

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
                    .append(row.magasin)
                    .append("\n");

            if (!row.ol.isEmpty()) {

                texte.append("🏢  OL : ")
                        .append(row.ol)
                        .append("\n");
            }

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

            totalQuantite +=
                    parseNumber(row.quantite);

            totalValeur +=
                    parseNumber(row.valeur);
        }

        contenu.setText(
                texte.toString()
        );

        // =====================================================
        // TOTAUX
        // =====================================================

        totalStock.setText(
                "📦  TOTAL EN STOCK : "
                        + formatNumber(totalQuantite)
        );

        valeurGlobale.setText(
                "💰  VALEUR GLOBALE : "
                        + formatNumber(totalValeur)
                        + " MAD"
        );

        // =====================================================
        // DIALOGUE
        // =====================================================

        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setView(view)
                        .create();

        fermer.setOnClickListener(v ->
                dialog.dismiss()
        );

        dialog.show();

        if (dialog.getWindow() != null) {

            dialog.getWindow()
                    .setBackgroundDrawableResource(
                            android.R.color.transparent
                    );
        }
    }

    // =========================================================
    // CONVERSION NOMBRE
    // =========================================================

    private double parseNumber(String value) {

        if (value == null
                || value.trim().isEmpty()) {

            return 0;
        }

        try {

            String v =
                    value
                            .replace(" ", "")
                            .replace(",", ".");

            return Double.parseDouble(v);

        } catch (Exception e) {

            return 0;
        }
    }

    // =========================================================
    // FORMATAGE
    // =========================================================

    private String formatNumber(double value) {

        return String.format(
                Locale.US,
                "%,.2f",
                value
        );
    }
}
