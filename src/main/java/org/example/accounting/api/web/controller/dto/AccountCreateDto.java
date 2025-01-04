package org.example.accounting.api.web.controller.dto;

import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link org.example.accounting.api.model.Account}
 */
@Value
public class AccountCreateDto implements Serializable {
    String name;
    String description;
    BigDecimal balance;
    Integer currencyCode;
    String userEmail;
}