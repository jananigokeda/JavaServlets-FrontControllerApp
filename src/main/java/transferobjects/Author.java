/**
 * Author: JananiKrishnaveni Gokeda
 * Due date March 30 ,2025
 * Professor :Sarah Khan 
 */
package transferobjects;

/**
 * Transfer object representing a row in the authors table.
 */
public class Author {
    // Unique ID for the author (primary key in the database)
    private int authorId;
    // First name of the author
    private String firstName;
    // Last name of the author
    private String lastName;
/**
  * No-argument constructor.
  * Required for frameworks and tools that use reflection 
  */    

    public Author() {
    }
/**
 * Constructs an Author object with the specified ID, first name, and last name.
 * @param authorId   the unique ID of the author
 * @param firstName  the author's first name
 * @param lastName   the author's last name
 */   

    public Author(int authorId, String firstName, String lastName) {
        this.authorId = authorId;
        this.firstName = firstName;
        this.lastName = lastName;
    }
   /**
     * Gets the author's ID.
     * @return the author's ID
     */  
    public int getAuthorId() {
        return authorId;
    }
    
    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return authorId + ": " + firstName + " " + lastName;
    }
}
