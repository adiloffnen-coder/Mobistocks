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

    // =========================
    // COULEURS
    // =========================

    private static final int GREEN = Color.rgb(18, 151, 91);
    private static final int DARK_GREEN = Color.rgb(8, 116, 70);
    private static final int DARK_BLUE = Color.rgb(16, 55, 75);
    private static final int TEXT_GRAY = Color.rgb(91, 115, 133);
    private static final int BACKGROUND = Color.rgb(246, 250, 252);
    private static final int WHITE = Color.WHITE;

    private int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(DARK_GREEN);
        getWindow().setNavigationBarColor(WHITE);

        buildInterface();
    }

    // ============================================================
    // INTERFACE PRINCIPALE
    // ============================================================

    private void buildInterface() {

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(BACKGROUND);

        // --------------------------------------------------------
        // BARRE SUPERIEURE
        // --------------------------------------------------------

        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(dp(18), dp(10), dp(18), dp(10));
        header.setBackgroundColor(GREEN);

        // Menu
        TextView menu = new TextView(this);
        menu.setText("☰");
        menu.setTextColor(WHITE);
        menu.setTextSize(30);
        menu.setGravity(Gravity.CENTER);

        header.addView(
                menu,
                new LinearLayout.LayoutParams(dp(60), dp(75))
        );

        // Logo rond
        TextView logo = new TextView(this);
        logo.setText("🌿");
        logo.setTextColor(WHITE);
        logo.setTextSize(38);
        logo.setGravity(Gravity.CENTER);

        GradientDrawable logoBackground = new GradientDrawable();
        logoBackground.setShape(GradientDrawable.OVAL);
        logoBackground.setStroke(dp(2), WHITE);
        logo.setBackground(logoBackground);

        LinearLayout.LayoutParams logoParams =
                new LinearLayout.LayoutParams(dp(70), dp(70));

        logoParams.setMargins(dp(5), 0, dp(15), 0);

        header.addView(logo, logoParams);

        // Texte société
        LinearLayout headerText = new LinearLayout(this);
        headerText.setOrientation(LinearLayout.VERTICAL);
        headerText.setGravity(Gravity.CENTER_VERTICAL);

        TextView company = new TextView(this);
        company.setText("JORF FERTILIZERS COMPANY I");
        company.setTextColor(WHITE);
        company.setTextSize(15);
        company.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        TextView appName = new TextView(this);
        appName.setText("MobiStock");
        appName.setTextColor(WHITE);
        appName.setTextSize(28);
        appName.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        TextView appSubtitle = new TextView(this);
        appSubtitle.setText("Gestion et consultation des stocks");
        appSubtitle.setTextColor(WHITE);
        appSubtitle.setTextSize(13);

        headerText.addView(company);
        headerText.addView(appName);
        headerText.addView(appSubtitle);

        header.addView(
                headerText,
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                )
        );

        // Icône utilisateur
        TextView user = new TextView(this);
        user.setText("●");
        user.setTextColor(WHITE);
        user.setTextSize(27);
        user.setGravity(Gravity.CENTER);

        GradientDrawable userBackground = new GradientDrawable();
        userBackground.setShape(GradientDrawable.OVAL);
        userBackground.setStroke(dp(2), GREEN);
        userBackground.setColor(Color.TRANSPARENT);
        user.setBackground(userBackground);

        header.addView(
                user,
                new LinearLayout.LayoutParams(dp(60), dp(60))
        );

        root.addView(header);

        // --------------------------------------------------------
        // ZONE SCROLLABLE
        // --------------------------------------------------------

        ScrollView scrollView = new ScrollView(this);
        scrollView.setFillViewport(true);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(
                dp(20),
                dp(20),
                dp(20),
                dp(25)
        );

        // --------------------------------------------------------
        // BANNIERE BIENVENUE
        // --------------------------------------------------------

        LinearLayout welcomeBox = new LinearLayout(this);
        welcomeBox.setOrientation(LinearLayout.HORIZONTAL);
        welcomeBox.setGravity(Gravity.CENTER_VERTICAL);
        welcomeBox.setPadding(
                dp(25),
                dp(22),
                dp(10),
                dp(22)
        );

        GradientDrawable welcomeBackground = new GradientDrawable();
        welcomeBackground.setColor(Color.rgb(239, 247, 250));
        welcomeBackground.setCornerRadius(dp(22));

        welcomeBox.setBackground(welcomeBackground);

        LinearLayout welcomeText = new LinearLayout(this);
        welcomeText.setOrientation(LinearLayout.VERTICAL);

        TextView hello = new TextView(this);
        hello.setText("Bonjour,");
        hello.setTextColor(DARK_BLUE);
        hello.setTextSize(25);
        hello.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        TextView welcome = new TextView(this);
        welcome.setText("Bienvenue sur MobiStock");
        welcome.setTextColor(DARK_BLUE);
        welcome.setTextSize(25);
        welcome.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        TextView welcomeDescription = new TextView(this);
        welcomeDescription.setText(
                "Une vision claire de vos stocks,\n" +
                "pour une meilleure gestion."
        );
        welcomeDescription.setTextColor(TEXT_GRAY);
        welcomeDescription.setTextSize(15);
        welcomeDescription.setPadding(0, dp(10), 0, 0);

        welcomeText.addView(hello);
        welcomeText.addView(welcome);
        welcomeText.addView(welcomeDescription);

        welcomeBox.addView(
                welcomeText,
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                )
        );

        // Illustration usine
        TextView factory = new TextView(this);
        factory.setText("🏭");
        factory.setTextSize(65);
        factory.setGravity(Gravity.CENTER);

        welcomeBox.addView(
                factory,
                new LinearLayout.LayoutParams(
                        dp(145),
                        dp(160)
                )
        );

        content.addView(welcomeBox);

        // --------------------------------------------------------
        // TABLEAU DE BORD
        // --------------------------------------------------------

        LinearLayout dashboard = new LinearLayout(this);
        dashboard.setOrientation(LinearLayout.VERTICAL);
        dashboard.setPadding(
                dp(15),
                dp(18),
                dp(15),
                dp(15)
        );

        GradientDrawable dashboardBackground = new GradientDrawable();
        dashboardBackground.setColor(WHITE);
        dashboardBackground.setCornerRadius(dp(22));

        dashboard.setBackground(dashboardBackground);

        LinearLayout.LayoutParams dashboardParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        dashboardParams.setMargins(0, dp(15), 0, 0);

        content.addView(dashboard, dashboardParams);

        // Titre tableau de bord
        LinearLayout dashboardHeader = new LinearLayout(this);
        dashboardHeader.setOrientation(LinearLayout.HORIZONTAL);
        dashboardHeader.setGravity(Gravity.CENTER_VERTICAL);

        TextView dashboardIcon = new TextView(this);
        dashboardIcon.setText("▮▮▮");
        dashboardIcon.setTextColor(DARK_BLUE);
        dashboardIcon.setTextSize(22);

        dashboardHeader.addView(
                dashboardIcon,
                new LinearLayout.LayoutParams(
                        dp(55),
                        dp(45)
                )
        );

        TextView dashboardTitle = new TextView(this);
        dashboardTitle.setText("Tableau de bord");
        dashboardTitle.setTextColor(DARK_BLUE);
        dashboardTitle.setTextSize(23);
        dashboardTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        dashboardHeader.addView(dashboardTitle);

        dashboard.addView(dashboardHeader);

        // Date
        TextView date = new TextView(this);
        date.setText("▣   16 sept. 2025   •   10:24");
        date.setTextColor(TEXT_GRAY);
        date.setTextSize(13);
        date.setGravity(Gravity.RIGHT);

        dashboard.addView(date);

        // Première ligne
        LinearLayout row1 = new LinearLayout(this);
        row1.setOrientation(LinearLayout.HORIZONTAL);

        row1.addView(
                createDashboardCard(
                        "▦",
                        "Valeur stock Actif",
                        "12 458 320 MAD",
                        "Stock en mouvement",
                        Color.rgb(235, 249, 243),
                        GREEN
                ),
                cardWeight()
        );

        row1.addView(
                createDashboardCard(
                        "◷",
                        "Valeur stock Dormant > 5 ans",
                        "2 846 500 MAD",
                        "À surveiller",
                        Color.rgb(255, 246, 236),
                        Color.rgb(244, 133, 21)
                ),
                cardWeight()
        );

        dashboard.addView(row1);

        // Deuxième ligne
        LinearLayout row2 = new LinearLayout(this);
        row2.setOrientation(LinearLayout.HORIZONTAL);
        row2.setPadding(0, dp(10), 0, 0);

        row2.addView(
                createDashboardCard(
                        "□",
                        "Nombre d’articles",
                        "12 487",
                        "Articles référencés",
                        Color.rgb(237, 246, 255),
                        Color.rgb(25, 119, 230)
                ),
                cardWeight()
        );

        row2.addView(
                createDashboardCard(
                        "◎",
                        "Valeur stock global",
                        "15 304 820 MAD",
                        "Total des stocks",
                        Color.rgb(246, 241, 255),
                        Color.rgb(118, 42, 210)
                ),
                cardWeight()
        );

        dashboard.addView(row2);

        // --------------------------------------------------------
        // SYNTHESE DES STOCKS
        // --------------------------------------------------------

        content.addView(
                createSection(
                        "▤",
                        "Synthèse des stocks",
                        "Vue d’ensemble des stocks par magasin, valeur,\nquantité et articles.",
                        GREEN,
                        Color.rgb(239, 249, 245)
                )
        );

        // --------------------------------------------------------
        // MOUVEMENTS
        // --------------------------------------------------------

        content.addView(
                createSection(
                        "↻",
                        "Mouvements matières",
                        "Historique des entrées, sorties et transferts\n(si disponible).",
                        Color.rgb(30, 120, 225),
                        Color.rgb(239, 247, 255)
                )
        );

        // --------------------------------------------------------
        // IMPORTATION
        // --------------------------------------------------------

        content.addView(
                createSection(
                        "⇧",
                        "Importer des fichiers",
                        "Importation des données depuis un fichier Excel\nou CSV.",
                        Color.rgb(120, 45, 210),
                        Color.rgb(247, 242, 255)
                )
        );

        scrollView.addView(content);

        root.addView(
                scrollView,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0,
                        1
                )
        );

        // --------------------------------------------------------
        // NAVIGATION INFERIEURE
        // --------------------------------------------------------

        LinearLayout bottomNavigation = new LinearLayout(this);
        bottomNavigation.setOrientation(LinearLayout.HORIZONTAL);
        bottomNavigation.setGravity(Gravity.CENTER);
        bottomNavigation.setPadding(
                dp(5),
                dp(8),
                dp(5),
                dp(8)
        );
        bottomNavigation.setBackgroundColor(WHITE);

        bottomNavigation.addView(
                createNavigation("⌂", "Accueil", true)
        );

        bottomNavigation.addView(
                createNavigation("□", "Stocks", false)
        );

        bottomNavigation.addView(
                createNavigation("▮▮", "Analyse", false)
        );

        bottomNavigation.addView(
                createNavigation("☰", "Menu", false)
        );

        root.addView(
                bottomNavigation,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dp(78)
                )
        );

        setContentView(root);
    }

    // ============================================================
    // CARTE TABLEAU DE BORD
    // ============================================================

    private LinearLayout createDashboardCard(
            String icon,
            String title,
            String value,
            String description,
            int backgroundColor,
            int accentColor) {

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(
                dp(16),
                dp(14),
                dp(12),
                dp(12)
        );

        GradientDrawable background = new GradientDrawable();
        background.setColor(backgroundColor);
        background.setCornerRadius(dp(17));
        background.setStroke(
                dp(1),
                Color.argb(35, 0, 0, 0)
        );

        card.setBackground(background);

        // Icône
        TextView iconView = new TextView(this);
        iconView.setText(icon);
        iconView.setTextColor(WHITE);
        iconView.setTextSize(25);
        iconView.setGravity(Gravity.CENTER);

        GradientDrawable iconBackground = new GradientDrawable();
        iconBackground.setShape(GradientDrawable.OVAL);
        iconBackground.setColor(accentColor);

        iconView.setBackground(iconBackground);

        card.addView(
                iconView,
                new LinearLayout.LayoutParams(
                        dp(54),
                        dp(54)
                )
        );

        // Titre
        TextView titleView = new TextView(this);
        titleView.setText(title);
        titleView.setTextColor(DARK_BLUE);
        titleView.setTextSize(14);
        titleView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        titleView.setPadding(0, dp(10), 0, 0);

        card.addView(titleView);

        // Valeur
        TextView valueView = new TextView(this);
        valueView.setText(value);
        valueView.setTextColor(accentColor);
        valueView.setTextSize(20);
        valueView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        valueView.setPadding(0, dp(5), 0, 0);

        card.addView(valueView);

        // Description
        TextView descriptionView = new TextView(this);
        descriptionView.setText(description);
        descriptionView.setTextColor(TEXT_GRAY);
        descriptionView.setTextSize(12);
        descriptionView.setPadding(0, dp(5), 0, 0);

        card.addView(descriptionView);

        return card;
    }

    private LinearLayout.LayoutParams cardWeight() {

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        0,
                        dp(215),
                        1
                );

        params.setMargins(
                dp(4),
                dp(4),
                dp(4),
                dp(4)
        );

        return params;
    }

    // ============================================================
    // SECTIONS
    // ============================================================

    private LinearLayout createSection(
            String icon,
            String title,
            String description,
            int accentColor,
            int backgroundColor) {

        LinearLayout section = new LinearLayout(this);
        section.setOrientation(LinearLayout.HORIZONTAL);
        section.setGravity(Gravity.CENTER_VERTICAL);
        section.setPadding(
                dp(18),
                dp(14),
                dp(12),
                dp(14)
        );

        GradientDrawable background = new GradientDrawable();
        background.setColor(backgroundColor);
        background.setCornerRadius(dp(18));
        background.setStroke(
                dp(1),
                Color.argb(45, accentColor)
        );

        section.setBackground(background);

        LinearLayout.LayoutParams sectionParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dp(125)
                );

        sectionParams.setMargins(
                0,
                dp(12),
                0,
                0
        );

        section.setLayoutParams(sectionParams);

        // Icône
        TextView iconView = new TextView(this);
        iconView.setText(icon);
        iconView.setTextColor(WHITE);
        iconView.setTextSize(30);
        iconView.setGravity(Gravity.CENTER);

        GradientDrawable iconBackground = new GradientDrawable();
        iconBackground.setShape(GradientDrawable.OVAL);
        iconBackground.setColor(accentColor);

        iconView.setBackground(iconBackground);

        section.addView(
                iconView,
                new LinearLayout.LayoutParams(
                        dp(62),
                        dp(62)
                )
        );

        // Textes
        LinearLayout textLayout = new LinearLayout(this);
        textLayout.setOrientation(LinearLayout.VERTICAL);
        textLayout.setPadding(
                dp(15),
                0,
                dp(5),
                0
        );

        TextView titleView = new TextView(this);
        titleView.setText(title);
        titleView.setTextColor(DARK_BLUE);
        titleView.setTextSize(18);
        titleView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        TextView descriptionView = new TextView(this);
        descriptionView.setText(description);
        descriptionView.setTextColor(TEXT_GRAY);
        descriptionView.setTextSize(13);
        descriptionView.setPadding(
                0,
                dp(5),
                0,
                0
        );

        textLayout.addView(titleView);
        textLayout.addView(descriptionView);

        section.addView(
                textLayout,
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                )
        );

        // Flèche
        TextView arrow = new TextView(this);
        arrow.setText("›");
        arrow.setTextColor(accentColor);
        arrow.setTextSize(38);
        arrow.setGravity(Gravity.CENTER);

        section.addView(
                arrow,
                new LinearLayout.LayoutParams(
                        dp(35),
                        dp(60)
                )
        );

        return section;
    }

    // ============================================================
    // NAVIGATION BASSE
    // ============================================================

    private LinearLayout createNavigation(
            String icon,
            String title,
            boolean selected) {

        LinearLayout item = new LinearLayout(this);
        item.setOrientation(LinearLayout.VERTICAL);
        item.setGravity(Gravity.CENTER);

        TextView iconView = new TextView(this);
        iconView.setText(icon);
        iconView.setTextSize(26);
        iconView.setGravity(Gravity.CENTER);

        if (selected) {
            iconView.setTextColor(GREEN);
        } else {
            iconView.setTextColor(Color.rgb(95, 117, 135));
        }

        item.addView(
                iconView,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dp(38)
                )
        );

        TextView titleView = new TextView(this);
        titleView.setText(title);
        titleView.setTextSize(12);
        titleView.setGravity(Gravity.CENTER);

        if (selected) {
            titleView.setTextColor(GREEN);
            titleView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        } else {
            titleView.setTextColor(Color.rgb(95, 117, 135));
        }

        item.addView(
                titleView,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dp(25)
                )
        );

        View indicator = new View(this);

        if (selected) {
            GradientDrawable indicatorBackground =
                    new GradientDrawable();

            indicatorBackground.setColor(GREEN);
            indicatorBackground.setCornerRadius(dp(5));

            indicator.setBackground(indicatorBackground);
        }

        item.addView(
                indicator,
                new LinearLayout.LayoutParams(
                        selected ? dp(70) : dp(1),
                        dp(4)
                )
        );

        item.setLayoutParams(
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1
                )
        );

        return item;
    }
}
