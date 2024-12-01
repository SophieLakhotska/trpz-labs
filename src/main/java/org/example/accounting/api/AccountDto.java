package org.example.accounting.api;

import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link org.example.accounting.api.model.Account}
 */
@Value
public class AccountDto implements Serializable {
    Long id;
    String name;
    String description;
    BigDecimal balance;
    Integer currencyCode;
}