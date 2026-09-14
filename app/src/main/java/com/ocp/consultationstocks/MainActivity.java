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

        View synthese = findViewById(R.id.btnSynthese);

        if (synthese != null) {
            synthese.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, StockSearchActivity.class);
                startActivity(intent);
            });
        }
    }
}
