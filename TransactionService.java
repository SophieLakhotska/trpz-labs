package org.example.accounting.api.service;

import org.example.accounting.api.model.Account;
import org.example.accounting.api.model.Category;
import org.example.accounting.api.model.Transaction;
import org.example.accounting.api.model.TransactionTypes;
import org.example.accounting.api.repository.AccountRepository;
import org.example.accounting.api.repository.TransactionRepository;
import org.example.accounting.api.service.factory.DepositTransactionFactory;
import org.example.accounting.api.service.factory.TransactionFactory;
import org.example.accounting.api.service.factory.WithdrawalTransactionFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public Transaction createTransaction(Long accountId, String name, String description, BigDecimal amount,
                                         TransactionTypes type, Category category) {

        TransactionFactory factory = switch (type) {
            case DEPOSIT -> new DepositTransactionFactory();
            case WITHDRAWAL -> new WithdrawalTransactionFactory();
        };

        Transaction transaction = factory.createTransaction(name, amount, description, category);

        Account account = accountRepository.findById(accountId).orElseThrow(() -> new IllegalArgumentException("Account not found"));
        account.addTransaction(transaction);
        accountRepository.save(account);

        return transactionRepository.save(transaction);
    }
}
