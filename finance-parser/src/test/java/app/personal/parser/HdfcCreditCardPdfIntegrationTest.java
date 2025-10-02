package app.personal.parser;

import org.junit.jupiter.api.Test;

import java.io.File;

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

        System.out.println("Parsed rows: " + (res.getRows() == null ? 0 : res.getRows().size()));
        if (res.getRows() != null) {
            for (String[] row : res.getRows()) {
                System.out.println(String.join(" | ", row));
            }
        }

        assertNotNull(res.getRows());
        assertTrue(res.getRows().size() > 0, "Expected at least one parsed transaction");
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
