package app.personal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {
        "app.personal"
})
@EnableAsync
@EnableTransactionManagement
public class FinancialTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinancialTrackerApplication.class, args);
        System.out.println("Financial Tracker started at http://localhost:8080");
    }
}
