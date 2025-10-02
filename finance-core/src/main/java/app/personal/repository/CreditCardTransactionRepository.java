package app.personal.repository;

import app.personal.model.CreditCardTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CreditCardTransactionRepository extends JpaRepository<CreditCardTransaction, Long> {
    List<CreditCardTransaction> findByDateBetween(LocalDate startDate, LocalDate endDate);
    List<CreditCardTransaction> findByDescriptionContainingIgnoreCase(String description);
}