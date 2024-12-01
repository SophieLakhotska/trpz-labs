package org.example.accounting.api.service.strategy;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.example.accounting.api.model.Account;
import org.example.accounting.api.model.Transaction;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class XlsStatStrategy implements StatStrategy {
    @Override
    public Resource construct(Account account) {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("Transactions");

        Row headerRow = sheet.createRow(0);
        String[] columnHeaders = {"ID", "Name", "Description", "Amount", "Timestamp", "Account", "Category", "Transaction Type"};

        for (int i = 0; i < columnHeaders.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnHeaders[i]);
            cell.setCellStyle(createHeaderCellStyle(workbook));
        }

        int rowIdx = 1;
        for (Transaction transaction : account.getTransactions()) {
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

        for (int i = 0; i < columnHeaders.length; i++) {
            sheet.autoSizeColumn(i);
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            workbook.close();
            return new ByteArrayResource(outputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private CellStyle createHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }
}
