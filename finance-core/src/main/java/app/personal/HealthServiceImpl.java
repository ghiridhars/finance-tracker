package app.personal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class HealthServiceImpl implements HealthService {

    @Autowired
    private DataSource dataSource;

    @Override
    public boolean isDatabaseUp() {
        try (Connection conn = dataSource.getConnection()) {
            return conn.isValid(1);
        } catch (SQLException e) {
            return false;
        }
    }
}
