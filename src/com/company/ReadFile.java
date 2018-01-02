package com.company;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by McGuirePC on 6/12/2017.
 */
public class ReadFile {

    public ArrayList<String> ReadFile(String filePath) {
        ArrayList<String> stockTickerArray = new ArrayList<String>();
        try {
            FileInputStream file = new FileInputStream(new File(filePath));
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    stockTickerArray.add(cell.getStringCellValue());
                    break;
                }
            }
            file.close();
        } catch (Exception e) {
            System.out.println("Catch 1");
            System.out.println(e.getMessage());
        }
        return stockTickerArray;
    }
}
