package org.example.accounting.api.service.factory;

import org.example.accounting.api.model.Category;
import org.example.accounting.api.model.Transaction;

import java.math.BigDecimal;

public interface TransactionFactory {
    Transaction createTransaction(String name, BigDecimal amount, String description, Category category);
}