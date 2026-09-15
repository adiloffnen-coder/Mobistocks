package com.ocp.consultationstocks;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
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

        loadExcel();

        searchButton.setOnClickListener(v ->
                searchStock()
        );
    }

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

        showResultDialog(resultats);
    }

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

        designation.setText(
                resultats.get(0).designation
        );

        SpannableStringBuilder texte =
                new SpannableStringBuilder();

        double totalQuantite = 0;
        double totalValeur = 0;

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

            appendLine(
                    texte,
                    "▥  Code SAP : ",
                    row.sap
            );

            appendLine(
                    texte,
                    "🏷  Code OCP : ",
                    row.ocp
            );

            if (!row.division.isEmpty()) {

                appendLine(
                        texte,
                        "🏢  DIVISION : ",
                        row.division
                );
            }

            // MAGASIN EN GRAS
            appendBoldLine(
                    texte,
                    "🏭  MAGASIN : ",
                    row.magasin
            );

            if (!row.ol.isEmpty()) {

                appendLine(
                        texte,
                        "🏢  OL : ",
                        row.ol
                );
            }

            // BIN EN GRAS
            appendBoldLine(
                    texte,
                    "📍  BIN : ",
                    row.bin
            );

            appendLine(
                    texte,
                    "📦  Quantité : ",
                    row.quantite
                            + " "
                            + row.unite
            );

            appendLine(
                    texte,
                    "🪙  Prix unitaire : ",
                    row.prixUnitaire
                            + " "
                            + row.devise
            );

            appendLine(
                    texte,
                    "💰  Valeur : ",
                    row.valeur
                            + " "
                            + row.devise
            );

            appendLine(
                    texte,
                    "📅  Date EM : ",
                    row.dateEM
            );

            appendLine(
                    texte,
                    "↩️  Dernière sortie : ",
                    row.derniereSortie
            );

            totalQuantite +=
                    row.quantiteValue;

            totalValeur +=
                    row.valeurValue;
        }

        contenu.setText(texte);

        totalStock.setText(
                "📦  TOTAL EN STOCK : "
                        + formatNumber(totalQuantite)
        );

        String devise = "MAD";

        if (!resultats.get(0).devise.isEmpty()) {
            devise = resultats.get(0).devise;
        }

        valeurGlobale.setText(
                "💰  VALEUR GLOBALE : "
                        + formatNumber(totalValeur)
                        + " "
                        + devise
        );

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

    private void appendLine(
            SpannableStringBuilder builder,
            String label,
            String value) {

        builder.append(label);
        builder.append(value);
        builder.append("\n");
    }

    private void appendBoldLine(
            SpannableStringBuilder builder,
            String label,
            String value) {

        int start =
                builder.length();

        builder.append(label);
        builder.append(value);
        builder.append("\n");

        int end =
                builder.length() - 1;

        builder.setSpan(
                new StyleSpan(Typeface.BOLD),
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
    }

    private String formatNumber(double value) {

        return String.format(
                Locale.FRANCE,
                "%,.2f",
                value
        );
    }
}
