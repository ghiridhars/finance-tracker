# Finance Common Module

*Related Documentation:*
- [🏗 Core Module](finance-core.md) - Uses common DTOs for data transfer
- [📝 Parser Module](finance-parser.md) - Uses common interfaces and DTOs
- [🌐 Web Module](finance-web.md) - REST API DTOs and models
- [🌐 API Documentation](../technical/api-documentation.md) - API models based on these DTOs
- [📐 Architecture Overview](../ARCHITECTURE.md) - System integration context

## Overview

The Common module contains shared components, DTOs, and utilities used across other modules in the Finance Tracker application. It provides the foundation for type-safe data exchange between modules and ensures consistency across the application.

## Recent Implementations

### Credit Card Statement DTOs

#### CreditCardStatementDto
Data transfer object for credit card statements:
```java
public class CreditCardStatementDto {
    private LocalDate statementDate;
    private LocalDate dueDate;
    private String cardNumber;
    private String cardHolderName;
    private BigDecimal creditLimit;
    private BigDecimal availableCredit;
    private BigDecimal totalDues;
    private BigDecimal minimumAmountDue;
    private List<CreditCardTransactionDto> transactions;
}
```

#### CreditCardTransactionDto
Data transfer object for credit card transactions:
```java
public class CreditCardTransactionDto {
    private LocalDate date;
    private String description;
    private BigDecimal amount;
    private TransactionType type;
    private String referenceNumber;
}
```

### Enums

#### TransactionType
```java
public enum TransactionType {
    CREDIT,
    DEBIT
}
```

## Module Purpose

1. **Cross-Module Sharing**
   - DTOs for data transfer between modules
   - Common enums and constants
   - Shared utilities

2. **Standardization**
   - Consistent data structures
   - Common type definitions
   - Shared validation rules

3. **Integration Support**
   - Parser to core module integration
   - Web to core module integration
   - External system integration

## Usage

### In Parser Module
```java
// Converting parsed data to DTO
CreditCardStatementDto dto = new CreditCardStatementDto();
dto.setStatementDate(parsedDate);
dto.setCardNumber(parsedCardNumber);
// ... other mappings
```

### In Core Module
```java
// Converting DTO to Entity
CreditCardStatement entity = new CreditCardStatement();
mapDtoToEntity(dto, entity);
```

### In Web Module
```java
// Returning DTO in REST response
@GetMapping("/statements/{id}")
public ResponseEntity<CreditCardStatementDto> getStatement(@PathVariable Long id) {
    // ... implementation
}
```

## Future Enhancements

1. Additional DTOs
   - Savings account DTOs
   - Investment account DTOs
   - Analytics DTOs

2. Common Utilities
   - Date formatting
   - Amount formatting
   - Validation utilities

3. Integration Support
   - API client interfaces
   - Common error handling
   - Logging utilities