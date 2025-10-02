package app.personal.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    
    @OneToMany(mappedBy = "statement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CreditCardTransaction> transactions = new ArrayList<>();

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStatementDate() {
        return statementDate;
    }

    public void setStatementDate(LocalDate statementDate) {
        this.statementDate = statementDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getAvailableCredit() {
        return availableCredit;
    }

    public void setAvailableCredit(BigDecimal availableCredit) {
        this.availableCredit = availableCredit;
    }

    public BigDecimal getTotalDues() {
        return totalDues;
    }

    public void setTotalDues(BigDecimal totalDues) {
        this.totalDues = totalDues;
    }

    public BigDecimal getMinimumAmountDue() {
        return minimumAmountDue;
    }

    public void setMinimumAmountDue(BigDecimal minimumAmountDue) {
        this.minimumAmountDue = minimumAmountDue;
    }

    public List<CreditCardTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<CreditCardTransaction> transactions) {
        this.transactions = transactions;
    }

    // Helper methods
    public void addTransaction(CreditCardTransaction transaction) {
        transactions.add(transaction);
        transaction.setStatement(this);
    }

    public void removeTransaction(CreditCardTransaction transaction) {
        transactions.remove(transaction);
        transaction.setStatement(null);
    }
}