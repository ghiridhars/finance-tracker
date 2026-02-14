package app.personal.parser;

import app.personal.dto.SavingsAccountStatementDto;
import app.personal.dto.SavingsAccountTransactionDto;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class HdfcSavingsPdfParserTest {

    @Test
    void shouldParseSampleText() {
        // mocked text simulating PDF extraction
        String sampleText = "HDFC BANK\n" +
                "Account No : 50100234567890\n" +
                "Customer Name\n" +
                "Customer ID : 12345678\n" +
                "Statement From : 01/01/2023 To : 31/01/2023\n" +
                "IFSC Code : HDFC0001234\n" +
                "Branch : OMR BRANCH\n" +
                "\n" +
                "Date Narration Chq./Ref.No. Value Dt Withdrawal Amt. Deposit Amt. Closing Balance\n" +
                "01/01/23 OPENING BALANCE 01/01/23 0.00 0.00 10,000.00\n" +
                "02/01/23 UPI-WALMART-PAY 123456 02/01/23 500.00 0.00 9,500.00\n" +
                "05/01/23 SALARY CREDIT REF-999 05/01/23 0.00 50,000.00 59,500.00\n";

        HdfcSavingsPdfParser parser = new HdfcSavingsPdfParser();
        ParseResult result = parser.parseText(sampleText);

        assertTrue(result.isSuccess());
        SavingsAccountStatementDto statement = (SavingsAccountStatementDto) result.getResult();

        assertEquals("50100234567890", statement.getAccountNumber());
        assertEquals("HDFC0001234", statement.getIfscCode());
        assertEquals(LocalDate.of(2023, 1, 1), statement.getFromDate());
        assertEquals(LocalDate.of(2023, 1, 31), statement.getToDate());

        assertEquals(3, statement.getTransactions().size());

        SavingsAccountTransactionDto t2 = statement.getTransactions().get(1);
        assertEquals("UPI-WALMART-PAY", t2.getDescription());
        assertEquals(new BigDecimal("500.00"), t2.getWithdrawalAmount());

        SavingsAccountTransactionDto t3 = statement.getTransactions().get(2);
        assertEquals(new BigDecimal("50000.00"), t3.getDepositAmount());
    }
}
