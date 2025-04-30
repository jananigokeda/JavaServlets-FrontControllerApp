/**
 * Author: JananiKrishnaveni Gokeda
 * Due date March 30 ,2025
 * Professor :Sarah Khan 
 */
package businesslayer;
import dataaccesslayer.AuthorDAO;
import dataaccesslayer.AuthorDAOImpl;
import dataaccesslayer.DAOException;
import transferobjects.Author;
import java.util.List;
/**
 * Business logic layer for Author operations.
 * This class acts as a middle layer between the servlet (presentation layer)
 * and the DAO (data access layer). It contains methods to process and forward
 * author-related operations to the DAO.
 * @author Janani
 */

public class AuthorBusinessLogic {
    private final AuthorDAO dao = new AuthorDAOImpl();
/**
  * Retrieves a list of all authors from the database.
  * @return List of all authors.
  * @throws DAOException if a data access error occurs.
  */
    public List<Author> getAllAuthors() throws DAOException {
        return dao.getAllAuthors();
    }
/**
  * Retrieves a single author by their ID.
  * @param id The ID of the author to retrieve.
  * @return The Author object if found, or null if not found.
  * @throws DAOException if a data access error occurs.
  */    
    public Author getAuthorById(int id) throws DAOException { 
        return dao.getAuthorById(id); 
    }
/**
  * Adds a new author to the database.
  * @param author The Author object to add.
  * @throws DAOException if a data access error occurs.
  */    
    public void addAuthor(Author author) throws DAOException {
        dao.addAuthor(author); 
    }
/**
  * Updates an existing author's information.
  * @param author The Author object containing updated data.
  * @throws DAOException if a data access error occurs.
  */    
    public void updateAuthor(Author author) throws DAOException { 
        dao.updateAuthor(author);
    }
/**
  * Deletes an author from the database using their ID.
  * @param id The ID of the author to delete.
  * @throws DAOException if a data access error occurs.
  */    
    public void deleteAuthor(int id) throws DAOException { 
        dao.deleteAuthor(id);
    }
}
