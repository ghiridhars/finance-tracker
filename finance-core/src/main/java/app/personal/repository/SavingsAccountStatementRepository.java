package app.personal.repository;

import app.personal.model.SavingsAccountStatement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SavingsAccountStatementRepository extends JpaRepository<SavingsAccountStatement, Long> {
    List<SavingsAccountStatement> findByAccountNumber(String accountNumber);

    List<SavingsAccountStatement> findByFromDateBetween(LocalDate startDate, LocalDate endDate);
}
