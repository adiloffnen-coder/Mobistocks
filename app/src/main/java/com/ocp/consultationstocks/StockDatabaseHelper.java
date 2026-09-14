package com.ocp.consultationstocks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StockDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mobistock.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_STOCK = "stock";

    public StockDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE " + TABLE_STOCK + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "code_sap TEXT," +
                "code_ocp TEXT," +
                "description TEXT," +
                "numero_magasin TEXT," +
                "division TEXT," +
                "magasin TEXT," +
                "bin TEXT," +
                "type_magasin TEXT," +
                "quantite REAL," +
                "unite TEXT," +
                "type_stock TEXT," +
                "designation_type_stock TEXT," +
                "groupe_valorisation TEXT," +
                "prix REAL," +
                "valeur REAL," +
                "devise TEXT," +
                "date_em TEXT," +
                "derniere_sortie TEXT" +
                ")";

        db.execSQL(sql);

        db.execSQL(
                "CREATE INDEX idx_code_sap ON " +
                        TABLE_STOCK + "(code_sap)"
        );

        db.execSQL(
                "CREATE INDEX idx_code_ocp ON " +
                        TABLE_STOCK + "(code_ocp)"
        );
    }

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOCK);
        onCreate(db);
    }

    /**
     * Supprime toutes les anciennes données
     * avant un nouvel import Excel.
     */
    public void clearStock() {

        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLE_STOCK, null, null);
    }

    /**
     * Ajoute une ligne du fichier Excel.
     */
    public long insertStock(
            String codeSap,
            String codeOcp,
            String description,
            String numeroMagasin,
            String division,
            String magasin,
            String bin,
            String typeMagasin,
            double quantite,
            String unite,
            String typeStock,
            String designationTypeStock,
            String groupeValorisation,
            double prix,
            double valeur,
            String devise,
            String dateEm,
            String derniereSortie) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("code_sap", codeSap);
        values.put("code_ocp", codeOcp);
        values.put("description", description);
        values.put("numero_magasin", numeroMagasin);
        values.put("division", division);
        values.put("magasin", magasin);
        values.put("bin", bin);
        values.put("type_magasin", typeMagasin);
        values.put("quantite", quantite);
        values.put("unite", unite);
        values.put("type_stock", typeStock);
        values.put(
                "designation_type_stock",
                designationTypeStock
        );
        values.put(
                "groupe_valorisation",
                groupeValorisation
        );
        values.put("prix", prix);
        values.put("valeur", valeur);
        values.put("devise", devise);
        values.put("date_em", dateEm);
        values.put("derniere_sortie", derniereSortie);

        return db.insert(TABLE_STOCK, null, values);
    }

    /**
     * Recherche toutes les lignes correspondant
     * au Code SAP OU au Code OCP.
     */
    public Cursor searchStock(String code) {

        SQLiteDatabase db = getReadableDatabase();

        return db.query(
                TABLE_STOCK,
                null,
                "code_sap = ? OR code_ocp = ?",
                new String[]{code, code},
                null,
                null,
                "id ASC"
        );
    }
}
