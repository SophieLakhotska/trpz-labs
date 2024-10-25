package org.example.accounting.api.repository;

import org.example.accounting.api.model.PeriodicTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeriodicTransactionRepository extends JpaRepository<PeriodicTransaction, Long> {
}
