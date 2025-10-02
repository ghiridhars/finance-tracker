package app.personal.controller;

import app.personal.dto.CreditCardStatementDto;
import app.personal.model.CreditCardStatement;
import app.personal.service.CreditCardStatementService;
import app.personal.service.ParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/credit-card")
public class CreditCardController {

    @Autowired
    private ParserService parserService;

    @Autowired
    private CreditCardStatementService statementService;

    @PostMapping("/statements/upload")
    public ResponseEntity<?> uploadStatement(
            @RequestPart("file") MultipartFile file,
            @RequestParam(value = "debug", required = false, defaultValue = "false") boolean debug) {
        try {
            CreditCardStatementDto parsedStatement = parserService.parseHdfcCreditCard(file);
            CreditCardStatement savedStatement = statementService.saveStatement(parsedStatement);
            
            if (debug) {
                return ResponseEntity.ok(parserService.parseHdfcCreditCardDebug(file));
            }
            
            return ResponseEntity.ok(savedStatement);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to process statement: " + e.getMessage());
        }
    }

    @GetMapping("/statements/{cardNumber}")
    public ResponseEntity<List<CreditCardStatement>> getStatementsByCard(@PathVariable String cardNumber) {
        return ResponseEntity.ok(statementService.getStatementsByCardNumber(cardNumber));
    }

    @GetMapping("/statements")
    public ResponseEntity<List<CreditCardStatement>> getStatementsByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(statementService.getStatementsByDateRange(startDate, endDate));
    }
}