/**
 * Author:JananiKrishnaveni Gokeda
 * Due date March 30 ,2025
 * Professor :Sarah Khan 
 */
package dataaccesslayer;
import transferobjects.Author;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Implementation of the AuthorDAO interface that uses JDBC to interact with
 * the database and perform CRUD operations on the authors table.
 * This class obtains connections from the DataSource singleton and uses
 * try-with-resources to manage database resources.
 */

public class AuthorDAOImpl implements AuthorDAO {
/**
  * Retrieves all author records from the database.
  * @return a list of all authors in the database.
  * @throws DAOException if any SQL error occurs.
  */

    @Override
    public List<Author> getAllAuthors() throws DAOException {
        String sql = "SELECT authorID, firstName, lastName FROM authors";
        try (Connection conn = DataSource.getInstance().createConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Author> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Author(
                    rs.getInt("authorID"),
                    rs.getString("firstName"),
                    rs.getString("lastName")
                ));
            }
            return list;
        } catch (SQLException e) {
            throw new DAOException("Error fetching all authors", e);
        }
    }
    /**
     * Retrieves a specific author by their ID.
     * @param authorId the ID of the author to retrieve.
     * @return the Author object if found, or null if no match is found.
     * @throws DAOException if any SQL error occurs.
     */
    @Override
    public Author getAuthorById(int authorId) throws DAOException {
        String sql = "SELECT authorID, firstName, lastName FROM authors WHERE authorID = ?";
        try (Connection conn = DataSource.getInstance().createConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, authorId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next()
                    ? new Author(rs.getInt("authorID"), rs.getString("firstName"), rs.getString("lastName"))
                    : null;
            }
        } catch (SQLException e) {
            throw new DAOException("Error fetching author by ID", e);
        }
    }
    /**
     * Adds a new author to the database.
     * @param author the Author object containing first and last name.
     * @throws DAOException if any SQL error occurs.
     */

    @Override
    public void addAuthor(Author author) throws DAOException {
        String sql = "INSERT INTO authors (firstName, lastName) VALUES (?, ?)";
        try (Connection conn = DataSource.getInstance().createConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, author.getFirstName());
            ps.setString(2, author.getLastName());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error adding author", e);
        }
    }
    /**
     * Updates an existing author's details in the database.
     * @param author the Author object with updated data.
     * @throws DAOException if no rows are affected or a SQL error occurs.
     */

    @Override
    public void updateAuthor(Author author) throws DAOException {
        String sql = "UPDATE authors SET firstName = ?, lastName = ? WHERE authorID = ?";
        try (Connection conn = DataSource.getInstance().createConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, author.getFirstName());
            ps.setString(2, author.getLastName());
            ps.setInt(3, author.getAuthorId());
            if (ps.executeUpdate() == 0) {
                throw new DAOException("No author found with ID " + author.getAuthorId());
            }
        } catch (SQLException e) {
            throw new DAOException("Error updating author", e);
        }
    }
    /**
     * Deletes an author from the database by ID.
     * @param authorId the ID of the author to delete.
     * @throws DAOException if no rows are affected or a SQL error occurs.
     */

    @Override
    public void deleteAuthor(int authorId) throws DAOException {
        String sql = "DELETE FROM authors WHERE authorID = ?";
        try (Connection conn = DataSource.getInstance().createConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, authorId);
            if (ps.executeUpdate() == 0) {
                throw new DAOException("No author found with ID " + authorId);
            }
        } catch (SQLException e) {
            throw new DAOException("Error deleting author", e);
        }
    }
}
