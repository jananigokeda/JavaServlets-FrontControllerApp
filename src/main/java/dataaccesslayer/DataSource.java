/**
 * Author :JananiKrishnaveni Gokeda
 * Due date March 30 ,2025
 * Professor :Sarah Khan 
 */
package dataaccesslayer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * Singleton class responsible for managing the database connection.
 * This class encapsulates the configuration and logic needed to connect
 * to the MySQL database using JDBC. It is implemented using the
 * Singleton Design Pattern to ensure only one instance is created and reused.
 * Thread-safety is provided using double-checked locking.
 */ 
public class DataSource {
    private static DataSource instance;
    private final String url = "jdbc:mysql://localhost:3306/books";
    private String user, password;
    private DataSource() {
    }
/**
  * Private constructor to prevent external instantiation.
  * Database credentials can be configured via setters before use.
  */

    public static DataSource getInstance() {
        if (instance == null) {
            synchronized (DataSource.class) {
                if (instance == null) instance = new DataSource();
            }
        }
        return instance;
    }
/**
  * Returns the singleton instance of the DataSource.
  */
    

    public void setCredentials(String user, String password) {
        this.user = user;
        this.password = password;
    }
/**
  * Creates and returns a new database connection using the configured credentials.
  */

    public Connection createConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}