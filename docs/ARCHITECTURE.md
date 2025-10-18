# Finance Tracker Architecture

*Related Documentation:*
- [📝 Parser Module](modules/finance-parser.md) - Implementation details of Stage 1 processing
- [🏗 Core Module](modules/finance-core.md) - Implementation details of Stage 2 processing
- [📊 Database Schema](technical/database-schema.md) - Data storage details
- [🌐 API Documentation](technical/api-documentation.md) - REST API for file processing
- [💻 Frontend Module](modules/frontend.md) - Frontend implementation details

## System Overview

The Finance Tracker is built on a two-stage processing architecture that separates concerns and provides better control and visibility over the data processing pipeline. The system utilizes multiple modules to handle different aspects of processing, from file parsing to data storage and API access.

## Two-Stage Processing Architecture

```
┌────────────────────────────────────────────────────────────────────┐
│                        LOCAL FINANCIAL TRACKER                      │
├────────────────────────────────────────────────────────────────────┤
│                                                                    │
│  INPUT           STAGE 1              STAGE 2           OUTPUT     │
│                                                                    │
│  ┌─────────┐    ┌─────────────┐    ┌─────────────┐    ┌──────┐   │
│  │ Files   │──▶ │  PDF → CSV  │──▶ │ STANDARD CSV │──▶ │Final │   │
│  │ • PDF   │    │             │    │             │    │Data  │   │
│  │ • CSV   │    │ • Parse     │    │ • Process   │    │      │   │
│  └─────────┘    │ • Extract   │    │ • Validate  │    └──────┘   │
│                 └─────────────┘    └─────────────┘                │
└────────────────────────────────────────────────────────────────────┘
```

## Data Flow

1. **Stage 1: PDF/CSV → Standard Format**
   - Parse bank statement files
   - Extract transaction data
   - Normalize dates and amounts
   - Generate standardized CSV

2. **Stage 2: Standard Format → Database**
   - Process standardized CSV
   - Validate transactions
   - Deduplicate entries
   - Store in database

3. **Output Processing**
   - Generate reports
   - Provide analytics
   - Export capabilities

## Module Organization

### Parser Module (Stage 1)
- PDF text extraction
- Bank-specific parsers
- Data normalization
- CSV generation

### Core Module (Stage 2)
- Data validation
- Business rules
- Storage management
- Analytics engine

### Web Module
- REST API with OpenAPI documentation
- API endpoints for data access
- Report generation
- File management

### Frontend Module
- React + TypeScript SPA
- Modern Vite development environment
- React Query for API integration
- Interactive data visualization
- Real-time data updates

## Benefits

✅ **Clean Separation**: PDF parsing problems don't affect database logic
✅ **User Control**: Review intermediate CSV before import
✅ **Debugging**: Easily identify which stage has issues
✅ **Reusability**: CSV processor works for native bank CSVs too
✅ **Testing**: Each stage can be tested independently
✅ **Flexibility**: Can enhance either stage without affecting the other

## Implementation Details

### Parser Configuration
The parser module uses YAML configuration files to define:
- Text extraction regions
- Date formats
- Amount patterns
- Bank-specific rules

### Database Schema
- Transactions table
- Statement metadata
- Categories and rules
- Audit logs

### API Structure
- File upload endpoints
- Parser selection
- Processing controls
- Data retrieval
- Analytics endpoints