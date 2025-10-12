package app.personal.controller;

import app.personal.service.ParserService;
import app.personal.dto.CreditCardStatementDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/parse")
public class ParseController {

    @Autowired
    private ParserService parserService;

    @PostMapping("/hdfc-credit-card")
    public ResponseEntity<?> parseHdfc(@RequestPart("file") MultipartFile file, @org.springframework.web.bind.annotation.RequestParam(value = "debug", required = false, defaultValue = "false") boolean debug) {
        // basic content-type validation (client-provided header)
        String contentType = file.getContentType();
        if (contentType == null || !(contentType.equals("application/pdf") || contentType.equals("application/octet-stream"))) {
            throw new ApiException(415, "Unsupported media type: expected PDF");
        }

        try {
            if (debug) {
                java.util.Map<String, Object> debugMap = parserService.parseHdfcCreditCardDebug(file);
                return ResponseEntity.ok(debugMap);
            }
            // delegate to service which will also enforce size limits and deeper checks
            CreditCardStatementDto statement = parserService.parseHdfcCreditCard(file);
            Map<String, Object> body = new HashMap<>();
            body.put("success", true);
            body.put("statement", statement);
            // Add rows field for backward compatibility
            if (statement != null && statement.getTransactions() != null) {
                body.put("rows", statement.getTransactions());
            } else {
                body.put("rows", new java.util.ArrayList<>());
            }
            return ResponseEntity.ok(body);
        } catch (IllegalArgumentException iae) {
            throw new ApiException(400, iae.getMessage());
        } catch (Exception e) {
            throw new ApiException(500, "Failed to parse uploaded PDF: " + e.getMessage());
        }
    }
}
