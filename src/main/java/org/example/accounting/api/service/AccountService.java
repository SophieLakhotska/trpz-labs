package org.example.accounting.api.service;

import jakarta.transaction.Transactional;
import org.example.accounting.api.model.Account;
import org.example.accounting.api.model.ExportType;
import org.example.accounting.api.model.Transaction;
import org.example.accounting.api.model.TransactionTypes;
import org.example.accounting.api.repository.AccountRepository;
import org.example.accounting.api.service.strategy.CsvStatStrategy;
import org.example.accounting.api.service.strategy.XlsStatStrategy;
import org.example.accounting.api.web.controller.dto.AccountCreateDto;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class AccountService {


    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account create(AccountCreateDto accountCreateDto) {
        Account account = new Account();
        account.setDescription(accountCreateDto.getDescription());
        account.setName(accountCreateDto.getName());
        account.setBalance(accountCreateDto.getBalance());
        account.setCurrencyCode(accountCreateDto.getCurrencyCode());
        return accountRepository.save(account);
    }

    @Transactional
    public void makeDepositTransaction(Long accountId, BigDecimal amount) {
        var account = accountRepository.findById(accountId).orElseThrow(IllegalArgumentException::new);
        account.setBalance(account.getBalance().add(amount));

        var transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionTypes.DEPOSIT);
        account.addTransaction(transaction);

        accountRepository.save(account);
    }

    @Transactional
    public void makeWithdrawalTransaction(Long accountId, BigDecimal amount) {
        var account = accountRepository.findById(accountId).orElseThrow(IllegalArgumentException::new);
        account.setBalance(account.getBalance().subtract(amount));
        if (account.getBalance().compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Not enough balance");

        var transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionTypes.WITHDRAWAL);
        account.addTransaction(transaction);
        accountRepository.save(account);
    }

    public Resource exportStats(Long accountId, ExportType exportType) {
        var account = accountRepository.findById(accountId).orElseThrow(IllegalArgumentException::new);
        var context = new ExportContext();
        switch (exportType) {
            case CSV -> context.setStrategy(new CsvStatStrategy());
            case XLS -> context.setStrategy(new XlsStatStrategy());
            default -> throw new IllegalArgumentException("Invalid export type");
        }
        return context.exportStats(account);
    }
}
