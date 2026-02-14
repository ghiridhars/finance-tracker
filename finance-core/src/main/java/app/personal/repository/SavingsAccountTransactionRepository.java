package app.personal.repository;

import app.personal.model.SavingsAccountTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SavingsAccountTransactionRepository extends JpaRepository<SavingsAccountTransaction, Long> {
    List<SavingsAccountTransaction> findByDateBetween(LocalDate startDate, LocalDate endDate);

    List<SavingsAccountTransaction> findByDescriptionContainingIgnoreCase(String description);
}
