package app.personal.service;

public class ParsedTransactionDto {
    private String bank;
    private String accountType;
    private String accountMask;
    private String date;
    private String description;
    private String amount;

    public String getBank() { return bank; }
    public void setBank(String bank) { this.bank = bank; }
    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
    public String getAccountMask() { return accountMask; }
    public void setAccountMask(String accountMask) { this.accountMask = accountMask; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getAmount() { return amount; }
    public void setAmount(String amount) { this.amount = amount; }
}
