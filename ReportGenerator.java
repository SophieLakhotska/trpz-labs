package org.example.accounting.api.service.strategy;

import org.example.accounting.api.model.Account;
import org.example.accounting.api.model.Transaction;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.io.ByteArrayOutputStream;
import java.util.List;

public abstract class ReportGenerator {

    public final Resource generate(Account account) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            startReport(outputStream);
            writeHeader(account, outputStream);
            writeTransactions(account.getTransactions(), outputStream);
            endReport(outputStream);
        } catch (Exception e) {
            throw new RuntimeException("Error generating report", e);
        }

        return new ByteArrayResource(outputStream.toByteArray());
    }

    protected abstract void startReport(ByteArrayOutputStream outputStream) throws Exception;

    protected abstract void writeHeader(Account account, ByteArrayOutputStream outputStream) throws Exception;

    protected abstract void writeTransactions(List<Transaction> transactions, ByteArrayOutputStream outputStream) throws Exception;

    protected abstract void endReport(ByteArrayOutputStream outputStream) throws Exception;
}
