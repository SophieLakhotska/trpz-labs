package org.example.accounting.api.web.controller.dto;

import java.time.LocalDateTime;

public record ReceiptInfo (LocalDateTime dateTime, double amount) {
}
