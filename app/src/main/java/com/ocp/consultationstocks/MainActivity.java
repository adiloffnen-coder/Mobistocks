package com.ocp.consultationstocks;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {

    private final int GREEN = Color.rgb(37, 211, 102);
    private final int DARK_GREEN = Color.rgb(20, 120, 65);
    private final int DARK = Color.rgb(30, 30, 30);
    private final int LIGHT = Color.rgb(245, 247, 246);
    private final int WHITE = Color.WHITE;
    private final int GRAY = Color.rgb(100, 100, 100);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(LIGHT);

        // =========================
        // BARRE SUPERIEURE
        // =========================

        LinearLayout toolbar = new LinearLayout(this);
        toolbar.setOrientation(LinearLayout.HORIZONTAL);
        toolbar.setGravity(Gravity.CENTER_VERTICAL);
        toolbar.setPadding(20, 12, 20, 12);
        toolbar.setBackgroundColor(GREEN);

        TextView menu = new TextView(this);
        menu.setText("☰");
        menu.setTextSize(28);
        menu.setTextColor(WHITE);
        menu.setGravity(Gravity.CENTER);

        toolbar.addView(menu, new LinearLayout.LayoutParams(
                55, 60
        ));

        TextView logo = new TextView(this);
        logo.setText("🌿");
        logo.setTextSize(27);
        logo.setGravity(Gravity.CENTER);

        toolbar.addView(logo, new LinearLayout.LayoutParams(
                55, 60
        ));

        LinearLayout companyLayout = new LinearLayout(this);
        companyLayout.setOrientation(LinearLayout.VERTICAL);
        companyLayout.setGravity(Gravity.CENTER_VERTICAL);

        TextView company = new TextView(this);
        company.setText("JORF FERTILIZERS COMPANY I");
        company.setTextColor(WHITE);
        company.setTextSize(14);
        company.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        TextView appName = new TextView(this);
        appName.setText("MobiStock");
        appName.setTextColor(WHITE);
        appName.setTextSize(20);
        appName.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        companyLayout.addView(company);
        companyLayout.addView(appName);

        toolbar.addView(companyLayout,
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                ));

        root.addView(toolbar);

        // =========================
        // CONTENU
        // =========================

        ScrollView scrollView = new ScrollView(this);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(20, 22, 20, 25);

        TextView welcome = new TextView(this);
        welcome.setText("Bonjour,\nBienvenue sur MobiStock");
        welcome.setTextColor(DARK);
        welcome.setTextSize(25);
        welcome.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        content.addView(welcome);

        TextView subtitle = new TextView(this);
        subtitle.setText("Gestion et consultation des stocks");
        subtitle.setTextColor(GRAY);
        subtitle.setTextSize(15);
        subtitle.setPadding(0, 8, 0, 22);

        content.addView(subtitle);

        // =========================
        // BLOC PRESENTATION
        // =========================

        LinearLayout presentation = new LinearLayout(this);
        presentation.setOrientation(LinearLayout.VERTICAL);
        presentation.setGravity(Gravity.CENTER);
        presentation.setPadding(20, 25, 20, 25);

        GradientDrawable presentationBg = new GradientDrawable();
        presentationBg.setColor(WHITE);
        presentationBg.setCornerRadius(22);
        presentation.setBackground(presentationBg);

        TextView factoryIcon = new TextView(this);
        factoryIcon.setText("🏭");
        factoryIcon.setTextSize(55);
        factoryIcon.setGravity(Gravity.CENTER);

        presentation.addView(factoryIcon);

        TextView presentationText = new TextView(this);
        presentationText.setText("Suivez et consultez vos stocks\n"
                + "simplement depuis votre mobile");
        presentationText.setTextColor(DARK);
        presentationText.setTextSize(16);
        presentationText.setGravity(Gravity.CENTER);
        presentationText.setPadding(10, 10, 10, 5);

        presentation.addView(presentationText);

        content.addView(presentation);

        // =========================
        // TITRE DASHBOARD
        // =========================

        TextView dashboardTitle = new TextView(this);
        dashboardTitle.setText("Tableau de bord");
        dashboardTitle.setTextColor(DARK);
        dashboardTitle.setTextSize(21);
        dashboardTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        dashboardTitle.setPadding(0, 25, 0, 12);

        content.addView(dashboardTitle);

        // Ligne 1
        LinearLayout row1 = new LinearLayout(this);
        row1.setOrientation(LinearLayout.HORIZONTAL);

        row1.addView(createCard(
                "Valeur stock Actif",
                "12 458 320 MAD"
        ), weightParams());

        row1.addView(createCard(
                "Stock dormant > 5 ans",
                "2 846 500 MAD"
        ), weightParams());

        content.addView(row1);

        // Ligne 2
        LinearLayout row2 = new LinearLayout(this);
        row2.setOrientation(LinearLayout.HORIZONTAL);
        row2.setPadding(0, 12, 0, 0);

        row2.addView(createCard(
                "Nombre d'articles",
                "12 487"
        ), weightParams());

        row2.addView(createCard(
                "Valeur stock global",
                "15 304 820 MAD"
        ), weightParams());

        content.addView(row2);

        // =========================
        // SYNTHESE
        // =========================

        TextView synthesisTitle = new TextView(this);
        synthesisTitle.setText("Synthèse des stocks");
        synthesisTitle.setTextColor(DARK);
        synthesisTitle.setTextSize(21);
        synthesisTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        synthesisTitle.setPadding(0, 28, 0, 12);

        content.addView(synthesisTitle);

        content.addView(createAction(
                "📦",
                "Consulter les stocks",
                "Rechercher par code SAP ou code OCP"
        ));

        content.addView(createAction(
                "📊",
                "Analyse des stocks",
                "Visualiser les indicateurs et les valeurs"
        ));

        content.addView(createAction(
                "🔄",
                "Mouvements matières",
                "Consulter les mouvements de stock"
        ));

        // =========================
        // IMPORTATION
        // =========================

        TextView importTitle = new TextView(this);
        importTitle.setText("Importer des fichiers");
        importTitle.setTextColor(DARK);
        importTitle.setTextSize(21);
        importTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        importTitle.setPadding(0, 28, 0, 12);

        content.addView(importTitle);

        content.addView(createAction(
                "📁",
                "Importer un fichier Excel",
                "Charger un fichier .xlsx"
        ));

        content.addView(createAction(
                "📄",
                "Importer un fichier CSV",
                "Charger un fichier .csv"
        ));

        // =========================
        // NAVIGATION
        // =========================

        TextView bottomSpace = new TextView(this);
        bottomSpace.setText("");
        bottomSpace.setHeight(25);
        content.addView(bottomSpace);

        scrollView.addView(content);

        root.addView(scrollView,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0,
                        1
                ));

        LinearLayout bottomBar = new LinearLayout(this);
        bottomBar.setOrientation(LinearLayout.HORIZONTAL);
        bottomBar.setGravity(Gravity.CENTER);
        bottomBar.setPadding(5, 8, 5, 8);
        bottomBar.setBackgroundColor(WHITE);

        bottomBar.addView(createNavigation("⌂", "Accueil"));
        bottomBar.addView(createNavigation("▣", "Stocks"));
        bottomBar.addView(createNavigation("▥", "Analyse"));
        bottomBar.addView(createNavigation("☰", "Menu"));

        root.addView(bottomBar);

        setContentView(root);
    }

    // =========================
    // CARTE DASHBOARD
    // =========================

    private TextView createCard(String title, String value) {

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(15, 18, 15, 18);

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(WHITE);
        bg.setCornerRadius(20);
        card.setBackground(bg);

        TextView titleView = new TextView(this);
        titleView.setText(title);
        titleView.setTextColor(GRAY);
        titleView.setTextSize(13);

        TextView valueView = new TextView(this);
        valueView.setText(value);
        valueView.setTextColor(DARK_GREEN);
        valueView.setTextSize(17);
        valueView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        valueView.setPadding(0, 8, 0, 0);

        card.addView(titleView);
        card.addView(valueView);

        TextView result = new TextView(this);
        result.setText("");
        result.setBackground(card.getBackground());

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        return result;
    }

    private LinearLayout.LayoutParams weightParams() {
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        0,
                        120,
                        1
                );
        params.setMargins(5, 0, 5, 0);
        return params;
    }

    // =========================
    // ACTION
    // =========================

    private LinearLayout createAction(
            String icon,
            String title,
            String description) {

        LinearLayout action = new LinearLayout(this);
        action.setOrientation(LinearLayout.HORIZONTAL);
        action.setGravity(Gravity.CENTER_VERTICAL);
        action.setPadding(15, 15, 15, 15);

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(WHITE);
        bg.setCornerRadius(18);
        action.setBackground(bg);

        LinearLayout.LayoutParams actionParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        actionParams.setMargins(0, 5, 0, 5);
        action.setLayoutParams(actionParams);

        TextView iconView = new TextView(this);
        iconView.setText(icon);
        iconView.setTextSize(27);
        iconView.setGravity(Gravity.CENTER);

        action.addView(iconView,
                new LinearLayout.LayoutParams(55, 60));

        LinearLayout texts = new LinearLayout(this);
        texts.setOrientation(LinearLayout.VERTICAL);

        TextView titleView = new TextView(this);
        titleView.setText(title);
        titleView.setTextColor(DARK);
        titleView.setTextSize(17);
        titleView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        TextView descView = new TextView(this);
        descView.setText(description);
        descView.setTextColor(GRAY);
        descView.setTextSize(13);
        descView.setPadding(0, 4, 0, 0);

        texts.addView(titleView);
        texts.addView(descView);

        action.addView(texts);

        return action;
    }

    // =========================
    // NAVIGATION BAS
    // =========================

    private LinearLayout createNavigation(
            String icon,
            String title) {

        LinearLayout item = new LinearLayout(this);
        item.setOrientation(LinearLayout.VERTICAL);
        item.setGravity(Gravity.CENTER);

        TextView iconView = new TextView(this);
        iconView.setText(icon);
        iconView.setTextSize(22);
        iconView.setTextColor(GREEN);
        iconView.setGravity(Gravity.CENTER);

        TextView titleView = new TextView(this);
        titleView.setText(title);
        titleView.setTextSize(11);
        titleView.setTextColor(GRAY);
        titleView.setGravity(Gravity.CENTER);

        item.addView(iconView);
        item.addView(titleView);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        0,
                        60,
                        1
                );

        item.setLayoutParams(params);

        return item;
    }
}
