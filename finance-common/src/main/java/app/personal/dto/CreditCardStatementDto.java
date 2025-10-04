package app.personal.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CreditCardStatementDto {
    private LocalDate statementDate;
    private LocalDate dueDate;
    private String cardNumber;
    private String cardHolderName;
    private BigDecimal creditLimit;
    private BigDecimal availableCredit;
    private BigDecimal totalDues;
    private BigDecimal minimumAmountDue;
    private List<CreditCardTransactionDto> transactions = new ArrayList<>();

    // Getters and Setters
    public LocalDate getStatementDate() { return statementDate; }
    public void setStatementDate(LocalDate statementDate) { this.statementDate = statementDate; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    public String getCardHolderName() { return cardHolderName; }
    public void setCardHolderName(String cardHolderName) { this.cardHolderName = cardHolderName; }
    public BigDecimal getCreditLimit() { return creditLimit; }
    public void setCreditLimit(BigDecimal creditLimit) { this.creditLimit = creditLimit; }
    public BigDecimal getAvailableCredit() { return availableCredit; }
    public void setAvailableCredit(BigDecimal availableCredit) { this.availableCredit = availableCredit; }
    public BigDecimal getTotalDues() { return totalDues; }
    public void setTotalDues(BigDecimal totalDues) { this.totalDues = totalDues; }
    public BigDecimal getMinimumAmountDue() { return minimumAmountDue; }
    public void setMinimumAmountDue(BigDecimal minimumAmountDue) { this.minimumAmountDue = minimumAmountDue; }
    public List<CreditCardTransactionDto> getTransactions() { return transactions; }
    public void setTransactions(List<CreditCardTransactionDto> transactions) { this.transactions = transactions; }
    public void addTransaction(CreditCardTransactionDto transaction) { this.transactions.add(transaction); }
}