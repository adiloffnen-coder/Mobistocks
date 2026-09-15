package com.ocp.consultationstocks;

import android.content.Context;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelDatabase {

    private final Context context;

    public ExcelDatabase(Context context) {
        this.context = context;
    }

    public List<StockRow> readExcel() {

        List<StockRow> rows = new ArrayList<>();

        try {

            File file = new File(
                    context.getFilesDir(),
                    "Stocks.xlsx"
            );

            if (!file.exists()) {
                return rows;
            }

            FileInputStream inputStream =
                    new FileInputStream(file);

            Workbook workbook =
                    new XSSFWorkbook(inputStream);

            Sheet sheet =
                    workbook.getSheetAt(0);

            DataFormatter formatter =
                    new DataFormatter();

            boolean firstRow = true;

            for (Row excelRow : sheet) {

                if (firstRow) {
                    firstRow = false;
                    continue;
                }

                String[] c = new String[18];

                for (int i = 0; i < 18; i++) {

                    Cell cell = excelRow.getCell(i);

                    if (cell != null) {
                        c[i] = formatter
                                .formatCellValue(cell)
                                .trim();
                    } else {
                        c[i] = "";
                    }
                }

                if (c[0].isEmpty()
                        && c[1].isEmpty()
                        && c[2].isEmpty()) {
                    continue;
                }

                StockRow stock = new StockRow();

                stock.sap = c[0];
                stock.ocp = c[1];
                stock.designation = c[2];

                stock.magasin = c[5];
                stock.bin = c[6];

                stock.quantite = c[8];
                stock.unite = c[9];

                stock.prixUnitaire = c[13];
                stock.valeur = c[14];
                stock.devise = c[15];

                stock.dateEM = c[16];
                stock.derniereSortie = c[17];

                rows.add(stock);
            }

            workbook.close();
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rows;
    }

    public static class StockRow {

        public String sap = "";
        public String ocp = "";
        public String designation = "";

        public String magasin = "";
        public String ol = "";
        public String bin = "";

        public String quantite = "";
        public String unite = "";

        public String prixUnitaire = "";
        public String valeur = "";
        public String devise = "";

        public String dateEM = "";
        public String derniereSortie = "";
    }
}
