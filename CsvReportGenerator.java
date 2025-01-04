package org.example.accounting.api.service.strategy;

import org.example.accounting.api.model.Account;
import org.example.accounting.api.model.Transaction;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.List;

public class CsvReportGenerator extends ReportGenerator {

    @Override
    protected void startReport(ByteArrayOutputStream outputStream) {
    }

    @Override
    protected void writeHeader(Account account, ByteArrayOutputStream outputStream) {
        try (PrintWriter writer = new PrintWriter(outputStream)) {
            String[] headers = {"ID", "Name", "Description", "Amount", "Timestamp", "Account", "Category", "Transaction Type"};
            writer.println(String.join(",", headers));
            writer.flush();
        }
    }

    @Override
    protected void writeTransactions(List<Transaction> transactions, ByteArrayOutputStream outputStream) {
        try (PrintWriter writer = new PrintWriter(outputStream)) {
            for (Transaction transaction : transactions) {
                String line = String.format(
                        "%s,%s,%s,%s,%s,%s,%s,%s",
                        transaction.getId() != null ? transaction.getId() : "",
                        transaction.getName() != null ? transaction.getName() : "N/A",
                        transaction.getDescription() != null ? transaction.getDescription() : "N/A",
                        transaction.getAmount() != null ? transaction.getAmount().toString() : "0.00",
                        transaction.getTimestamp() != null ? transaction.getTimestamp().toString() : "N/A",
                        transaction.getAccount() != null && transaction.getAccount().getId() != null ? transaction.getAccount().getId() : "N/A",
                        transaction.getCategory() != null && transaction.getCategory().getId() != null ? transaction.getCategory().getId() : "N/A",
                        transaction.getTransactionType() != null ? transaction.getTransactionType().toString() : "N/A"
                );
                writer.println(line);
            }
            writer.flush();
        }
    }

    @Override
    protected void endReport(ByteArrayOutputStream outputStream) {
    }
}
