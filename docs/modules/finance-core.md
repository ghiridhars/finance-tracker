# Finance Core Module

*Related Documentation:*
- [📐 Architecture Overview](../ARCHITECTURE.md#two-stage-processing-architecture) - Two-stage processing details
- [📊 Database Schema](../technical/database-schema.md) - Entity relationship diagrams
- [📝 Parser Module](finance-parser.md) - Stage 1 processing implementation
- [📦 Common Module](finance-common.md) - Shared DTOs and interfaces
- [🌐 API Documentation](../technical/api-documentation.md) - REST API that uses core services

## Overview

The Core module contains the domain models, repositories, and business logic for the Finance Tracker application. It implements the second stage of our [two-stage processing architecture](../ARCHITECTURE.md#two-stage-processing-architecture) and manages all database interactions.

## Recent Implementations

### Credit Card Statement Domain Models

#### CreditCardStatement Entity
```java
@Entity
@Table(name = "credit_card_statements")
public class CreditCardStatement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate statementDate;
    private LocalDate dueDate;
    private String cardNumber;
    private String cardHolderName;
    private BigDecimal creditLimit;
    private BigDecimal availableCredit;
    private BigDecimal totalDues;
    private BigDecimal minimumAmountDue;
    
    @OneToMany(mappedBy = "statement", cascade = CascadeType.ALL)
    private List<CreditCardTransaction> transactions;
}
```

#### CreditCardTransaction Entity
```java
@Entity
@Table(name = "credit_card_transactions")
public class CreditCardTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private String description;
    private BigDecimal amount;
    private TransactionType type;
    private String referenceNumber;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private CreditCardStatement statement;
}
```

### Repositories

#### CreditCardStatementRepository
```java
@Repository
public interface CreditCardStatementRepository 
    extends JpaRepository<CreditCardStatement, Long> {
    List<CreditCardStatement> findByCardNumber(String cardNumber);
    List<CreditCardStatement> findByStatementDateBetween(
        LocalDate startDate, 
        LocalDate endDate
    );
}
```

#### CreditCardTransactionRepository
```java
@Repository
public interface CreditCardTransactionRepository 
    extends JpaRepository<CreditCardTransaction, Long> {
    List<CreditCardTransaction> findByDateBetween(
        LocalDate startDate, 
        LocalDate endDate
    );
    List<CreditCardTransaction> findByDescriptionContainingIgnoreCase(
        String description
    );
}
```

## Services

### CreditCardStatementService
- Statement persistence
- Transaction management
- Data validation
- Business rules implementation

## Database Schema

### Tables
- credit_card_statements
- credit_card_transactions

### Relationships
- One-to-Many between Statement and Transactions
- Proper indexing on search fields
- Cascade operations for data integrity

## Future Enhancements

1. Additional financial product support
   - Savings accounts
   - Investment accounts
   - Loan accounts

2. Enhanced querying capabilities
   - Complex search criteria
   - Aggregation functions
   - Custom reports

3. Data enrichment
   - Category management
   - Merchant recognition
   - Transaction patterns