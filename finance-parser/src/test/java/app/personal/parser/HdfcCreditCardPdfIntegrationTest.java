package app.personal.parser;

import org.junit.jupiter.api.Test;

import java.io.File;

import app.personal.dto.CreditCardStatementDto;
import app.personal.dto.CreditCardTransactionDto;

import static org.junit.jupiter.api.Assertions.*;

public class HdfcCreditCardPdfIntegrationTest {

    @Test
    void parseRealPdf_shouldNotFail() throws Exception {
        File pdf = findPdfUpwards("hdfc-credit-card-statement.pdf", 4);
        assertNotNull(pdf, "Could not find hdfc-credit-card-statement.pdf in repository (searched up to 4 levels)");
        assertTrue(pdf.exists(), "Expected PDF to exist: " + (pdf == null ? "null" : pdf.getAbsolutePath()));

        HdfcCreditCardPdfParser parser = new HdfcCreditCardPdfParser();
        ParseResult res = parser.parse(pdf);

        if (!res.isSuccess()) {
            fail("Parsing failed: " + res.getErrorMessage());
        }

        CreditCardStatementDto statement = null;
        if (res.getResult() instanceof CreditCardStatementDto) {
            statement = (CreditCardStatementDto) res.getResult();
        }

        int txCount = (statement == null || statement.getTransactions() == null) ? 0 : statement.getTransactions().size();
        System.out.println("Parsed transactions: " + txCount);
        if (statement != null && statement.getTransactions() != null) {
            for (CreditCardTransactionDto tx : statement.getTransactions()) {
                System.out.println(tx.getDate() + " | " + tx.getDescription() + " | " + tx.getAmount() + " | " + tx.getType());
            }
        }

        assertNotNull(statement, "Expected a CreditCardStatementDto result");
        assertNotNull(statement.getTransactions(), "Expected transactions list to be non-null");
        assertTrue(statement.getTransactions().size() > 0, "Expected at least one parsed transaction");
    }

    private static File findPdfUpwards(String name, int maxLevels) {
        // Start from working directory for the test JVM
        File cur = new File(System.getProperty("user.dir"));
        for (int i = 0; i <= maxLevels; i++) {
            File f = new File(cur, name);
            if (f.exists()) return f.getAbsoluteFile();
            cur = cur.getParentFile();
            if (cur == null) break;
        }
        return null;
    }
}
