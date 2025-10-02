# API Documentation

*Related Documentation:*
- [🌐 Web Module](../modules/finance-web.md) - API implementation details
- [📝 Parser Module](../modules/finance-parser.md) - File parsing endpoints
- [🏗 Core Module](../modules/finance-core.md) - Business logic behind APIs
- [📊 Database Schema](database-schema.md) - Data models used
- [📐 Architecture Overview](../ARCHITECTURE.md) - API context in system

## Current API Endpoints (October 2025)

This document provides comprehensive documentation for the Finance Tracker REST API endpoints. The API is implemented in the [Web Module](../modules/finance-web.md).

### Credit Card Statement Operations

#### Upload Statement
```http
POST /api/credit-card/statements/upload
Content-Type: multipart/form-data

Parameters:
- file: PDF file (required)
- debug: boolean (optional)

Response:
{
    "id": 1,
    "statementDate": "2025-07-17",
    "dueDate": "2025-08-06",
    "cardNumber": "4632XXXX4418",
    "cardHolderName": "GHIRIDHAR S",
    "creditLimit": 61000.00,
    "availableCredit": 38527.00,
    "totalDues": 22473.00,
    "minimumAmountDue": 1130.00,
    "transactions": [
        {
            "date": "2025-06-18",
            "description": "AMAZONIN GURGAON",
            "amount": 1469.00,
            "type": "DEBIT"
        },
        // ... more transactions
    ]
}
```

#### Get Statements by Card Number
```http
GET /api/credit-card/statements/{cardNumber}

Response: List of CreditCardStatement objects
```

#### Get Statements by Date Range
```http
GET /api/credit-card/statements?startDate={date}&endDate={date}

Parameters:
- startDate: YYYY-MM-DD
- endDate: YYYY-MM-DD

Response: List of CreditCardStatement objects
```

### Error Responses

#### Validation Error
```http
Status: 400 Bad Request
{
    "error": "validation_error",
    "message": "Invalid file format",
    "details": ["Only PDF files are supported"]
}
```

#### Parse Error
```http
Status: 400 Bad Request
{
    "error": "parse_error",
    "message": "Failed to parse statement",
    "details": ["Unable to extract transaction data"]
}
```

## Authentication

Currently, the API is not authenticated. Authentication will be added in future updates.

## Rate Limiting

- Maximum file size: 10MB
- Rate limit: Not implemented yet

## Future Endpoints

### Planned for Next Release
1. Statement Management
   - DELETE /api/credit-card/statements/{id}
   - PATCH /api/credit-card/statements/{id}

2. Analytics
   - GET /api/credit-card/analytics/spending-by-category
   - GET /api/credit-card/analytics/monthly-summary

3. Batch Operations
   - POST /api/credit-card/statements/batch-upload
   - GET /api/credit-card/statements/batch-status