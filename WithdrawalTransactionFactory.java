package org.example.accounting.api.service.factory;

import org.example.accounting.api.model.Category;
import org.example.accounting.api.model.Transaction;
import org.example.accounting.api.model.TransactionTypes;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class WithdrawalTransactionFactory implements TransactionFactory {

    @Override
    public Transaction createTransaction(String name, BigDecimal amount, String description, Category category) {
        return new Transaction.Builder()
                .name(name)
                .amount(amount)
                .description(description)
                .category(category)
                .transactionType(TransactionTypes.WITHDRAWAL)
                .build();
    }
}
