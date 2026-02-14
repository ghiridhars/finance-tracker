package app.personal.controller;

import app.personal.dto.SavingsAccountStatementDto;
import app.personal.model.SavingsAccountStatement;
import app.personal.service.ParserService;
import app.personal.service.SavingsAccountStatementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatementController.class)
public class StatementControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ParserService parserService;

    @MockBean
    private SavingsAccountStatementService statementService;

    @Test
    void uploadHdfcSavings_shouldReturnOkAndInvokeSave() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf",
                "dummy content".getBytes());

        SavingsAccountStatementDto dto = new SavingsAccountStatementDto();
        dto.setAccountNumber("12345");

        given(parserService.parseHdfcSavings(any())).willReturn(dto);
        given(statementService.saveStatement(any())).willReturn(new SavingsAccountStatement());

        mvc.perform(multipart("/api/statements/upload")
                .file(file)
                .param("bank", "HDFC_SAVINGS")
                .param("save", "true"))
                .andExpect(status().isOk());
    }
}
