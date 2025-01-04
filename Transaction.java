package org.example.accounting.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "transaction")
public class Transaction {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(nullable = false)
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "amount", precision = 19, scale = 2)
  private BigDecimal amount;

  @Column(name = "timestamp")
  private Instant timestamp = Instant.now();

  @ManyToOne
  @JoinColumn(name = "account_id")
  private Account account;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;

  @Enumerated(EnumType.STRING)
  @Column(name = "transaction_type")
  private TransactionTypes transactionType;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  private Transaction(Builder builder) {
    this.name = builder.name;
    this.description = builder.description;
    this.amount = builder.amount;
    this.category = builder.category;
    this.transactionType = builder.transactionType;
  }

  public static class Builder {
    private String name;
    private String description;
    private BigDecimal amount;
    private Category category;
    private TransactionTypes transactionType;

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder description(String description) {
      this.description = description;
      return this;
    }

    public Builder amount(BigDecimal amount) {
      this.amount = amount;
      return this;
    }

    public Builder category(Category category) {
      this.category = category;
      return this;
    }

    public Builder transactionType(TransactionTypes transactionType) {
      this.transactionType = transactionType;
      return this;
    }

    public Transaction build() {
      return new Transaction(this);
    }
  }


}