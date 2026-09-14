package com.ocp.consultationstocks;

import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 60, 40, 40);

        TextView titre = new TextView(this);
        titre.setText("MobiStock");
        titre.setTextSize(28);
        titre.setTextColor(Color.rgb(37, 211, 102));
        titre.setGravity(Gravity.CENTER);

        EditText recherche = new EditText(this);
        recherche.setHint("Saisir Code SAP ou Code OCP");

        Button bouton = new Button(this);
        bouton.setText("RECHERCHER");

        TextView resultat = new TextView(this);
        resultat.setText("");
        resultat.setTextSize(18);
        resultat.setPadding(0, 40, 0, 0);

        bouton.setOnClickListener(v -> {
            String code = recherche.getText().toString().trim();

            if (code.equals("80111111")) {
                resultat.setText(
                    "Code SAP : 80111111\n\n" +
                    "Code OCP : 20325.11677\n\n" +
                    "Désignation : ROULEMENT ROOLWAY REF 6122\n\n" +
                    "Quantité : 04 Piece\n\n" +
                    "Prix unitaire : 5043 MAD\n\n" +
                    "Magasin : MC01\n\n" +
                    "Emplacement : PDSR-R001-F-04-00"
                );
            } else {
                resultat.setText("Aucun stock disponible pour cet article.");
            }
        });

        layout.addView(titre);
        layout.addView(recherche);
        layout.addView(bouton);
        layout.addView(resultat);

        setContentView(layout);
    }
}
