package org.example.accounting.api.web.controller;

import org.example.accounting.api.AccountDto;
import org.example.accounting.api.model.ExportType;
import org.example.accounting.api.service.AccountService;
import org.example.accounting.api.web.controller.dto.AccountCreateDto;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountDto> create(@RequestBody AccountCreateDto account) {
        var newAccount = accountService.create(account);
        var dto = new AccountDto(newAccount.getId(), newAccount.getName(), newAccount.getDescription(),
                newAccount.getBalance(), newAccount.getCurrencyCode());
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{accountId}/deposit-transactions")
    public void makeDepositTransaction(@PathVariable Long accountId, @RequestParam BigDecimal amount) {
        accountService.makeDepositTransaction(accountId, amount);
    }

    @PostMapping("/{accountId}/withdrawal-transactions")
    public void makeWithdrawalTransaction(@PathVariable Long accountId, @RequestParam BigDecimal amount) {
        accountService.makeWithdrawalTransaction(accountId, amount);
    }

    @GetMapping("/{accountId}/statistics")
    public ResponseEntity<Resource> exportStats(@PathVariable Long accountId, @RequestParam ExportType exportType) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=STATS.%s".formatted(exportType));
        return ResponseEntity.ok().headers(headers).body(accountService.exportStats(accountId, exportType));
    }

}
