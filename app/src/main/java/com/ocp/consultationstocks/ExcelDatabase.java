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
import java.util.Locale;

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
                    new DataFormatter(Locale.FRANCE);

            // Important pour les cellules contenant des formules
            formatter.setUseCachedValuesForFormulaCells(true);

            // Recherche des colonnes par leur en-tête
            Row headerRow = sheet.getRow(0);

            int colSAP = findColumn(headerRow, formatter,
                    "SAP", "CODE SAP", "CODE_SAP");

            int colOCP = findColumn(headerRow, formatter,
                    "OCP", "CODE OCP", "CODE_OCP");

            int colDesignation = findColumn(headerRow, formatter,
                    "DESIGNATION", "DÉSIGNATION", "DESIGNATION ARTICLE");

            int colDivision = findColumn(headerRow, formatter,
                    "DIVISION", "DIV");

            int colMagasin = findColumn(headerRow, formatter,
                    "MAGASIN", "MAG", "STORE");

            int colOL = findColumn(headerRow, formatter,
                    "OL", "ORDRE LIVRAISON");

            int colBin = findColumn(headerRow, formatter,
                    "BIN", "EMPLACEMENT", "CASE");

            int colQuantite = findColumn(headerRow, formatter,
                    "QUANTITE", "QUANTITÉ", "QTE", "QTY", "QUANTITY");

            int colUnite = findColumn(headerRow, formatter,
                    "UNITE", "UNITÉ", "UNIT", "UQ");

            int colPrixUnitaire = findColumn(headerRow, formatter,
                    "PRIX UNITAIRE", "PRIX", "UNIT PRICE");

            int colValeur = findColumn(headerRow, formatter,
                    "VALEUR", "VALEUR STOCK", "VALUE", "MONTANT");

            int colDevise = findColumn(headerRow, formatter,
                    "DEVISE", "CURRENCY");

            int colDateEM = findColumn(headerRow, formatter,
                    "DATE EM", "DATEEM", "DATE ENTREE", "DATE ENTRÉE");

            int colDerniereSortie = findColumn(headerRow, formatter,
                    "DERNIERE SORTIE", "DERNIÈRE SORTIE",
                    "LAST SORTIE", "LAST ISSUE");

            boolean firstRow = true;

            for (Row excelRow : sheet) {

                if (firstRow) {
                    firstRow = false;
                    continue;
                }

                if (excelRow == null) {
                    continue;
                }

                String sap = getCellText(
                        excelRow,
                        colSAP,
                        formatter
                );

                String ocp = getCellText(
                        excelRow,
                        colOCP,
                        formatter
                );

                String designation = getCellText(
                        excelRow,
                        colDesignation,
                        formatter
                );

                if (sap.isEmpty()
                        && ocp.isEmpty()
                        && designation.isEmpty()) {
                    continue;
                }

                StockRow stock = new StockRow();

                stock.sap = sap;
                stock.ocp = ocp;
                stock.designation = designation;

                stock.division = getCellText(
                        excelRow,
                        colDivision,
                        formatter
                );

                stock.magasin = getCellText(
                        excelRow,
                        colMagasin,
                        formatter
                );

                stock.ol = getCellText(
                        excelRow,
                        colOL,
                        formatter
                );

                stock.bin = getCellText(
                        excelRow,
                        colBin,
                        formatter
                );

                stock.quantite = getCellText(
                        excelRow,
                        colQuantite,
                        formatter
                );

                stock.unite = getCellText(
                        excelRow,
                        colUnite,
                        formatter
                );

                stock.prixUnitaire = getCellText(
                        excelRow,
                        colPrixUnitaire,
                        formatter
                );

                stock.valeur = getCellText(
                        excelRow,
                        colValeur,
                        formatter
                );

                stock.devise = getCellText(
                        excelRow,
                        colDevise,
                        formatter
                );

                stock.dateEM = getCellText(
                        excelRow,
                        colDateEM,
                        formatter
                );

                stock.derniereSortie = getCellText(
                        excelRow,
                        colDerniereSortie,
                        formatter
                );

                // Valeurs numériques utilisées pour les totaux
                stock.quantiteValue =
                        getNumericValue(
                                excelRow,
                                colQuantite
                        );

                stock.valeurValue =
                        getNumericValue(
                                excelRow,
                                colValeur
                        );

                rows.add(stock);
            }

            workbook.close();
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rows;
    }

    private String getCellText(
            Row row,
            int column,
            DataFormatter formatter) {

        if (column < 0) {
            return "";
        }

        Cell cell = row.getCell(column);

        if (cell == null) {
            return "";
        }

        try {
            return formatter
                    .formatCellValue(cell)
                    .trim();

        } catch (Exception e) {
            return "";
        }
    }

    private double getNumericValue(
            Row row,
            int column) {

        if (column < 0) {
            return 0;
        }

        Cell cell = row.getCell(column);

        if (cell == null) {
            return 0;
        }

        try {

            switch (cell.getCellType()) {

                case NUMERIC:
                    return cell.getNumericCellValue();

                case FORMULA:

                    try {
                        return cell.getNumericCellValue();
                    } catch (Exception ignored) {
                    }

                    break;

                case STRING:

                    return parseNumber(
                            cell.getStringCellValue()
                    );

                default:
                    return 0;
            }

        } catch (Exception e) {
            return 0;
        }

        return 0;
    }

    private double parseNumber(String value) {

        if (value == null
                || value.trim().isEmpty()) {
            return 0;
        }

        try {

            String v = value
                    .replace("\u00A0", "")
                    .replace(" ", "")
                    .replace("MAD", "")
                    .replace("€", "")
                    .trim();

            if (v.contains(",")
                    && v.contains(".")) {

                if (v.lastIndexOf(",")
                        > v.lastIndexOf(".")) {

                    v = v.replace(".", "");
                    v = v.replace(",", ".");

                } else {

                    v = v.replace(",", "");
                }

            } else if (v.contains(",")) {

                v = v.replace(",", ".");

            }

            return Double.parseDouble(v);

        } catch (Exception e) {
            return 0;
        }
    }

    private int findColumn(
            Row headerRow,
            DataFormatter formatter,
            String... names) {

        if (headerRow == null) {
            return -1;
        }

        for (int i = 0;
             i < headerRow.getLastCellNum();
             i++) {

            Cell cell = headerRow.getCell(i);

            if (cell == null) {
                continue;
            }

            String header =
                    formatter
                            .formatCellValue(cell)
                            .trim()
                            .toUpperCase(Locale.ROOT);

            header = normalize(header);

            for (String name : names) {

                String normalizedName =
                        normalize(
                                name.toUpperCase(Locale.ROOT)
                        );

                if (header.equals(normalizedName)
                        || header.contains(normalizedName)) {

                    return i;
                }
            }
        }

        return -1;
    }

    private String normalize(String text) {

        return text
                .replace("É", "E")
                .replace("È", "E")
                .replace("Ê", "E")
                .replace("Ë", "E")
                .replace("À", "A")
                .replace("Â", "A")
                .replace("Ä", "A")
                .replace("Î", "I")
                .replace("Ï", "I")
                .replace("Ô", "O")
                .replace("Ö", "O")
                .replace("Ù", "U")
                .replace("Û", "U")
                .replace("Ü", "U")
                .replace("_", " ")
                .replace("-", " ")
                .replace("  ", " ")
                .trim();
    }

    public static class StockRow {

        public String sap = "";
        public String ocp = "";
        public String designation = "";

        public String division = "";
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

        public double quantiteValue = 0;
        public double valeurValue = 0;
    }
}
