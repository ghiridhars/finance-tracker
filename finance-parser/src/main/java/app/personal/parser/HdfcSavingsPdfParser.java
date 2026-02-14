package app.personal.parser;

import app.personal.dto.SavingsAccountStatementDto;
import app.personal.dto.SavingsAccountTransactionDto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser for HDFC Savings Account Statements.
 */
public class HdfcSavingsPdfParser extends PdfBoxStatementParser {

    // Regex Patterns
    private static final Pattern STATEMENT_DATE_PATTERN = Pattern
            .compile("Statement\\s+From\\s*:\\s*(\\d{2}/\\d{2}/\\d{4})\\s*To\\s*:\\s*(\\d{2}/\\d{2}/\\d{4})");
    private static final Pattern ACCOUNT_INFO_PATTERN = Pattern.compile("Account\\s+No\\s*:\\s*(\\d{14})");
    private static final Pattern CUSTOMER_NAME_PATTERN = Pattern.compile("(?m)^\\s*([^\\n]+)\\s*\\n\\s*Customer\\s+ID");
    private static final Pattern IFSC_PATTERN = Pattern.compile("IFSC\\s+Code\\s*:\\s*([A-Z]{4}0[A-Z0-9]{6})");

    // Transaction Line Pattern: Date | Desc | Ref | Value Date | Debit | Credit |
    // Balance
    // 01/02/23 UPI-123... 01/02/23 500.00 0.00 10500.00
    private static final Pattern TRANSACTION_PATTERN = Pattern.compile(
            "(\\d{2}/\\d{2}/\\d{2})\\s+([^\\n]+?)\\s+(\\d{2}/\\d{2}/\\d{2})\\s+([0-9,]+\\.\\d{2})\\s+([0-9,]+\\.\\d{2})\\s+([0-9,]+\\.\\d{2})");

    // Header often spans multiple lines
    private static final Pattern BRANCH_NAME_PATTERN = Pattern.compile("Branch\\s*:\\s*([^\\n]+)");

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter SHORT_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yy");

    @Override
    protected ParseResult parseText(String text) {
        if (text == null || text.isBlank()) {
            return ParseResult.failure("Empty text");
        }

        try {
            SavingsAccountStatementDto statement = new SavingsAccountStatementDto();

            extractMetadata(text, statement);
            extractTransactions(text, statement);

            if (statement.getTransactions().isEmpty()) {
                return ParseResult.failure("No transactions found");
            }

            return ParseResult.success(statement);

        } catch (Exception e) {
            e.printStackTrace();
            return ParseResult.failure("Parsing Error: " + e.getMessage());
        }
    }

    private void extractMetadata(String text, SavingsAccountStatementDto statement) {
        // Account Number
        Matcher m = ACCOUNT_INFO_PATTERN.matcher(text);
        if (m.find()) {
            statement.setAccountNumber(m.group(1));
        }

        // Statement Period
        m = STATEMENT_DATE_PATTERN.matcher(text);
        if (m.find()) {
            statement.setFromDate(LocalDate.parse(m.group(1), DATE_FORMATTER));
            statement.setToDate(LocalDate.parse(m.group(2), DATE_FORMATTER));
        }

        // IFSC
        m = IFSC_PATTERN.matcher(text);
        if (m.find()) {
            statement.setIfscCode(m.group(1));
        }

        // Branch
        m = BRANCH_NAME_PATTERN.matcher(text);
        if (m.find()) {
            statement.setBranchName(m.group(1).trim());
        }

        // Customer Name - tricky as it's often at the top left without a specific label
        // prefix
        // Strategy: Look for the line before "Customer ID" or "Account No" in the
        // header section
        // Simplified for this implementation:
        m = CUSTOMER_NAME_PATTERN.matcher(text);
        if (m.find()) {
            statement.setAccountHolderName(m.group(1).trim());
        }
    }

    private void extractTransactions(String text, SavingsAccountStatementDto statement) {
        Matcher m = TRANSACTION_PATTERN.matcher(text);

        while (m.find()) {
            try {
                SavingsAccountTransactionDto t = new SavingsAccountTransactionDto();

                // Date
                String dateStr = m.group(1);
                t.setDate(LocalDate.parse(dateStr, SHORT_DATE_FORMATTER));

                // Description and Ref Number handling
                String rawDesc = m.group(2).trim();
                String description = rawDesc;
                String refNo = "";

                // Heuristic: Check if the last part of the string is a Reference Number
                int lastSpaceIndex = rawDesc.lastIndexOf(' ');
                if (lastSpaceIndex != -1) {
                    String lastToken = rawDesc.substring(lastSpaceIndex + 1);
                    // Check if last token is numeric (Chq No) or Ref format (Alphanumeric with
                    // digits)
                    // Excluding common words like "BALANCE", "TRANSFER" etc which are all letters
                    if (lastToken.matches(".*\\d.*") && !lastToken.matches("(?i)^(BLOCK|REV|CWDR)$")) {
                        // It contains digits, likely a RefNo/ChqNo
                        refNo = lastToken;
                        description = rawDesc.substring(0, lastSpaceIndex).trim();
                    }
                }

                t.setDescription(description);
                t.setReferenceNumber(refNo);

                // Debit / Credit / Balance
                BigDecimal debit = parseMoney(m.group(4));
                BigDecimal credit = parseMoney(m.group(5));
                BigDecimal balance = parseMoney(m.group(6));

                t.setWithdrawalAmount(debit);
                t.setDepositAmount(credit);
                t.setClosingBalance(balance);

                statement.addTransaction(t);

                // Set statement open/close balance based on first/last transaction
                statement.setClosingBalance(balance); // Update to latest line

            } catch (Exception e) {
                System.err.println("Skipping malformed line: " + m.group());
            }
        }

        // refine opening balance if transactions exist
        if (!statement.getTransactions().isEmpty()) {
            SavingsAccountTransactionDto first = statement.getTransactions().get(0);
            BigDecimal open = first.getClosingBalance()
                    .add(first.getWithdrawalAmount())
                    .subtract(first.getDepositAmount());
            statement.setOpeningBalance(open);
        }
    }

    private BigDecimal parseMoney(String amount) {
        if (amount == null)
            return BigDecimal.ZERO;
        return new BigDecimal(amount.replaceAll(",", ""));
    }
}
