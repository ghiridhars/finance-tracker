package app.personal.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class ParseControllerNegativeTests {

    @Autowired
    private MockMvc mvc;

    @Test
    void missingFile_shouldReturnBadRequest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.multipart("/api/parse/hdfc-credit-card"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void wrongContentType_shouldReturnUnsupportedMediaType() throws Exception {
        MockMultipartFile txt = new MockMultipartFile("file", "notes.txt", MediaType.TEXT_PLAIN_VALUE, "hello".getBytes());
        mvc.perform(MockMvcRequestBuilders.multipart("/api/parse/hdfc-credit-card").file(txt))
                .andExpect(MockMvcResultMatchers.status().isUnsupportedMediaType());
    }

    @Test
    void largeFile_shouldReturnBadRequest() throws Exception {
        // create a fake PDF-like header but large size (just over 10MB)
        byte[] content = new byte[10 * 1024 * 1024 + 10];
        System.arraycopy("%PDF-".getBytes(), 0, content, 0, 5);
        MockMultipartFile big = new MockMultipartFile("file", "big.pdf", "application/pdf", content);
        mvc.perform(MockMvcRequestBuilders.multipart("/api/parse/hdfc-credit-card").file(big))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
