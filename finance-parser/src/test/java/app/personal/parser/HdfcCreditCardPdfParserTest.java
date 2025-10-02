package app.personal.parser;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HdfcCreditCardPdfParserTest {

    @Test
    void parseSimpleText_shouldProduceRows() throws Exception {
        HdfcCreditCardPdfParser parser = new HdfcCreditCardPdfParser();
        String sample = "15/08/2023 AMAZONIN GURGAON 1,299.00\n" +
                        "16/08/2023 COFFEE SHOP 150.50\n" +
                        "Summary page";

        ParseResult res = parser.parseText(sample);
        assertTrue(res.isSuccess());
        List<String[]> rows = res.getRows();
        assertEquals(2, rows.size());
        assertEquals("15/08/2023", rows.get(0)[3]);
    }
}
