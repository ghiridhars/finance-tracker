package app.personal.service;

import app.personal.dto.CreditCardStatementDto;
import app.personal.dto.CreditCardTransactionDto;
import app.personal.model.CreditCardStatement;
import app.personal.model.CreditCardTransaction;
import app.personal.repository.CreditCardStatementRepository;
import app.personal.repository.CreditCardTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CreditCardStatementService {
    
    @Autowired
    private CreditCardStatementRepository statementRepository;
    
    @Autowired
    private CreditCardTransactionRepository transactionRepository;

    @Transactional
    public CreditCardStatement saveStatement(CreditCardStatementDto dto) {
        CreditCardStatement statement = new CreditCardStatement();
        mapDtoToStatement(dto, statement);
        
        // Check for existing statement
        Optional<CreditCardStatement> existing = statementRepository.findByCardNumber(dto.getCardNumber())
            .stream()
            .filter(s -> s.getStatementDate().equals(dto.getStatementDate()))
            .findFirst();

        if (existing.isPresent()) {
            // Update existing statement
            mapDtoToStatement(dto, existing.get());
            return statementRepository.save(existing.get());
        }

        return statementRepository.save(statement);
    }

    private void mapDtoToStatement(CreditCardStatementDto dto, CreditCardStatement statement) {
        statement.setStatementDate(dto.getStatementDate());
        statement.setDueDate(dto.getDueDate());
        statement.setCardNumber(dto.getCardNumber());
        statement.setCardHolderName(dto.getCardHolderName());
        statement.setCreditLimit(dto.getCreditLimit());
        statement.setAvailableCredit(dto.getAvailableCredit());
        statement.setTotalDues(dto.getTotalDues());
        statement.setMinimumAmountDue(dto.getMinimumAmountDue());
        
        // Clear existing transactions
        statement.getTransactions().clear();
        
        // Add new transactions
        for (CreditCardTransactionDto txDto : dto.getTransactions()) {
            CreditCardTransaction transaction = new CreditCardTransaction();
            transaction.setDate(txDto.getDate());
            transaction.setDescription(txDto.getDescription());
            transaction.setAmount(txDto.getAmount());
            transaction.setType(txDto.getType());
            transaction.setReferenceNumber(txDto.getReferenceNumber());
            statement.addTransaction(transaction);
        }
    }

    public List<CreditCardStatement> getStatementsByCardNumber(String cardNumber) {
        return statementRepository.findByCardNumber(cardNumber);
    }

    public List<CreditCardStatement> getStatementsByDateRange(LocalDate startDate, LocalDate endDate) {
        return statementRepository.findByStatementDateBetween(startDate, endDate);
    }

    public List<CreditCardTransaction> getTransactionsByDateRange(LocalDate startDate, LocalDate endDate) {
        return transactionRepository.findByDateBetween(startDate, endDate);
    }
}