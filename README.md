# Contact Management System

A Java-based Contact Management System that allows users to store and manage their contacts efficiently. The system uses MySQL for data storage and provides a simple graphical user interface (GUI) for managing contact information.

## Features

- Add new contacts with input validation
- View, update, and delete existing contacts
- Search for contacts by name or phone number
- Robust error handling for database operations
- Client-side and server-side data validation

## Prerequisites

- Java JDK 8 or higher
- Maven
- MySQL Server
- MySQL Workbench (optional, for database management)

## Database Setup

1. Create a new MySQL database named `contact_manager`.
2. The application will automatically handle table creation.
3. Update the database configuration in `src/main/java/com/contactmanager/util/DatabaseConnection.java` if needed.

## Building the Project

This project uses Maven for dependency management. To build the project:

```bash
mvn clean install
```

## Running the Application

After building, you can run the application using:

```bash
java -jar target/contactmanager.jar
```

## Code Quality and Robustness

This project adheres to high code quality standards:
- **Error Handling**: Comprehensive `try-catch` blocks are implemented throughout the application, especially in database operations, to prevent crashes and provide informative error messages to the user. A custom `ContactManagerException` is used for consistent error propagation.
- **Data Validation**: Both client-side (GUI) and server-side (DAO) validation are implemented to ensure data integrity. This includes checks for empty fields, and basic format validation for phone numbers and email addresses.
- **Modularity**: The project follows a clear MVC (Model-View-Controller) architecture, separating concerns into `model`, `view`, `controller`, and `util` packages, promoting maintainability and scalability.
- **Documentation**: Code is well-commented, and the project includes this `README.md` for clear setup and usage instructions.

## Project Documentation

This README provides essential information for setting up, building, running, and understanding the project's features and architectural decisions. Further inline comments within the code provide detailed explanations of specific logic and functionalities.

## Contact Information
For any queries or feedback, please contact Deepanshu Chauhan.

## Configuration

Database configuration can be found in `src/main/java/com/contactmanager/dao/DatabaseConfig.java`. Update the following properties as needed:

- `URL`: JDBC connection URL
- `USER`: Database username
- `PASSWORD`: Database password
#   C o n t a c t M a n a g e m e n t S y s t e m 
 
 