# Finance Tracker

A modern financial statement management system with a React frontend and Spring Boot backend that helps you track and analyze your banking transactions by processing bank statements (PDF/CSV) into a structured format.

## Overview

Finance Tracker is a full-stack application that:
- Parses bank statements from various formats (PDF, CSV)
- Normalizes transaction data into a standard format
- Stores transactions in a structured database
- Provides a modern web interface for data management
- Exposes comprehensive REST APIs with OpenAPI documentation
- Provides APIs for financial analysis and reporting

### Current Status (October 2025)
- [x] Basic project structure and modules setup
- [x] HDFC Credit Card PDF parser implementation
- [x] Core domain models and DTOs for credit card statements
- [ ] Web API implementation (In Progress)
- [ ] Dashboard and Analytics (Planned)

## Project Structure

```
finance-tracker/
├── finance-common/      # Shared DTOs, utilities, and common code
├── finance-core/        # Domain models, repositories, and business logic
├── finance-parser/      # PDF/CSV parsing implementations
├── finance-web/         # REST API and web interface
└── docs/               # Detailed documentation
    ├── ARCHITECTURE.md  # System design and architecture
    ├── modules/         # Module-specific documentation
    ├── technical/      # Technical specifications
    └── guides/         # Development and usage guides
```
// - Better dependency management
// - Incremental builds
// - Easy custom tasks
```


### Version Control: **Git with conventional commits**

```bash
feat: add HDFC PDF parser
fix: handle wrapped transaction descriptions
test: add golden file tests for BOB statements
docs: update parser configuration guide
```


## Project Structure

```
financial-parser/
├── gradle/
├── src/
│   ├── main/java/com/finparser/
│   │   ├── core/                    # Core interfaces
│   │   ├── detection/               # Bank detection
│   │   ├── parsers/                 # Parser implementations
│   │   ├── normalization/           # Data normalization
│   │   ├── config/                  # Configuration management
│   │   └── utils/                   # Utilities
│   ├── main/resources/
│   │   ├── profiles/                # Bank parsing profiles (JSON)
│   │   ├── detection-rules.yml      # Detection configuration
│   │   └── application.yml          # App configuration
│   └── test/
│       ├── java/                    # Unit & integration tests
│       └── resources/
│           ├── golden-files/        # Test statement samples
│           └── expected-results/    # Expected parsing outputs
├── docs/                           # Documentation
├── build.gradle
└── README.md
```


## Documentation

### Architecture & Design
- [📐 Architecture Overview](docs/ARCHITECTURE.md) - System design, components, and data flow
  - [Database Schema](docs/technical/database-schema.md) - Data models and relationships
  - [API Documentation](docs/technical/api-documentation.md) - REST endpoints and usage

### Module Documentation
- [📦 Common Module](docs/modules/finance-common.md)
  - Shared DTOs and utilities
  - Integration with Core and Parser modules
  - Related: [Core Module](docs/modules/finance-core.md), [API Documentation](docs/technical/api-documentation.md)

- [🏗 Core Module](docs/modules/finance-core.md)
  - Domain models and business logic
  - Database integration
  - Related: [Database Schema](docs/technical/database-schema.md), [Common Module](docs/modules/finance-common.md)

- [📝 Parser Module](docs/modules/finance-parser.md)
  - PDF/CSV parsing implementations
  - Bank statement processing
  - Related: [Common Module](docs/modules/finance-common.md), [Architecture Overview](docs/ARCHITECTURE.md)

- [🌐 Web Module](docs/modules/finance-web.md)
  - REST API and controllers
  - File upload handling
  - Related: [API Documentation](docs/technical/api-documentation.md), [Parser Module](docs/modules/finance-parser.md)

### Development Guides
- [Development Guide](docs/guides/development-guide.md) - Setup and contribution guidelines
- [Testing Guide](docs/guides/testing-guide.md) - Testing strategies and practices
- [Deployment Guide](docs/guides/deployment-guide.md) - Deployment procedures

### Project Planning
- [Development Plan](development-plan.md) - Project phases and timeline

```
test/resources/golden-files/
├── hdfc_savings_sanitized.pdf
├── hdfc_cc_sanitized.pdf  
├── bob_savings_sanitized.pdf
└── expected_results/
    ├── hdfc_savings_expected.json
    └── hdfc_cc_expected.json
```

**Test-first development cycle:**

1. Create sanitized test files
2. Write expected output JSON
3. Write failing test
4. Implement parser to pass test
5. Refactor and optimize

### 2. **Incremental Development Strategy**

**Phase 1: Foundation (Week 1)**

- [ ] Core interfaces and models
- [ ] Bank detection service
- [ ] Profile loading mechanism
- [ ] Basic PDF text extraction

**Phase 2: Single Parser (Week 2)**

- [ ] HDFC Savings PDF parser (most common)
- [ ] Date/amount normalization utilities
- [ ] Basic transaction modeling
- [ ] Golden file testing

**Phase 3: Expand Coverage (Week 3)**

- [ ] HDFC Credit Card parser
- [ ] Bank of Baroda parser
- [ ] CSV parsing capability
- [ ] Error handling and validation

**Phase 4: Polish (Week 4)**

- [ ] Preview functionality
- [ ] Reconciliation checks
- [ ] Configuration externalization
- [ ] Documentation


### 3. **Code Organization Principles**

**Single Responsibility:**

```java
// Good - each class has one job
class HdfcSavingsParser
class IndianDateNormalizer  
class TransactionBuilder

// Bad - doing too much
class HdfcParserAndNormalizerAndValidator
```

**Configuration over Code:**

```java
// Avoid hardcoded patterns
Pattern.compile("HDFC Bank Ltd.*Account"); // ❌

// Use external configuration
@Value("${parser.hdfc.savings.account-pattern}")
private String accountPattern; // ✅
```


## Development Environment Setup

### 1. **Local Development Stack**

```yaml
# docker-compose.yml for optional services
version: '3.8'
services:
  sqlite-browser:
    image: sqlitebrowser/sqlitebrowser
    # For viewing database during development
    
  pdf-tools:
    image: minidocks/pdftk
    # For PDF manipulation/testing
```


### 2. **IDE Configuration**

**IntelliJ Settings:**

- Enable annotation processing
- Set up code style (Google Java Style)
- Configure live templates for common patterns
- Set up run configurations for different parsers

**Useful Plugins:**

- SonarLint (code quality)
- Rainbow Brackets (code readability)
- Database Navigator (SQLite viewing)
- PlantUML (for diagrams)


### 3. **Build Configuration**

```gradle
// build.gradle
plugins {
    id 'java'
    id 'org.springframework.boot' version '3.2.0'
    id 'jacoco' // Code coverage
    id 'checkstyle' // Code quality
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    // Core
    implementation 'org.springframework.boot:spring-boot-starter'
    implementation 'org.apache.pdfbox:pdfbox:3.0.1'
    implementation 'com.opencsv:opencsv:5.8'
    
    // Testing  
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation 'org.testcontainers:junit-jupiter'
    
    // Utilities
    implementation 'org.apache.commons:commons-lang3'
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'
}

test {
    useJUnitPlatform()
    finalizedBy jacocoTestReport
}

jacocoTestReport {
    dependsOn test
    reports {
        xml.required = true
        html.required = true
    }
}
```


## Testing Strategy

### 1. **Test Data Management**

**Create sanitized samples:**

```java
// Utility to sanitize real statements
class StatementSanitizer {
    public void sanitizeAndSave(File realStatement, File outputFile) {
        // Replace real data with fake but structurally similar data
        // Keep dates, amounts, patterns but change personal info
    }
}
```

**Golden file structure:**

```
golden-files/
├── input/
│   ├── hdfc_savings_sample1.pdf
│   └── hdfc_cc_sample1.pdf  
└── expected/
    ├── hdfc_savings_sample1_expected.json
    └── hdfc_cc_sample1_expected.json
```


### 2. **Test Categories**

```java
// Unit tests - fast, isolated
@Tag("unit")
class IndianDateNormalizerTest {
    @Test void should_parse_dd_mm_yyyy() { }
}

// Integration tests - slower, end-to-end  
@Tag("integration")
class HdfcSavingsParserIntegrationTest {
    @Test void should_parse_real_statement() { }
}

// Performance tests
@Tag("performance") 
class LargeFileParsingTest {
    @Test void should_handle_100_page_statement() { }
}
```


### 3. **Continuous Testing**

```gradle
// Run different test suites
gradle test // All tests
gradle test --tests "*Unit*" // Only unit tests
gradle test -Dtag=integration // Only integration tests
```


## Code Quality \& Standards

### 1. **Code Style**

- Google Java Style Guide
- Consistent naming (parseHdfcSavingsStatement, not parse_hdfc)
- Meaningful variable names (transactionLines, not lines)


### 2. **Error Handling Strategy**

```java
// Custom exceptions hierarchy
ParseException
├── UnsupportedStatementException
├── MalformedStatementException  
└── ConfigurationException

// Consistent error handling
public ParseResult parse(StatementFile file) {
    try {
        // parsing logic
        return ParseResult.success(transactions);
    } catch (ParseException e) {
        return ParseResult.failure(e.getMessage(), e.getDetails());
    }
}
```


### 3. **Logging Strategy**

```java
// Structured logging
log.info("Starting parse for bank={} type={} file={}", 
         bank, type, filename);
log.debug("Extracted {} transaction lines", lineCount);
log.warn("Failed to parse line {}: {}", lineNumber, line);
```


## Development Workflow

### 1. **Feature Development Flow**

1. Create feature branch: `git checkout -b feat/hdfc-credit-parser`
2. Write failing test with golden file
3. Implement minimum code to pass test
4. Add error cases and edge case tests
5. Refactor and optimize
6. Update documentation
7. Create pull request with test coverage report

### 2. **Daily Development Routine**

1. Run all tests: `gradle test`
2. Check code coverage: `gradle jacocoTestReport`
3. Run quality checks: `gradle checkstyleMain`
4. Test with real data (outside version control)
5. Document any new patterns discovered

### 3. **Release Preparation**

1. Full test suite on multiple samples
2. Performance testing with large files
3. Documentation update
4. Create release notes with supported banks/formats

## Recommended Development Order

**Start Simple:**

1. Set up project structure
2. Create one golden file test (HDFC Savings)
3. Implement basic PDF text extraction
4. Build minimal parser for that one case
5. Add normalization utilities
6. Expand to other formats incrementally

**Key Success Metrics:**

- All golden file tests passing
- >90% code coverage
- Parse 1000-line statement in <2 seconds
- Clear error messages for unsupported formats

This approach ensures you build robust, maintainable code with good test coverage and clear development progression. Would you like me to elaborate on any specific aspect?

