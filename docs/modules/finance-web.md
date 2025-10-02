# Finance Web Module

*Related Documentation:*
- [🌐 API Documentation](../technical/api-documentation.md) - Complete API reference
- [📝 Parser Module](finance-parser.md) - File parsing implementation
- [🏗 Core Module](finance-core.md) - Business logic and data access
- [📦 Common Module](finance-common.md) - Shared DTOs
- [📐 Architecture Overview](../ARCHITECTURE.md) - System design and flow

## Overview

The Web module provides the REST API interface for the Finance Tracker application, handling file uploads, parsing requests, and data retrieval. It acts as the primary interface between clients and the [two-stage processing architecture](../ARCHITECTURE.md#two-stage-processing-architecture).

## Recent Implementations

### Credit Card Statement API

#### Upload and Parse Endpoint
```java
@RestController
@RequestMapping("/api/credit-card")
public class CreditCardController {
    @PostMapping("/statements/upload")
    public ResponseEntity<?> uploadStatement(
        @RequestPart("file") MultipartFile file,
        @RequestParam(value = "debug", required = false) boolean debug
    )
}
```

#### Statement Retrieval Endpoints
```java
@GetMapping("/statements/{cardNumber}")
public ResponseEntity<List<CreditCardStatement>> getStatementsByCard(
    @PathVariable String cardNumber
)

@GetMapping("/statements")
public ResponseEntity<List<CreditCardStatement>> getStatementsByDateRange(
    @RequestParam LocalDate startDate,
    @RequestParam LocalDate endDate
)
```

## API Documentation

### Statement Upload
- **Endpoint**: POST `/api/credit-card/statements/upload`
- **Content-Type**: multipart/form-data
- **Parameters**:
  - `file`: PDF file (required)
  - `debug`: boolean (optional)
- **Returns**: 
  - Success: CreditCardStatement object
  - Debug Mode: Parsing details and raw text

### Statement Retrieval
- **Endpoint**: GET `/api/credit-card/statements/{cardNumber}`
- **Returns**: List of statements for the given card number

- **Endpoint**: GET `/api/credit-card/statements?startDate=&endDate=`
- **Returns**: List of statements within the date range

## Service Layer

### ParserService
- Handles file upload processing
- Validates file size and format
- Coordinates parsing process
- Returns structured data

### Error Handling
```java
@ExceptionHandler(ParseException.class)
public ResponseEntity<?> handleParseException(ParseException e) {
    return ResponseEntity.badRequest()
        .body("Failed to parse statement: " + e.getMessage());
}
```

## Future Enhancements

1. Authentication and Authorization
   - Secure endpoints
   - User-specific data access

2. Enhanced Error Handling
   - Structured error responses
   - Validation error details

3. Additional Endpoints
   - Statement deletion
   - Batch processing
   - Analytics endpoints