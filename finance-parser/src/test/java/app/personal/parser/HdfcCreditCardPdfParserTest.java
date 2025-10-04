package app.personal.parser;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import app.personal.dto.CreditCardStatementDto;
import app.personal.dto.CreditCardTransactionDto;

import static org.junit.jupiter.api.Assertions.*;

public class HdfcCreditCardPdfParserTest {

    @Test
    void parseSimpleText_shouldProduceRows() throws Exception {
        HdfcCreditCardPdfParser parser = new HdfcCreditCardPdfParser();
    // Provide minimal metadata and markers so the parser's validation succeeds
    String sample = "Statement Date:15/08/2023\n" +
            "Card No: 1234 56XX XXXX 7890\n" +
            "Payment Due Date 20/08/2023\n" +
            "Domestic Transactions\n" +
            "15/08/2023 AMAZONIN GURGAON 1,299.00\n" +
            "16/08/2023 COFFEE SHOP 150.50\n" +
            "Important Information\n";

    ParseResult res = parser.parseText(sample);
    assertTrue(res.isSuccess());

    assertTrue(res.getResult() instanceof CreditCardStatementDto);
    CreditCardStatementDto statement = (CreditCardStatementDto) res.getResult();
    List<CreditCardTransactionDto> txs = statement.getTransactions();
    assertEquals(2, txs.size());
    assertEquals(LocalDate.of(2023, 8, 15), txs.get(0).getDate());
    }
}
