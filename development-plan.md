# Development Plan & Progress

This document tracks our development progress and plans. Following a modular, incremental, test-driven approach.

## Current Status (October 2025)
- Phase 0 ✓ Complete: Project setup and foundation
- Phase 1 ✓ Complete: Core infrastructure
- Phase 2 ⚡ In Progress: Parser implementation
  - HDFC Credit Card Parser completed
  - Domain models and DTOs implemented
  - More parsers planned

**Development plan**.
We’ll keep it **modular, incremental, test‑driven** so that each step can be tested before moving on.

Here’s how I’d break it down:

***

## **Phase 0 – Setup & Foundations** ✓ COMPLETED

### **Goals:**
✓ Establish codebase, project structure, and testing setup.
✓ Set up modular architecture.

### **Completed Tasks:**
1. **Backend Project Setup** ✓
    - Created modules:
      - finance-common
      - finance-core
      - finance-parser
      - finance-web
    - Added dependencies:
      - PDF parsing (org.apache.pdfbox)
      - Spring Boot
      - SQLite & JPA
      - Testing frameworks
2. **Create frontend skeleton** (React or Vue SPA)
    - Single-page with:
        - Navbar
        - Upload form with bank/type dropdown
        - Dashboard page placeholder
3. **Set up SQLite DB schema migration scripts**
    - Tables: `transactions`, `accounts`, `categories`, `rules`, `import_files`
4. **CI/CD setup**
    - Git repo with `main` and `develop` branches
    - Add GitHub Actions or GitLab CI for:
        - Build
        - Run unit tests
        - Generate coverage report

📌 **Deliverable:**
Backend \& frontend skeleton run locally with “Hello API” call.

***

## **Phase 1 – Core Infrastructure (Week 1–2)**

### **Goals:**

- Implement CSV schema contract.
- Build Stage 2 processor.
- Test direct CSV import end‑to‑end (skipping PDFs).


### **Tasks:**

1. **Define Standard CSV Schema** in code:
    - Include validations (mandatory fields, date/amount formats)
    - Create CSV parser util with schema checks
2. **Implement Stage 2 Processor:**
    - Deduplication service
    - Categorization engine (with sample rules in JSON)
    - DB persister
    - Import summary output
3. **Implement Endpoint:**
`POST /import/standard-csv`
4. **Frontend:**
    - Add “Direct CSV Import” option
    - Render import summary after upload
5. **Testing:**
    - Golden sample CSV file → import → verify DB contents

📌 **Deliverable:**
Stage 2 working with direct CSV upload → DB → visible in a sample dashboard view.

***

## **Phase 2 – Parser Implementation** ⚡ IN PROGRESS

### **Goals:**
✓ Build configurable PDF parser framework
✓ Implement first parser (HDFC Credit Card)
- Add more bank statement parsers

### **Completed Tasks:**
1. **PDF Processing Framework** ✓
    - Created base parser classes
    - Implemented text extraction
    - Added configuration support

2. **HDFC Credit Card Parser** ✓
    - Statement metadata extraction
    - Transaction parsing
    - Credit/Debit detection
    - Data validation

### **In Progress:**
1. **Additional Parser Support**
    - HDFC Savings Account parser
    - Bank of Baroda parser implementation
    - Federal Bank parser implementation

2. **Enhanced Features**
    - Password-protected PDF support
    - Better error handling
    - Performance optimizations

```java
interface BankStatementParser {
    ParseResult parse(File file) throws ParseException;
}
```

    - CSV writer utility for standard format
2. **Session management** for preview:
    - Store intermediate CSV in temp folder keyed by `sessionId`
3. **Preview \& download API:**
    - `POST /parse/{bank-type}?preview=true` → JSON preview (first N rows + sessionId)
    - `GET /preview/{sessionId}/csv` → full CSV
4. **Frontend:**
    - File upload → preview table
    - “Confirm Import” button calls Stage 2 with `sessionId`
5. **Testing**:
    - Mock parser that reads a pre-created CSV and produces preview.

📌 **Deliverable:**
UI can upload → preview → confirm import → see results in dashboard.

***

## **Phase 3 – First Real Parser (Week 3–4)**

### **Goals:**

- Implement **HDFC Credit Card PDF parser** fully.
- End‑to‑end from PDF → CSV → DB with categorization.


### **Tasks:**

1. **HdfcCreditCardPdfParser**:
    - Parse table region (Apache PDFBox)
    - Handle wrapped descriptions
    - Detect "Cr" for credits
    - Normalize dates/amounts
2. **Config file** (`hdfc-cc-pdf.yml`) with:
    - Header markers
    - Table start regex
    - Column order
3. **Integration with Stage 1 pipeline**
4. **Test with sanitized sample PDFs**
5. **Frontend:**
    - Add “HDFC Credit Card PDF” upload option → API `/parse/hdfc-credit-card-pdf`

📌 **Deliverable:**
HDFC CC PDF can be parsed locally → previewed → imported → visible in dashboard.

***

## **Phase 4 – Additional Parsers (Week 4–6)**

### **Goals:**

- Add other bank parsers incrementally.
- All follow same Stage 1 output format.


### **Targets:**

1. HDFC Savings PDF
2. HDFC UPI Credit Card PDF
3. BoB Savings CSV
4. BoB Savings PDF
5. Federal Bank Savings PDF/CSV

**Approach:**

- One parser per week alongside test coverage.
- Same effort: config file + parser class + golden test files.

📌 **Deliverable:**
All v1 supported banks/formats in production-ready state.

***

## **Phase 5 – Dashboard \& Reporting (Week 6–7)**

### **Goals:**

- Make UI dashboard usable and attractive.
- Add filtering \& basic analytics.


### **Tasks:**

1. Backend:
    - `/dashboard`, `/dashboard/recent`, `/dashboard/categories-summary` APIs
2. Frontend:
    - Monthly totals
    - Pie chart (category share)
    - Recent transactions table
3. Testing:
    - Verify correct aggregates against test DB data

📌 **Deliverable:**
Working dashboard with summary, category chart, recent transactions.

***

## **Phase 6 – Error Handling \& Polish (Week 7–8)**

### **Goals:**

- Harden the system for real-world usage.
- Provide clear error messages \& logs.


### **Tasks:**

1. Unified error response format (code, message, suggestions)
2. Logging strategy (debug vs error)
3. Password-protected PDF support
4. Cleanup job for old session CSV files

📌 **Deliverable:**
Stable, user-friendly app that handles common edge cases.

***

## Updated Sprint Plan & Progress

| Sprint | Focus | Status | Deliverables |
| :-- | :-- | :-- | :-- |
| 0–1 | Setup & Architecture | ✓ DONE | Modular structure, basic setup |
| 1–2 | Core Domain Models | ✓ DONE | Entity models, repositories |
| 2–3 | First Parser | ⚡ ACTIVE | HDFC CC parser, DTOs |
| 3–4 | Web API Layer | PENDING | REST endpoints, controllers |
| 4–5 | More Parsers | PENDING | Additional bank support |
| 5–6 | Dashboard | PENDING | UI, analytics |
| 6–7 | Polish & Deploy | PENDING | Error handling, deployment |

### Current Sprint Achievements (Sprint 2-3)
- ✓ Implemented HDFC Credit Card parser
- ✓ Created domain models and DTOs
- ✓ Added statement parsing validation
- ⚡ Working on additional parsers


***

💡 **Why this plan works:**

- **Incremental:** You'll have a working, testable product by Sprint 2 (via direct CSV).
- **Extensible:** Adding new parsers won’t break anything else.
- **UI and backend** progress together — no waiting for each other.
- **Early wins:** Preview flow \& simple dashboard come before all banks are done.

***

If you want, I can turn this into a **Kanban-style task board** with logical groupings
(Backlog, In Progress, Testing, Done) so the team can track progress efficiently.

Do you want me to create that Kanban task layout next?

