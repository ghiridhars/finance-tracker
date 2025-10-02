package app.personal.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootTest
@AutoConfigureMockMvc
public class ParseControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void uploadPdf_shouldReturnRows() throws Exception {
        Path start = Paths.get(System.getProperty("user.dir"));
        Path found = null;
        while (start != null) {
            Path candidate = start.resolve("hdfc-credit-card-statement.pdf");
            if (Files.exists(candidate)) {
                found = candidate;
                break;
            }
            start = start.getParent();
        }

        if (found == null) {
            throw new java.io.FileNotFoundException("hdfc-credit-card-statement.pdf not found from working dir");
        }

    try (FileInputStream fis = new FileInputStream(found.toFile())) {
        byte[] data = fis.readAllBytes();
        org.springframework.mock.web.MockMultipartFile mf = new org.springframework.mock.web.MockMultipartFile("file", "hdfc-credit-card-statement.pdf", "application/pdf", data);
        mvc.perform(MockMvcRequestBuilders.multipart("/api/parse/hdfc-credit-card").file(mf))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
            .andExpect(MockMvcResultMatchers.jsonPath("$.rows").isArray());
    }
    }
}
