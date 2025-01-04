package org.example.accounting.api.service;


import org.example.accounting.api.model.Account;
import org.example.accounting.api.service.strategy.StatStrategy;
import org.springframework.core.io.Resource;

public class ExportContext {

    private StatStrategy strategy;

    public StatStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(StatStrategy strategy) {
        this.strategy = strategy;
    }

    public Resource exportStats(Account account) {
        return strategy.construct(account);
    }

}
