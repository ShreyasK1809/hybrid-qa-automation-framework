package com.framework.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Reads an Excel sheet into a list of rows; row 1 is the header and becomes the map keys. */
public final class ExcelUtils {

    private ExcelUtils() { }

    public static List<Map<String, String>> readSheet(String classpathFile, String sheetName) {
        try (InputStream in = ExcelUtils.class.getClassLoader().getResourceAsStream(classpathFile)) {
            if (in == null) {
                throw new IllegalArgumentException("Excel file not found on classpath: " + classpathFile);
            }
            try (Workbook wb = new XSSFWorkbook(in)) {
                Sheet sheet = wb.getSheet(sheetName);
                if (sheet == null) {
                    throw new IllegalArgumentException("Sheet not found: " + sheetName);
                }
                DataFormatter fmt = new DataFormatter();   // formats numbers/dates exactly as shown in Excel
                Row header = sheet.getRow(0);
                int cols = header.getLastCellNum();
                List<Map<String, String>> rows = new ArrayList<>();
                for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                    Row row = sheet.getRow(r);
                    if (row == null) {
                        continue;
                    }
                    Map<String, String> data = new LinkedHashMap<>();
                    for (int c = 0; c < cols; c++) {
                        Cell cell = row.getCell(c, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        data.put(fmt.formatCellValue(header.getCell(c)), cell == null ? "" : fmt.formatCellValue(cell));
                    }
                    rows.add(data);
                }
                return rows;
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read Excel file " + classpathFile, e);
        }
    }
}
