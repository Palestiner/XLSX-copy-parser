package ru.palestiner.xlsx;

import jxl.Workbook;
import jxl.write.*;

import java.io.File;
import java.io.IOException;

public class XLSXFile {
    private final WritableWorkbook book;
    private final WritableSheet sheet;
    private int j = 0;

    public XLSXFile(String fileName) throws IOException {
        book = Workbook.createWorkbook(new File(fileName));
        sheet = book.createSheet("Лист1", 0);
    }

    public void writeRow(final String[] cells) throws WriteException {
        int cellsCount = cells.length;
        for (int i = 0; i < cellsCount; i++) {
            sheet.addCell(new Label(i, j, cells[i]));
        }
        j++;
    }

    public void writeBook() throws IOException, WriteException {
        book.write();
        book.close();
    }
}
