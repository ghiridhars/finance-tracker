package app.personal.dto;

import app.personal.model.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;

public class CreditCardTransactionDto {
    private LocalDate date;
    private String description;
    private BigDecimal amount;
    private TransactionType type;
    private String referenceNumber;

    // Getters and Setters
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }
    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }
}