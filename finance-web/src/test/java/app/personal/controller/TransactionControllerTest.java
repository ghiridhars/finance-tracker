package app.personal.controller;

import app.personal.model.SavingsAccountTransaction;
import app.personal.service.SavingsAccountStatementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SavingsAccountStatementService statementService;

    @Test
    void getTransactions_shouldReturnList() throws Exception {
        SavingsAccountTransaction t = new SavingsAccountTransaction();
        t.setId(1L);
        List<SavingsAccountTransaction> list = Collections.singletonList(t);

        given(statementService.getTransactions(any(), any())).willReturn(list);

        mvc.perform(get("/api/transactions")
                .param("from", LocalDate.now().toString())
                .param("to", LocalDate.now().toString()))
                .andExpect(status().isOk());
    }
}
