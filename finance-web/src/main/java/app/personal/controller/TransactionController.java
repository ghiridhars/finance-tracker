package app.personal.controller;

import app.personal.model.SavingsAccountTransaction;
import app.personal.service.SavingsAccountStatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private SavingsAccountStatementService statementService;

    @GetMapping
    public ResponseEntity<List<SavingsAccountTransaction>> getTransactions(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        if (from == null)
            from = LocalDate.now().minusDays(30);
        if (to == null)
            to = LocalDate.now();

        return ResponseEntity.ok(statementService.getTransactions(from, to));
    }
}
