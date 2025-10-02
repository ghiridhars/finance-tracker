# Finance Parser Module

*Related Documentation:*
- [📐 Architecture Overview](../ARCHITECTURE.md#two-stage-processing-architecture) - Two-stage processing details
- [📦 Common Module](finance-common.md) - Shared DTOs used by parsers
- [🌐 Web Module](finance-web.md) - File upload and parsing endpoints
- [🏗 Core Module](finance-core.md) - Stage 2 processing

## Overview

The Parser module is responsible for converting bank statements from various formats (PDF, CSV) into a standardized format. It implements the first stage of our [two-stage processing architecture](../ARCHITECTURE.md#two-stage-processing-architecture).

## Recent Implementations

### HDFC Credit Card Statement Parser
Recently implemented features:
- Full statement metadata extraction
- Transaction parsing with credit/debit detection
- Reference number extraction
- Amount normalization
- Date standardization

#### Parser Components

1. **Statement Metadata Extraction**
```java
private static final Pattern STATEMENT_DATE_PATTERN = Pattern.compile("Statement Date:([\\d/]+)");
private static final Pattern CARD_NUMBER_PATTERN = Pattern.compile("Card No:\\s*(\\d{4}\\s*\\d{2}XX\\s*XXXX\\s*\\d{4})");
private static final Pattern PAYMENT_DUE_DATE_PATTERN = Pattern.compile("Payment Due Date\\s*([\\d/]+)");
```

2. **Transaction Processing**
- Date parsing with format dd/MM/yyyy
- Amount parsing with credit/debit detection
- Description cleaning and normalization
- Reference number extraction from descriptions

3. **Data Models**
- CreditCardStatementDto for statement metadata
- CreditCardTransactionDto for individual transactions

## Supported Formats

### Currently Implemented
- HDFC Credit Card PDF Statements

### Planned Support
- HDFC Savings Account Statements
- Bank of Baroda Statements
- Federal Bank Statements

## Configuration

### Parser Profiles
Parsers can be configured using YAML profiles:

```yaml
regions:
  transactions: [50.0, 200.0, 500.0, 600.0]
```

### Error Handling
- PDF format validation
- Text extraction error handling
- Data validation checks
- Detailed error messages

## Usage Example

```java
HdfcCreditCardPdfParser parser = new HdfcCreditCardPdfParser();
ParseResult result = parser.parse(pdfFile);

if (result.isSuccess()) {
    CreditCardStatementDto statement = (CreditCardStatementDto) result.getResult();
    // Process statement data
}
```

## Testing

### Test Data
- Golden test files with sanitized data
- Various statement formats and layouts
- Edge cases and error conditions

### Test Coverage
Current test coverage for the HDFC Credit Card parser:
- Statement parsing: ✓
- Transaction extraction: ✓
- Error handling: ✓
- Edge cases: ✓

## Future Enhancements

1. Support for password-protected PDFs
2. More bank statement formats
3. Enhanced error reporting
4. Performance optimizations for large files