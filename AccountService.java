package org.example.accounting.api.service;

import jakarta.transaction.Transactional;
import org.example.accounting.api.model.*;
import org.example.accounting.api.repository.AccountRepository;
import org.example.accounting.api.repository.PeriodicTransactionRepository;
import org.example.accounting.api.repository.TransactionRepository;
import org.example.accounting.api.repository.UserRepository;
import org.example.accounting.api.service.flyweight.CategoryFlyweightFactory;
import org.example.accounting.api.service.strategy.CsvReportGenerator;
import org.example.accounting.api.service.strategy.XlsReportGenerator;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {


    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final PeriodicTransactionRepository periodicTransactionRepository;
    private final UserRepository userRepository;
    private final CategoryFlyweightFactory categoryFactory;


    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository, PeriodicTransactionRepository periodicTransactionRepository, UserRepository userRepository, CategoryFlyweightFactory categoryFactory) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.periodicTransactionRepository = periodicTransactionRepository;
        this.userRepository = userRepository;
        this.categoryFactory = categoryFactory;
    }

    public Optional<Account> findById(Long id) {
        return accountRepository.findById(id);
    }

    public List<Account> findByUsername(String username) {
        return accountRepository.findByUserUsername(username);
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Transactional
    public Account create(Account account, String username) {
        var user = userRepository.findByUsername(username).orElseThrow(IllegalArgumentException::new);
        user.addAccount(account);
        return accountRepository.save(account);
    }

    @Transactional
    public List<Transaction> getTransactions(Long accountId) {
        return accountRepository.findById(accountId).map(Account::getTransactions).map(List::copyOf).orElseThrow(IllegalStateException::new);
    }

    @Transactional
    public Transaction addTransaction(Transaction transaction, Long accountId) {
        var flyweight = categoryFactory.getFlyweight(transaction.getCategory().getName());
        transaction.getCategory().setFlyweight(flyweight);

        var account = accountRepository.findById(accountId).orElseThrow(IllegalArgumentException::new);
        var balance = switch (transaction.getTransactionType()) {
            case DEPOSIT -> account.getBalance().add(transaction.getAmount());
            case WITHDRAWAL -> account.getBalance().subtract(transaction.getAmount());
        };
        account.setBalance(balance);
        account.addTransaction(transaction);
        accountRepository.save(account);

        return transaction;
    }

    @Transactional
    public PeriodicTransaction addPeriodicTransaction(PeriodicTransaction transaction, Long accountId) {
        var account = accountRepository.findById(accountId).orElseThrow(IllegalArgumentException::new);
        account.addPeriodicTransaction(transaction);
        accountRepository.save(account);
        return transaction;
    }


    @Transactional
    public Resource exportStats(Long accountId, ExportType exportType) {
        var account = accountRepository.findById(accountId).orElseThrow(IllegalArgumentException::new);
        var context = new ExportContext();
        switch (exportType) {
            case CSV -> context.setStrategy(new CsvReportGenerator());
            case XLS -> context.setStrategy(new XlsReportGenerator());
            default -> throw new IllegalArgumentException("Invalid export type");
        }
        return context.exportStats(account);
    }

}
