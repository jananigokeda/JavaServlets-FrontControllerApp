/**
 * Author: JananiKrishnaveni Gokeda
 * Due date March 30 ,2025
 * Professor :Sarah Khan 
 */
 package dataaccesslayer;
/**
 * Custom exception class for handling DAO-level errors.
 * This exception is used to wrap and rethrow SQLExceptions or
 * other data access-related exceptions from the DAO layer.
 * Using a custom exception allows the business and presentation layers
 * to handle database issues in a centralized and meaningful way.
 */

 public class DAOException extends Exception {
/**
 * Constructs a DAOException with a descriptive error message.
 */

 public DAOException(String message) { 
        super(message); 
    }
/**
 * Constructs a DAOException with a descriptive message and cause.
 */
 public DAOException(String message, Throwable cause) { super(message, cause);
    }
}
