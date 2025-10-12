package app.personal.parser;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.apache.pdfbox.text.TextPosition;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class PdfBoxStatementParser implements BankStatementParser {

    @Override
    public ParseResult parse(File file) throws ParseException {
        try (PDDocument document = PDDocument.load(file)) {
            // Use a custom PDFTextStripper that's more resilient to parsing errors
            PDFTextStripper stripper = new PDFTextStripper() {
                @Override
                protected void processTextPosition(TextPosition text) {
                    try {
                        super.processTextPosition(text);
                    } catch (Exception e) {
                        // Log the error but continue processing
                        System.err.println("Warning: Error processing text at position " + text + ": " + e.getMessage());
                    }
                }

                // Removed showText override as it's causing compile errors

                @Override
                protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
                    try {
                        super.writeString(text, textPositions);
                    } catch (Exception e) {
                        // Log the error but continue processing
                        System.err.println("Warning: Error writing string: " + text + ": " + e.getMessage());
                        // Try to write the text directly if possible
                        try {
                            output.write(text);
                        } catch (Exception e2) {
                            // Ignore if we can't even write directly
                        }
                    }
                }
            };
            
            // Configure stripper to be more lenient
            stripper.setSortByPosition(true);
            stripper.setAddMoreFormatting(false);
            stripper.setSpacingTolerance(0.5f);
            
            // Set debug parameters
            System.out.println("\nAttempting to extract text from PDF...");
            System.out.println("PDF Document Info:");
            System.out.println("- Number of pages: " + document.getNumberOfPages());
            
            // Try to get text with error handling at each level
            String text;
            try {
                text = stripper.getText(document);
            } catch (Exception e) {
                System.err.println("Warning: Error extracting text, trying fallback method: " + e.getMessage());
                // Fallback: try to extract text page by page
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < document.getNumberOfPages(); i++) {
                    try {
                        stripper.setStartPage(i + 1);
                        stripper.setEndPage(i + 1);
                        String pageText = stripper.getText(document);
                        sb.append(pageText).append("\n");
                    } catch (Exception e2) {
                        System.err.println("Warning: Failed to extract text from page " + (i + 1) + ": " + e2.getMessage());
                    }
                }
                text = sb.toString();
            }

            // Debug log the extracted text
            System.out.println("Extracted text from PDF:");
            System.out.println("----------------------------------------");
            System.out.println(text);
            System.out.println("----------------------------------------");
            return parseText(text);
        } catch (IOException e) {
            throw new ParseException("Failed to read PDF", e);
        }
    }

    /**
     * Extract raw text from the PDF using PDFBox. Exposed so callers outside this module
     * don't need to depend on PDFBox types directly.
     */
    public String extractRawText(File file) throws ParseException {
        try (PDDocument document = PDDocument.load(file)) {
            PDFTextStripper stripper = new PDFTextStripper() {
                @Override
                protected void processTextPosition(TextPosition text) {
                    try {
                        super.processTextPosition(text);
                    } catch (Exception e) {
                        // Log the error but continue processing
                        System.err.println("Warning: Error processing text at position " + text + ": " + e.getMessage());
                    }
                }
            };
            
            // Configure for more resilient extraction
            stripper.setSortByPosition(true);
            stripper.setAddMoreFormatting(false);
            stripper.setSpacingTolerance(0.5f);
            
            return stripper.getText(document);
        } catch (IOException e) {
            throw new ParseException("Failed to extract raw text", e);
        }
    }

    /**
     * Area-based parse: if you have a ParserProfile with region coords (x,y,w,h) you can extract
     * text only from that area on the first page. This is a small helper; real code should
     * iterate pages and regions robustly.
     */
    public ParseResult parseByArea(File file, ParserProfile profile) throws ParseException {
        if (profile == null || profile.getRegions() == null || profile.getRegions().isEmpty()) {
            return parse(file);
        }

        try (PDDocument document = PDDocument.load(file)) {
            PDPage page = document.getPage(0);
            PDFTextStripperByArea stripper = new PDFTextStripperByArea();
            for (String name : profile.getRegions().keySet()) {
                double[] v = profile.getRegions().get(name);
                // PDFTextStripperByArea uses java.awt.Rectangle (int) coordinates
                java.awt.geom.Rectangle2D.Double rect = new java.awt.geom.Rectangle2D.Double(v[0], v[1], v[2], v[3]);
                stripper.addRegion(name, rect);
            }
            stripper.extractRegions(page);
            StringBuilder sb = new StringBuilder();
            for (String name : profile.getRegions().keySet()) {
                sb.append(stripper.getTextForRegion(name)).append("\n---\n");
            }
            return parseText(sb.toString());
        } catch (IOException e) {
            throw new ParseException("Failed to read PDF by area", e);
        }
    }

    /**
     * Parse the raw extracted text (useful for unit-testing without PDF files)
     */
    protected abstract ParseResult parseText(String text);

    protected List<String[]> rowsFromLines(List<String> lines) {
        List<String[]> rows = new ArrayList<>();
            for (String l : lines) {
                // very small placeholder: split by tab, pipe or multiple spaces
                String[] cols = l.split("\\t|\\||\\s{2,}");
            rows.add(cols);
        }
        return rows;
    }
}
