package app.personal.service;

import app.personal.dto.SavingsAccountStatementDto;
import app.personal.dto.SavingsAccountTransactionDto;
import app.personal.model.SavingsAccountStatement;
import app.personal.model.SavingsAccountTransaction;
import app.personal.repository.SavingsAccountStatementRepository;
import app.personal.repository.SavingsAccountTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SavingsAccountStatementService {

    @Autowired
    private SavingsAccountStatementRepository statementRepository;

    @Autowired
    private SavingsAccountTransactionRepository transactionRepository;

    @Transactional
    public SavingsAccountStatement saveStatement(SavingsAccountStatementDto dto) {
        SavingsAccountStatement statement = new SavingsAccountStatement();
        mapDtoToStatement(dto, statement);

        // Check for existing statement (Assuming duplication check based on account
        // number and date range)
        Optional<SavingsAccountStatement> existing = statementRepository.findByAccountNumber(dto.getAccountNumber())
                .stream()
                .filter(s -> s.getFromDate().equals(dto.getFromDate()) && s.getToDate().equals(dto.getToDate()))
                .findFirst();

        if (existing.isPresent()) {
            mapDtoToStatement(dto, existing.get());
            return statementRepository.save(existing.get());
        }

        return statementRepository.save(statement);
    }

    private void mapDtoToStatement(SavingsAccountStatementDto dto, SavingsAccountStatement statement) {
        statement.setAccountNumber(dto.getAccountNumber());
        statement.setAccountHolderName(dto.getAccountHolderName());
        statement.setIfscCode(dto.getIfscCode());
        statement.setBranchName(dto.getBranchName());
        statement.setFromDate(dto.getFromDate());
        statement.setToDate(dto.getToDate());
        statement.setOpeningBalance(dto.getOpeningBalance());
        statement.setClosingBalance(dto.getClosingBalance());

        // Clear existing transactions
        statement.getTransactions().clear();

        // Add new transactions
        for (SavingsAccountTransactionDto txDto : dto.getTransactions()) {
            SavingsAccountTransaction transaction = new SavingsAccountTransaction();
            transaction.setDate(txDto.getDate());
            transaction.setDescription(txDto.getDescription());
            transaction.setReferenceNumber(txDto.getReferenceNumber());
            transaction.setWithdrawalAmount(txDto.getWithdrawalAmount());
            transaction.setDepositAmount(txDto.getDepositAmount());
            transaction.setClosingBalance(txDto.getClosingBalance());
            transaction.setType(txDto.getType());

            statement.addTransaction(transaction);
        }
    }

    public List<SavingsAccountStatement> getStatementsByAccountNumber(String accountNumber) {
        return statementRepository.findByAccountNumber(accountNumber);
    }

    public List<SavingsAccountTransaction> getTransactions(LocalDate from, LocalDate to) {
        return transactionRepository.findByDateBetween(from, to);
    }
}
