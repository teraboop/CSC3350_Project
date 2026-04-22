package backend;
import java.sql.Connection;

public interface IDataSource {
    Connection getConnection();
}
