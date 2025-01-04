package org.example.accounting.api.service.strategy;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.example.accounting.api.model.Account;
import org.example.accounting.api.model.Transaction;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class XlsReportGenerator extends ReportGenerator {

    @Override
    protected void startReport(ByteArrayOutputStream outputStream) {
    }

    @Override
    protected void writeHeader(Account account, ByteArrayOutputStream outputStream) throws IOException {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("Transactions");

        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Name", "Description", "Amount", "Timestamp", "Account", "Category", "Transaction Type"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(createHeaderCellStyle(workbook));
        }

        workbook.write(outputStream);
    }

    @Override
    protected void writeTransactions(List<Transaction> transactions, ByteArrayOutputStream outputStream) throws IOException {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.getSheetAt(0);

        int rowIdx = 1;
        for (Transaction transaction : transactions) {
            Row row = sheet.createRow(rowIdx++);

            row.createCell(0).setCellValue(transaction.getId() != null ? transaction.getId() : 0);
            row.createCell(1).setCellValue(transaction.getName() != null ? transaction.getName() : "");
            row.createCell(2).setCellValue(transaction.getDescription() != null ? transaction.getDescription() : "");
            row.createCell(3).setCellValue(transaction.getAmount() != null ? transaction.getAmount().toString() : "0.00");
            row.createCell(4).setCellValue(transaction.getTimestamp() != null ? transaction.getTimestamp().toString() : "");
            row.createCell(5).setCellValue(transaction.getAccount() != null ? transaction.getAccount().getId() : 0);
            row.createCell(6).setCellValue(transaction.getCategory() != null ? transaction.getCategory().getId() : 0);
            row.createCell(7).setCellValue(transaction.getTransactionType() != null ? transaction.getTransactionType().toString() : "");
        }

        workbook.write(outputStream);
    }

    @Override
    protected void endReport(ByteArrayOutputStream outputStream) {
    }

    private CellStyle createHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }
}
