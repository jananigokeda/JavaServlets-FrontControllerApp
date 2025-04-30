/**
 * Author: JananiKrishnaveni Gokeda
 * Due date March 30 ,2025
 * Professor :Sarah Khan 
 */
package viewlayer;
import businesslayer.AuthorBusinessLogic;
import dataaccesslayer.DAOException;
import transferobjects.Author;
import dataaccesslayer.DataSource;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
/**
 * FrontController is a single servlet that handles all incoming requests and dispatches them
 * to appropriate handlers based on the "action" parameter. It is a server-side-only implementation
 * of the Front Controller pattern and supports full CRUD functionality on the authors table.
 * - Display login form and validate DB credentials
 * - List all authors
 * - Get, add, update, and delete authors by ID
 * - Manage DB user session for reuse  
 * This servlet is mapped in web.xml and accessible through the "Controller" URL pattern.
 */
public class FrontController extends HttpServlet {

    private final AuthorBusinessLogic logic = new AuthorBusinessLogic();
/**
 * Handles HTTP GET requests by delegating to processRequest.
 */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }
/**
 * Handles HTTP POST requests by delegating to processRequest.
 */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }
/**
  * Main dispatcher method that routes requests based on the "action" parameter.
  */

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");
        if (action == null) action = "loginPage";

        switch (action) {
            case "loginPage": showLoginPage(resp); break;
            case "authenticate": handleAuthenticate(req, resp); break;
            case "getAllAuthors": handleGetAllAuthors(req, resp); break;
            case "showGetAuthorByIdForm": showGetAuthorByIdForm(resp, null); break;
            case "getAuthorById": handleGetAuthorById(req, resp); break;
            case "addAuthor": showAddAuthorForm(resp, null); break;
            case "addAuthorSubmit": handleAddAuthor(req, resp); break;
            case "updateAuthor": showUpdateAuthorForm(resp, null); break;
            case "updateAuthorSubmit": handleUpdateAuthor(req, resp); break;
            case "deleteAuthor": showDeleteAuthorForm(resp, null); break;
            case "deleteAuthorSubmit": handleDeleteAuthor(req, resp); break;
            default: showError(resp, "Unknown action: " + action);
        }
    }
/** 
 * Displays the login page for DB credentials input.
 */    

    private void showLoginPage(HttpServletResponse resp) throws IOException {
        String html = generateHtml("Enter DBMS Credentials", "<h1>Enter DBMS Credentials</h1>" +
                "<form action='Controller' method='post'>" +
                "<label>Username:</label><input name='dbUser'/><br/>" +
                "<label>Password:</label><input type='password' name='dbPass'/><br/>" +
                "<input type='hidden' name='action' value='authenticate'/>" +
                "<button type='submit'>Login</button>" +
                "</form>" +
                generateActionButtons());
        sendResponse(resp, html);
    }
/** 
 * Authenticates DB credentials and stores them in session.
 */
    private void handleAuthenticate(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("dbUser");
        String password = req.getParameter("dbPass");

        HttpSession session = req.getSession();
        session.setAttribute("dbUser", username);
        session.setAttribute("dbPass", password);

        DataSource.getInstance().setCredentials(username, password);

        try (Connection conn = DataSource.getInstance().createConnection()) {
            String html = generateHtml("Enter DBMS Credentials", "<h1>Enter DBMS Credentials</h1>" +
                    "<p style='color:green;'>Login successful!</p>" +
                    generateActionButtons());
            sendResponse(resp, html);
        } catch (SQLException e) {
            String html = generateHtml("Enter DBMS Credentials", "<h1>Enter DBMS Credentials</h1>" +
                    "<p class='error'>Invalid username or password. Please try again.</p>" +
                    "<form action='Controller' method='post'>" +
                    "<label>Username:</label><input name='dbUser'/><br/>" +
                    "<label>Password:</label><input type='password' name='dbPass'/><br/>" +
                    "<input type='hidden' name='action' value='authenticate'/>" +
                    "<button type='submit'>Login</button>" +
                    "</form>" +
                    generateActionButtons());
            sendResponse(resp, html);
        }
    }
/**
  * Displays all authors in an HTML table.
 */    

    private void handleGetAllAuthors(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!restoreCredentialsFromSession(req)) {
            showLoginPage(resp);
            return;
        }

        try (PrintWriter out = resp.getWriter()) {
            List<Author> authors = logic.getAllAuthors();
            StringBuilder table = new StringBuilder("<html><body><h1>All Authors</h1><table border='1'><tr><th>ID</th><th>First</th><th>Last</th></tr>");
            for (Author a : authors) {
                table.append(String.format("<tr><td>%d</td><td>%s</td><td>%s</td></tr>", a.getAuthorId(), a.getFirstName(), a.getLastName()));
            }
            table.append("</table></body></html>");
            sendResponse(resp, table.toString());
        } catch (DAOException e) {
            showError(resp, e.getMessage());
        }
    }
/**
 * Sends a styled HTML error message to the client.
 */    

    private void showGetAuthorByIdForm(HttpServletResponse resp, String errorMessage) throws IOException {
        String html = generateHtml("Get Author by ID", "<h1>Get Author by ID</h1>" +
                (errorMessage != null ? "<p class='error'>" + errorMessage + "</p>" : "") +
                "<form action='Controller' method='get'>" +
                "<label>Author ID:</label><input name='authorId'/><br/>" +
                "<input type='hidden' name='action' value='getAuthorById'/>" +
                "<button type='submit'>Show</button>" +
                "</form>");
        sendResponse(resp, html);
    }

    private void handleGetAuthorById(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!restoreCredentialsFromSession(req)) {
            showLoginPage(resp);
            return;
        }

        String authorIdParam = req.getParameter("authorId");
        if (authorIdParam == null || authorIdParam.trim().isEmpty()) {
            showGetAuthorByIdForm(resp, "Author ID is required.");
            return;
        }

        try {
            int authorId = Integer.parseInt(authorIdParam);
            Author author = logic.getAuthorById(authorId);
            if (author != null) {
                String html = generateHtml("Author Details", "<h1>Author Details</h1>" +
                        "<p>ID: " + author.getAuthorId() + "</p>" +
                        "<p>First Name: " + author.getFirstName() + "</p>" +
                        "<p>Last Name: " + author.getLastName() + "</p>");
                sendResponse(resp, html);
            } else {
                showGetAuthorByIdForm(resp, "Author not found with ID: " + authorId);
            }
        } catch (NumberFormatException e) {
            showGetAuthorByIdForm(resp, "Invalid Author ID. Please enter a valid number.");
        } catch (DAOException e) {
            showError(resp, "Error fetching author: " + e.getMessage());
        }
    }

    private void showAddAuthorForm(HttpServletResponse resp, String errorMessage) throws IOException {
        String html = generateHtml("Add Author", "<h1>Add Author</h1>" +
                (errorMessage != null ? "<p class='error'>" + errorMessage + "</p>" : "") +
                "<form action='Controller' method='post'>" +
                "<label>First Name:</label><input name='firstName'/><br/>" +
                "<label>Last Name:</label><input name='lastName'/><br/>" +
                "<input type='hidden' name='action' value='addAuthorSubmit'/>" +
                "<button type='submit'>Add Author</button>" +
                "</form>");
        sendResponse(resp, html);
    }

    private void handleAddAuthor(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!restoreCredentialsFromSession(req)) {
            showLoginPage(resp);
            return;
        }

        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");

        if (firstName == null || firstName.trim().isEmpty() || lastName == null || lastName.trim().isEmpty()) {
            showAddAuthorForm(resp, "First name and last name cannot be empty.");
            return;
        }

        Author author = new Author(0, firstName.trim(), lastName.trim());

        try {
            logic.addAuthor(author);
            resp.sendRedirect("Controller?action=getAllAuthors");
        } catch (DAOException e) {
            showError(resp, "Error adding author: " + e.getMessage());
        }
    }

    private void showUpdateAuthorForm(HttpServletResponse resp, String errorMessage) throws IOException {
        String html = generateHtml("Update Author", "<h1>Update Author</h1>" +
                (errorMessage != null ? "<p class='error'>" + errorMessage + "</p>" : "") +
                "<form action='Controller' method='post'>" +
                "<label>Author ID:</label><input name='authorId'/><br/>" +
                "<label>First Name:</label><input name='firstName'/><br/>" +
                "<label>Last Name:</label><input name='lastName'/><br/>" +
                "<input type='hidden' name='action' value='updateAuthorSubmit'/>" +
                "<button type='submit'>Update Author</button>" +
                "</form>");
        sendResponse(resp, html);
    }

    private void handleUpdateAuthor(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!restoreCredentialsFromSession(req)) {
            showLoginPage(resp);
            return;
        }

        String authorIdParam = req.getParameter("authorId");
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");

        if (authorIdParam == null || authorIdParam.trim().isEmpty() ||
                firstName == null || firstName.trim().isEmpty() ||
                lastName == null || lastName.trim().isEmpty()) {
            showUpdateAuthorForm(resp, "All fields are required.");
            return;
        }

        try {
            int authorId = Integer.parseInt(authorIdParam);
            Author author = new Author(authorId, firstName.trim(), lastName.trim());
            logic.updateAuthor(author);
            resp.sendRedirect("Controller?action=getAllAuthors");
        } catch (NumberFormatException e) {
            showUpdateAuthorForm(resp, "Invalid Author ID. Please enter a valid number.");
        } catch (DAOException e) {
            showError(resp, "Error updating author: " + e.getMessage());
        }
    }

    private void showDeleteAuthorForm(HttpServletResponse resp, String errorMessage) throws IOException {
        String html = generateHtml("Delete Author", "<h1>Delete Author</h1>" +
                (errorMessage != null ? "<p class='error'>" + errorMessage + "</p>" : "") +
                "<form action='Controller' method='post'>" +
                "<label>Author ID:</label><input name='authorId'/><br/>" +
                "<input type='hidden' name='action' value='deleteAuthorSubmit'/>" +
                "<button type='submit'>Delete Author</button>" +
                "</form>");
        sendResponse(resp, html);
    }

    private void handleDeleteAuthor(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!restoreCredentialsFromSession(req)) {
            showLoginPage(resp);
            return;
        }

        String authorIdParam = req.getParameter("authorId");

        if (authorIdParam == null || authorIdParam.trim().isEmpty()) {
            showDeleteAuthorForm(resp, "Author ID is required.");
            return;
        }

        try {
            int authorId = Integer.parseInt(authorIdParam);
            logic.deleteAuthor(authorId);
            resp.sendRedirect("Controller?action=getAllAuthors");
        } catch (NumberFormatException e) {
            showDeleteAuthorForm(resp, "Invalid Author ID. Please enter a valid number.");
        } catch (DAOException e) {
            showError(resp, "Error deleting author: " + e.getMessage());
        }
    }
/**
 * Tries to restore DB credentials from session into DataSource.
 */ 
    private boolean restoreCredentialsFromSession(HttpServletRequest req) {
        HttpSession session = req.getSession();
        String username = (String) session.getAttribute("dbUser");
        String password = (String) session.getAttribute("dbPass");
        if (username != null && password != null) {
            DataSource.getInstance().setCredentials(username, password);
            return true;
        }
        return false;
    }
 /**
  * Sends a styled HTML error message to the client.
  */   

    private void showError(HttpServletResponse resp, String msg) throws IOException {
        String html = generateHtml("Error", "<h1>Error: " + msg + "</h1>");
        sendResponse(resp, html);
    }
    /**
     * Helper method to generate consistent HTML layout.
     */

    private String generateHtml(String title, String content) {
        return "<html><head><title>" + title + "</title><style>" +
                "body{background:#FAF3E0;font-family:Georgia;} .container{width:400px;margin:80px auto;}" +
                "label{display:inline-block;width:100px;font-weight:bold;}input{width:200px;padding:4px;margin:5px;}" +
                ".buttons{text-align:center;margin-top:20px;}button{margin:0 5px;padding:6px 12px;}" +
                ".error{color:red;}" +
                "</style></head><body><div class='container'>" + content + "</div></body></html>";
    }
/**
 * Creates navigation buttons for all CRUD operations.
 */
    private String generateActionButtons() {
        return "<div class='buttons'>" +
                "<form action='Controller' method='get' style='display:inline;'>" +
                "<button name='action' value='getAllAuthors'>GetAllAuthors</button></form>" +
                "<form action='Controller' method='get' style='display:inline;'>" +
                "<button name='action' value='showGetAuthorByIdForm'>GetAuthorByAuthorId</button></form>" +
                "<form action='Controller' method='get' style='display:inline;'>" +
                "<button name='action' value='addAuthor'>AddAuthor</button></form>" +
                "<form action='Controller' method='get' style='display:inline;'>" +
                "<button name='action' value='updateAuthor'>UpdateAuthorById</button></form>" +
                "<form action='Controller' method='get' style='display:inline;'>" +
                "<button name='action' value='deleteAuthor'>DeleteAuthorById</button></form>" +
                "</div>";
    }
/**
  * Sends an HTML response to the browser.
 */    

    private void sendResponse(HttpServletResponse resp, String html) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.println(html);
    }
}
