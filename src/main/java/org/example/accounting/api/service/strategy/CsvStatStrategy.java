package org.example.accounting.api.service.strategy;

import org.example.accounting.api.model.Account;
import org.example.accounting.api.model.Transaction;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

public class CsvStatStrategy implements StatStrategy {
    @Override
    public Resource construct(Account account) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (PrintWriter writer = new PrintWriter(outputStream)) {
            String[] headers = {"ID", "Name", "Description", "Amount", "Timestamp", "Account", "Category", "Transaction Type"};
            writer.println(String.join(",", headers));

            for (Transaction transaction : account.getTransactions()) {
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

        return new ByteArrayResource(outputStream.toByteArray());

    }
}
