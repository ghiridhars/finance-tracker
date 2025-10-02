package app.personal.parser;

import app.personal.dto.CreditCardStatementDto;
import app.personal.dto.CreditCardTransactionDto;
import app.personal.model.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.List;

/**
 * Enhanced HDFC credit card parser implementation.
 * This parser extracts both statement metadata and transactions.
 */
public class HdfcCreditCardPdfParser extends PdfBoxStatementParser {
    private static final Pattern DATE_PATTERN = Pattern.compile("^(\\d{1,2}[\\/\\-]\\d{1,2}[\\/\\-]\\d{2,4})");
    private static final Pattern AMOUNT_PATTERN = Pattern.compile("([0-9]{1,3}(?:,[0-9]{3})*(?:\\.[0-9]{1,2})?(?:\\s*Cr)?)$");
    private static final Pattern STATEMENT_DATE_PATTERN = Pattern.compile("Statement Date:([\\d/]+)");
    private static final Pattern CARD_NUMBER_PATTERN = Pattern.compile("Card No:\\s*(\\d{4}\\s*\\d{2}XX\\s*XXXX\\s*\\d{4})");
    private static final Pattern PAYMENT_DUE_DATE_PATTERN = Pattern.compile("Payment Due Date\\s*([\\d/]+)");
    private static final Pattern CREDIT_LIMIT_PATTERN = Pattern.compile("Credit Limit\\s*(\\d+,?\\d*)");
    private static final Pattern CARD_HOLDER_PATTERN = Pattern.compile("Name\\s*:\\s*([^\\n]+)");
    private static final Pattern TOTAL_DUES_PATTERN = Pattern.compile("Total Dues\\s*(\\d+,?\\d*\\.?\\d*)");
    private static final Pattern MIN_DUE_PATTERN = Pattern.compile("Minimum Amount Due\\s*(\\d+,?\\d*\\.?\\d*)");
    
    private static final String TRANSACTION_SECTION_MARKER = "Domestic Transactions";
    private static final String STATEMENT_END_MARKER = "Important Information";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    protected ParseResult parseText(String text) {
        if (text == null || text.isBlank()) {
            return ParseResult.failure("Empty text");
        }

        try {
            CreditCardStatementDto statement = new CreditCardStatementDto();
            extractMetadata(text, statement);
            extractTransactions(text, statement);
            
            if (!validateStatement(statement)) {
                return ParseResult.failure("Statement validation failed");
            }

            return ParseResult.success(statement);
        } catch (Exception e) {
            return ParseResult.failure("Failed to parse statement: " + e.getMessage());
        }
    }

    private void extractMetadata(String text, CreditCardStatementDto statement) {
        // Extract Statement Date
        Matcher m = STATEMENT_DATE_PATTERN.matcher(text);
        if (m.find()) {
            statement.setStatementDate(LocalDate.parse(m.group(1).trim(), DATE_FORMATTER));
        }

        // Extract Card Number
        m = CARD_NUMBER_PATTERN.matcher(text);
        if (m.find()) {
            statement.setCardNumber(m.group(1).replaceAll("\\s+", ""));
        }

        // Extract Due Date
        m = PAYMENT_DUE_DATE_PATTERN.matcher(text);
        if (m.find()) {
            statement.setDueDate(LocalDate.parse(m.group(1).trim(), DATE_FORMATTER));
        }

        // Extract Credit Limit
        m = CREDIT_LIMIT_PATTERN.matcher(text);
        if (m.find()) {
            statement.setCreditLimit(parseMoney(m.group(1)));
        }

        // Extract Card Holder Name
        m = CARD_HOLDER_PATTERN.matcher(text);
        if (m.find()) {
            statement.setCardHolderName(m.group(1).trim());
        }

        // Extract Total Dues
        m = TOTAL_DUES_PATTERN.matcher(text);
        if (m.find()) {
            statement.setTotalDues(parseMoney(m.group(1)));
        }

        // Extract Minimum Due
        m = MIN_DUE_PATTERN.matcher(text);
        if (m.find()) {
            statement.setMinimumAmountDue(parseMoney(m.group(1)));
        }
    }

    private void extractTransactions(String text, CreditCardStatementDto statement) {
        int start = text.indexOf(TRANSACTION_SECTION_MARKER);
        int end = text.indexOf(STATEMENT_END_MARKER);
        if (start == -1 || end == -1) {
            return;
        }

        String transactionText = text.substring(start, end);
        String[] lines = transactionText.split("\\r?\\n");
        
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            
            Matcher dateMatcher = DATE_PATTERN.matcher(line);
            if (!dateMatcher.find()) continue;

            CreditCardTransactionDto transaction = new CreditCardTransactionDto();
            
            // Set Date
            String dateStr = dateMatcher.group(1);
            transaction.setDate(LocalDate.parse(dateStr, DATE_FORMATTER));

            // Extract Amount and Type
            Matcher amountMatcher = AMOUNT_PATTERN.matcher(line);
            if (amountMatcher.find()) {
                String amountStr = amountMatcher.group(1);
                boolean isCredit = amountStr.endsWith("Cr");
                if (isCredit) {
                    amountStr = amountStr.substring(0, amountStr.length() - 2).trim();
                }
                transaction.setAmount(parseMoney(amountStr));
                transaction.setType(isCredit ? TransactionType.CREDIT : TransactionType.DEBIT);
            }

            // Extract Description
            String desc = line.substring(dateMatcher.end()).trim();
            if (amountMatcher.find()) {
                desc = desc.substring(0, desc.length() - amountMatcher.group(1).length()).trim();
            }
            transaction.setDescription(desc);

            // Extract Reference Number if present
            if (desc.contains("Ref#")) {
                int refStart = desc.indexOf("Ref#") + 4;
                int refEnd = desc.indexOf(")", refStart);
                if (refEnd != -1) {
                    transaction.setReferenceNumber(desc.substring(refStart, refEnd).trim());
                }
            }

            statement.addTransaction(transaction);
        }
    }

    private boolean validateStatement(CreditCardStatementDto statement) {
        return statement.getStatementDate() != null &&
               statement.getDueDate() != null &&
               statement.getCardNumber() != null &&
               !statement.getTransactions().isEmpty();
    }

    private BigDecimal parseMoney(String amount) {
        return new BigDecimal(amount.replaceAll("[,\\s]", ""));
    }
    }
}
