package app.personal.service;

import app.personal.dto.CreditCardStatementDto;
import app.personal.parser.HdfcCreditCardPdfParser;
import app.personal.parser.ParseException;
import app.personal.parser.ParseResult;
import app.personal.parser.ParserProfile;
import app.personal.dto.SavingsAccountStatementDto;
import app.personal.parser.HdfcSavingsPdfParser;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

@Service
public class ParserService {

    private static final long MAX_BYTES = 10L * 1024L * 1024L; // 10MB

    public CreditCardStatementDto parseHdfcCreditCard(MultipartFile multipart) throws ParseException, IOException {
        Map<String, Object> map = parseHdfcCreditCardInternalMap(multipart);
        return (CreditCardStatementDto) map.get("statement");
    }

    public SavingsAccountStatementDto parseHdfcSavings(MultipartFile multipart) throws ParseException, IOException {
        Map<String, Object> map = parseHdfcSavingsInternalMap(multipart);
        return (SavingsAccountStatementDto) map.get("statement");
    }

    public Map<String, Object> parseHdfcCreditCardDebug(MultipartFile multipart) throws ParseException, IOException {
        return parseHdfcCreditCardInternalMap(multipart);
    }

    private Map<String, Object> parseHdfcCreditCardInternalMap(MultipartFile multipart)
            throws ParseException, IOException {
        if (multipart.getSize() > MAX_BYTES) {
            throw new IllegalArgumentException("File too large. Max allowed is 10MB");
        }

        File tmp = Files.createTempFile("hdfc-upload-", ".pdf").toFile();
        try {
            multipart.transferTo(tmp);

            // check PDF magic
            byte[] header = new byte[5];
            try (FileInputStream fis = new FileInputStream(tmp)) {
                int r = fis.read(header);
                String h = r > 0 ? new String(header, 0, r) : "";
                if (!h.startsWith("%PDF")) {
                    throw new IllegalArgumentException("Uploaded file is not a valid PDF");
                }
            }

            HdfcCreditCardPdfParser parser = new HdfcCreditCardPdfParser();

            // attempt to load YAML profile
            ParserProfile profile = null;
            try (InputStream is = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("hdfc-profile.yml")) {
                if (is != null) {
                    Yaml yaml = new Yaml();
                    profile = yaml.loadAs(is, ParserProfile.class);
                }
            } catch (Exception ignore) {
            }

            String rawText = "";
            try {
                rawText = parser.extractRawText(tmp);
            } catch (Exception e) {
                // ignore raw text extraction failures
            }

            ParseResult res;
            if (profile != null) {
                res = parser.parseByArea(tmp, profile);
            } else {
                res = parser.parse(tmp);
            }

            CreditCardStatementDto statement = (CreditCardStatementDto) res.getResult();

            Map<String, Object> map = new HashMap<>();
            if (!res.isSuccess()) {
                map.put("success", false);
                map.put("error", res.getErrorMessage());
            } else {
                map.put("success", true);
            }
            map.put("statement", statement);
            map.put("rawText", rawText);
            return map;
        } finally {
            tmp.delete();
        }
    }

    private Map<String, Object> parseHdfcSavingsInternalMap(MultipartFile multipart)
            throws ParseException, IOException {
        if (multipart.getSize() > MAX_BYTES) {
            throw new IllegalArgumentException("File too large. Max allowed is 10MB");
        }

        File tmp = Files.createTempFile("hdfc-savings-upload-", ".pdf").toFile();
        try {
            multipart.transferTo(tmp);

            // check PDF magic
            byte[] header = new byte[5];
            try (FileInputStream fis = new FileInputStream(tmp)) {
                int r = fis.read(header);
                String h = r > 0 ? new String(header, 0, r) : "";
                if (!h.startsWith("%PDF")) {
                    throw new IllegalArgumentException("Uploaded file is not a valid PDF");
                }
            }

            HdfcSavingsPdfParser parser = new HdfcSavingsPdfParser();

            String rawText = "";
            try {
                rawText = parser.extractRawText(tmp);
            } catch (Exception e) {
                // ignore raw text extraction failures
            }

            ParseResult res = parser.parse(tmp);

            SavingsAccountStatementDto statement = (SavingsAccountStatementDto) res.getResult();

            Map<String, Object> map = new HashMap<>();
            if (!res.isSuccess()) {
                map.put("success", false);
                map.put("error", res.getErrorMessage());
            } else {
                map.put("success", true);
            }
            map.put("statement", statement);
            map.put("rawText", rawText);
            return map;
        } finally {
            tmp.delete();
        }
    }
}
