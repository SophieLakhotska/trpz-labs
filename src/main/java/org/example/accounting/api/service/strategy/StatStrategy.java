package org.example.accounting.api.service.strategy;

import org.example.accounting.api.model.Account;
import org.springframework.core.io.Resource;

public interface StatStrategy {
    Resource construct(Account account);
}
