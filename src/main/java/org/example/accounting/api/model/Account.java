package org.example.accounting.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "account")
public class Account {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(nullable = false)
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "balance", precision = 19, scale = 2)
  private BigDecimal balance;

  @Column(name = "currency_code")
  private Integer currencyCode;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @OrderBy("timestamp ASC")
  @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Transaction> transactions = new ArrayList<>();

  @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PeriodicTransaction> periodicTransactions = new ArrayList<>();

  public void addTransaction(Transaction transaction) {
    transactions.add(transaction);
    transaction.setAccount(this);
  }

  public void removeTransaction(Transaction transaction) {
    transactions.remove(transaction);
    transaction.setAccount(null);
  }

}