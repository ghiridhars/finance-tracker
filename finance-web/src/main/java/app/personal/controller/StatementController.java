package app.personal.controller;

import app.personal.dto.SavingsAccountStatementDto;
import app.personal.service.ParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/statements")
public class StatementController {

    @Autowired
    private ParserService parserService;

    @Autowired
    private app.personal.service.SavingsAccountStatementService statementService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadStatement(
            @RequestParam("file") MultipartFile file,
            @RequestParam("bank") String bankType,
            @RequestParam(value = "save", defaultValue = "true") boolean save) {

        try {
            if ("HDFC_SAVINGS".equalsIgnoreCase(bankType)) {
                SavingsAccountStatementDto statement = parserService.parseHdfcSavings(file);
                if (save) {
                    return ResponseEntity.ok(statementService.saveStatement(statement));
                }
                return ResponseEntity.ok(statement);
            }
            // Future extensions for other banks

            return ResponseEntity.badRequest().body("Unsupported bank type: " + bankType);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error parsing file: " + e.getMessage());
        }
    }
}
