package com.ocp.mobistock;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {

    private final String VERT = "#25D366";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        // ==============================
        // CONTENEUR PRINCIPAL
        // ==============================

        LinearLayout principal =
                new LinearLayout(this);

        principal.setOrientation(
                LinearLayout.VERTICAL
        );

        principal.setBackgroundColor(
                Color.parseColor("#F5F7F6")
        );


        // ==============================
        // SCROLL
        // ==============================

        ScrollView scroll =
                new ScrollView(this);

        LinearLayout contenu =
                new LinearLayout(this);

        contenu.setOrientation(
                LinearLayout.VERTICAL
        );

        contenu.setPadding(
                30,
                30,
                30,
                30
        );


        // ==============================
        // ENTETE
        // ==============================

        LinearLayout entete =
                new LinearLayout(this);

        entete.setOrientation(
                LinearLayout.VERTICAL
        );

        entete.setPadding(
                35,
                35,
                35,
                35
        );

        entete.setGravity(
                Gravity.CENTER
        );


        GradientDrawable fondEntete =
                new GradientDrawable();

        fondEntete.setColor(
                Color.parseColor(VERT)
        );

        fondEntete.setCornerRadius(
                35
        );

        entete.setBackground(
                fondEntete
        );


        TextView societe =
                new TextView(this);

        societe.setText(
                "JORF FERTILIZERS COMPANY I"
        );

        societe.setTextColor(
                Color.WHITE
        );

        societe.setTextSize(
                17
        );

        societe.setTypeface(
                null,
                Typeface.BOLD
        );

        societe.setGravity(
                Gravity.CENTER
        );

        entete.addView(
                societe
        );


        TextView titre =
                new TextView(this);

        titre.setText(
                "MobiStock"
        );

        titre.setTextColor(
                Color.WHITE
        );

        titre.setTextSize(
                24
        );

        titre.setTypeface(
                null,
                Typeface.BOLD
        );

        titre.setGravity(
                Gravity.CENTER
        );

        titre.setPadding(
                0,
                15,
                0,
                0
        );

        entete.addView(
                titre
        );


        contenu.addView(
                entete
        );


        // ==============================
        // TITRE TABLEAU DE BORD
        // ==============================

        TextView tableauTitre =
                new TextView(this);

        tableauTitre.setText(
                "Tableau de bord"
        );

        tableauTitre.setTextSize(
                23
        );

        tableauTitre.setTextColor(
                Color.parseColor("#202020")
        );

        tableauTitre.setTypeface(
                null,
                Typeface.BOLD
        );

        tableauTitre.setPadding(
                5,
                45,
                0,
                25
        );

        contenu.addView(
                tableauTitre
        );


        // ==============================
        // LIGNE 1 : STOCK ACTIF
        // ==============================

        LinearLayout ligne1 =
                new LinearLayout(this);

        ligne1.setOrientation(
                LinearLayout.HORIZONTAL
        );


        ligne1.addView(
                creerCarte(
                        "STOCK ACTIF",
                        "0 MAD",
                        "#E8F8EE",
                        "#1E8E4D"
                ),
                new LinearLayout.LayoutParams(
                        0,
                        190,
                        1
                )
        );


        View espace1 =
                new View(this);

        ligne1.addView(
                espace1,
                new LinearLayout.LayoutParams(
                        20,
                        1
                )
        );


        ligne1.addView(
                creerCarte(
                        "STOCK DORMANT\n> 5 ANS",
                        "0 MAD",
                        "#FFF3E0",
                        "#E67E22"
                ),
                new LinearLayout.LayoutParams(
                        0,
                        190,
                        1
                )
        );


        contenu.addView(
                ligne1
        );


        // ESPACE

        View espaceVertical1 =
                new View(this);

        contenu.addView(
                espaceVertical1,
                new LinearLayout.LayoutParams(
                        1,
                        20
                )
        );


        // ==============================
        // LIGNE 2
        // ==============================

        LinearLayout ligne2 =
                new LinearLayout(this);

        ligne2.setOrientation(
                LinearLayout.HORIZONTAL
        );


        ligne2.addView(
                creerCarte(
                        "NOMBRE\nD'ARTICLES",
                        "0",
                        "#EAF2FF",
                        "#2979FF"
                ),
                new LinearLayout.LayoutParams(
                        0,
                        190,
                        1
                )
        );


        View espace2 =
                new View(this);

        ligne2.addView(
                espace2,
                new LinearLayout.LayoutParams(
                        20,
                        1
                )
        );


        ligne2.addView(
                creerCarte(
                        "VALEUR STOCK\nGLOBALE",
                        "0 MAD",
                        "#F3E8FF",
                        "#8E44AD"
                ),
                new LinearLayout.LayoutParams(
                        0,
                        190,
                        1
                )
        );


        contenu.addView(
                ligne2
        );


        // ==============================
        // ESPACE
        // ==============================

        View espaceVertical2 =
                new View(this);

        contenu.addView(
                espaceVertical2,
                new LinearLayout.LayoutParams(
                        1,
                        45
                )
        );


        // ==============================
        // TITRE MODULES
        // ==============================

        TextView modulesTitre =
                new TextView(this);

        modulesTitre.setText(
                "Modules"
        );

        modulesTitre.setTextSize(
                22
        );

        modulesTitre.setTextColor(
                Color.parseColor("#202020")
        );

        modulesTitre.setTypeface(
                null,
                Typeface.BOLD
        );

        modulesTitre.setPadding(
                5,
                0,
                0,
                20
        );

        contenu.addView(
                modulesTitre
        );


        // ==============================
        // SYNTHESE STOCKS
        // ==============================

        contenu.addView(
                creerModule(
                        "📊",
                        "Synthèse des stocks",
                        "Analyse et consultation des stocks"
                )
        );


        ajouterEspace(
                contenu,
                18
        );


        // ==============================
        // MOUVEMENTS
        // ==============================

        contenu.addView(
                creerModule(
                        "🔄",
                        "Mouvements matières",
                        "Consultation des entrées et sorties"
                )
        );


        ajouterEspace(
                contenu,
                18
        );


        // ==============================
        // IMPORTATION
        // ==============================

        contenu.addView(
                creerModule(
                        "📥",
                        "Importation des fichiers",
                        "Importer vos fichiers Excel"
                )
        );


        ajouterEspace(
                contenu,
                40
        );


        // ==============================
        // AJOUT SCROLL
        // ==============================

        scroll.addView(
                contenu
        );

        principal.addView(
                scroll,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0,
                        1
                )
        );


        // ==============================
        // MENU BAS
        // ==============================

        LinearLayout menuBas =
                new LinearLayout(this);

        menuBas.setOrientation(
                LinearLayout.HORIZONTAL
        );

        menuBas.setGravity(
                Gravity.CENTER
        );

        menuBas.setPadding(
                10,
                15,
                10,
                15
        );

        menuBas.setBackgroundColor(
                Color.WHITE
        );


        menuBas.addView(
                creerMenu(
                        "⌂",
                        "Accueil",
                        true
                ),
                new LinearLayout.LayoutParams(
                        0,
                        
