# Java Servlet Web Application: CRUD with Front Controller, DAO, and Singleton Patterns

This is a Java Servlet-based web application built using NetBeans (Maven project), deployed with Apache Tomcat, and connected to a MySQL database (books.sql). It demonstrates the use of **multi-tier architecture** and integrates the **Front Controller**, **DAO**, and **Singleton** design patterns.

## Technologies Used
- Java (Servlets, JDBC)
- MySQL
- NetBeans IDE (Maven project structure)
- Apache Tomcat
- HTML (index.html only for redirect)
- Javadoc for documentation

## Project Structure
- viewlayer: Contains all Servlets and Front Controller logic
- businesslayer: Contains business logic
- dataaccesslayer: Implements the DAO pattern  
- Uses a thread-safe Singleton for data source access  
- transferobjects: Encapsulates data objects (DTOs / JavaBeans)
- web: Contains index.html 
- web.xml: for deployment configuration

## Design Patterns
- DAO Pattern for encapsulating all access to the database
- Singleton Pattern for managing database connection source
- Front ControllerPattern to route all requests through a central servlet

## Architecture
- 3-Tier (Layered) Architecture:
  - Presentation Layer: viewlayer Servlets
  - Business Layer: businesslayer for processing
  - Data Access Layer: dataaccesslayer for JDBC operations

## Functionality (CRUD)
- View all records
- Add a new record
- Search record by ID
- Error message shown if ID is not found
- Update a record by ID
- Error message shown if ID is not found
- Delete a record by ID
- Error message shown if ID is not found

