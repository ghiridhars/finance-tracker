package app.personal.dto;

import app.personal.model.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;

public class SavingsAccountTransactionDto {
    private LocalDate date;
    private String description;
    private String referenceNumber;
    private BigDecimal withdrawalAmount;
    private BigDecimal depositAmount;
    private BigDecimal closingBalance;

    // Getters and Setters
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public BigDecimal getWithdrawalAmount() {
        return withdrawalAmount;
    }

    public void setWithdrawalAmount(BigDecimal withdrawalAmount) {
        this.withdrawalAmount = withdrawalAmount;
    }

    public BigDecimal getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

    public BigDecimal getClosingBalance() {
        return closingBalance;
    }

    public void setClosingBalance(BigDecimal closingBalance) {
        this.closingBalance = closingBalance;
    }

    public TransactionType getType() {
        return (withdrawalAmount != null && withdrawalAmount.compareTo(BigDecimal.ZERO) > 0)
                ? TransactionType.DEBIT
                : TransactionType.CREDIT;
    }
}
