/**
 * Name :JananiKrishnaveni Gokeda
 * Assignment2
 * Due date March 30 ,2025
 * Professor :Sarah Khan 
 */
package dataaccesslayer;
import transferobjects.Author;
import java.util.List;

/**
 * The AuthorDAO interface defines the contract for
 * performing CRUD operations on the "authors" table in the database.
 * This interface follows the DAO (Data Access Object) software pattern,
 * which abstracts and encapsulates all access to the data source
 * Implementations of this interface (such as  AuthorDAOImpl})
 * will provide the actual JDBC logic using  DataSurce for DB connections.
 */
public interface AuthorDAO {
    
    List<Author> getAllAuthors() throws DAOException;
    Author getAuthorById(int authorId) throws DAOException;
    void addAuthor(Author author) throws DAOException;
    void updateAuthor(Author author) throws DAOException;
    void deleteAuthor(int authorId) throws DAOException;
}
