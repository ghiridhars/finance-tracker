package app.personal.repository;

import app.personal.model.CreditCardStatement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CreditCardStatementRepository extends JpaRepository<CreditCardStatement, Long> {
    List<CreditCardStatement> findByCardNumber(String cardNumber);
    List<CreditCardStatement> findByStatementDateBetween(LocalDate startDate, LocalDate endDate);
}