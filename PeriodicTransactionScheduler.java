package org.example.accounting.api.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.formula.functions.T;
import org.example.accounting.api.model.PeriodicTransaction;
import org.example.accounting.api.model.Transaction;
import org.example.accounting.api.repository.PeriodicTransactionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class PeriodicTransactionScheduler {
    private final PeriodicTransactionRepository periodicTransactionRepository;
    private final AccountService accountService;

    @Scheduled(fixedRate = 10000)
    public void processPeriodicTransactions() {
        periodicTransactionRepository.findAll().stream()
                .filter(this::shouldExecute)
                .forEach(this::executeTransaction);
    }

    @Transactional
    public void executeTransaction(PeriodicTransaction transaction) {
        var newTransaction = new Transaction.Builder()
                .name(transaction.getName())
                .amount(transaction.getAmount())
                .category(transaction.getCategory())
                .transactionType(transaction.getTransactionType())
                .description(transaction.getDescription())
                .build();
        accountService.addTransaction(newTransaction, transaction.getAccount().getId());
        transaction.setLastExecution(Instant.now());
        periodicTransactionRepository.save(transaction);
    }

    private boolean shouldExecute(PeriodicTransaction transaction) {
        var now = Instant.now();
        var nextExecution = calculateNextExecution(transaction);
        return nextExecution != null && now.isAfter(nextExecution);
    }

    private Instant calculateNextExecution(PeriodicTransaction transaction) {
        var lastExecution = transaction.getLastExecution();
        if (lastExecution == null) {
            return transaction.getTimestamp();
        }

        return switch (transaction.getPeriod()) {
            case MINUTELY -> lastExecution.plus(1, ChronoUnit.MINUTES);
            case HOURLY -> lastExecution.plus(1, ChronoUnit.HOURS);
            case DAILY -> lastExecution.plus(1, ChronoUnit.DAYS);
            case WEEKLY -> lastExecution.plus(1, ChronoUnit.WEEKS);
            case MONTHLY -> lastExecution.plus(1, ChronoUnit.MONTHS);
            case YEARLY -> lastExecution.plus(1, ChronoUnit.YEARS);
        };
    }

}
