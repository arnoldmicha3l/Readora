# Readora: A Smart Library Management System

## Project Description

Readora is a JavaFX-based desktop application designed to streamline and modernize library operations within academic institutions. It provides a complete solution for managing books, students, borrowing transactions, and reports through a structured, role-based system.

The system is built using Java, JavaFX, FXML, CSS, SQLite, JDBC, Maven, and Draw.io for UML modeling. It follows a clean MVC-inspired architecture supported by Service and DAO layers, ensuring scalability, maintainability, and clarity in design.

---

## Group Members

- Cabrera, Francis Andrei L.
- Dialimas, Nirhven Kyle C.
- Repe, Dave Laurence R.
- Supremo, Rolando P.
- Tabada, Arnold Michael P.

---

## Main Features

- Role-based authentication (Admin, Librarian, Student)  
- Book management (Add, Edit, Delete, Search, Filter)  
- Student/member management  
- Borrow and return transactions  
- Automatic book availability updates  
- Borrow records tracking  
- Reports dashboard  
- Profile and password management  
- About Us page  
- Confirmation dialogs for critical actions  
- Real-time search and filtering  
- Smooth scene transitions and navigation  
- User-friendly alerts and validations  

---

## Technologies Used

- Java  
- JavaFX  
- FXML  
- CSS  
- SQLite  
- JDBC  
- Maven  
- IntelliJ IDEA  
- GitHub  
- Draw.io (for UML Diagrams: Use Case and Class Diagram)

---

## User Roles

### Admin
- Manage books  
- Manage students  
- View borrow records  
- View reports  
- Update profile  
- Change password  

### Librarian
- Search books  
- Manage members  
- View borrowed books  
- Process returns  
- Update book availability  

### Student
- Register account  
- Browse books  
- Search and filter books  
- Borrow books  
- View borrowing history  

---

## System Modules

### 1. Authentication Module
Handles login, registration, validation, and role-based redirection.

### 2. Book Management Module
Manages all book-related operations.

### 3. Student Management Module
Handles student/member data.

### 4. Borrowing Module
Creates borrow records and updates book availability.

### 5. Returning Module
Processes returns and restores book status.

### 6. Borrow Records Module
Displays full borrowing activity.

### 7. Reports Module
Provides system analytics and summaries.

### 8. Profile Module
Allows user information updates.

### 9. Change Password Module
Handles secure password updates.

### 10. About Us Module
Displays system and developer details.

---

## Project Structure

```text
src
└── main
    ├── java
    │   └── com
    │       └── readora
    │           ├── controller
    │           ├── database
    │           ├── model
    │           ├── service
    │           └── user
    └── resources
        └── view
            ├── LoginView.fxml
            ├── RegisterView.fxml
            ├── AdminView.fxml
            ├── BookFormView.fxml
            ├── AdminStudentManagement.fxml
            ├── BorrowRecords.fxml
            ├── AdminReportsView.fxml
            ├── LibrarianLandingPage.fxml
            ├── SearchBookView.fxml
            ├── ManageMemberView.fxml
            ├── BorrowedBookView.fxml
            ├── ReturnBookView.fxml
            ├── StudentView.fxml
            ├── BrowseBooks.fxml
            ├── MyHistory.fxml
            ├── StudentProfile.fxml
            ├── ChangePassword.fxml
            ├── AboutUsView.fxml
            └── style.css
````

---

## Evaluation Criteria Implementation

### 1. Object-Oriented Programming

* Encapsulation through private fields
* Inheritance using shared structures
* Abstraction via interfaces and abstract classes
* Polymorphism through reusable DAO and service layers

---

### 2. Java Generics

* `GenericDAO<T, ID>`
* `ObservableList<T>`
* `TableView<T>`

Improves reusability, type safety, and code consistency.

---

### 3. Multithreading

* JavaFX background tasks prevent UI freezing
* Thread-safe session handling via `SessionManager`

---

### 4. Graphical User Interface

* Event-driven JavaFX UI
* Clean and consistent design
* Smooth transitions using centralized navigation
* Fully functional components (no dead buttons)

---

### 5. Database Connectivity

Uses SQLite with JDBC.

#### What is DAO?

DAO stands for **Data Access Object**.

It is a design pattern that separates database operations from the rest of the application logic.

#### Why DAO is Used

* Keeps SQL logic out of controllers
* Improves maintainability
* Promotes clean architecture
* Supports reusable database operations

#### Flow

```
Controller → Service → DAO → Database
```

#### DAO Classes

* BookDAO
* StudentDAO
* BorrowRecordDAO
* UserAccountDAO

---

### 6. UML (Draw.io)

The system includes:

* Use Case Diagram
* Class Diagram

Created using **Draw.io** for clarity and professional visualization.

The diagrams are consistent with:

* System features
* Code structure
* Class relationships

---

### 7. Design Patterns

* Singleton → DatabaseConnection
* DAO Pattern → Data handling
* MVC Architecture → System structure
* Service Layer → Business logic separation

---

### 8. Code Quality

* Clean, modular structure
* Consistent naming conventions
* Separation of concerns
* Error handling and validation
* No redundant or unused code

---

## Default Accounts

Admin
Username: admin
Password: admin123

Librarian
Username: librarian
Password: lib123

Student
Username: student
Password: stud123

---

## Database

* SQLite database: `readora.db`
* Auto-initialized using `DatabaseInitializer`

---

## How to Run

1. Open IntelliJ
2. Load project
3. Wait for Maven dependencies
4. Run:

```
com.readora.ReadoraApplication
```

---

## Testing

* Authentication testing
* CRUD operations
* Borrow and return flow
* Search and filtering
* Navigation and transitions
* Error handling

---

## Demo Flow (12 Minutes)

### Admin

* Dashboard
* Book Management
* Student Management
* Reports

### Librarian

* Search Books
* Borrowed Records
* Return Books

### Student

* Browse Books
* Borrow Book
* View History

---

## GitHub Submission

Includes:

* Source code
* FXML files
* CSS
* UML diagrams
* README.md
* pom.xml

---
## Conclusion

Readora is a fully integrated and well-structured Smart Library Management System that demonstrates a strong and practical application of software engineering principles.

The system is built with a clear separation of concerns using MVC architecture, reinforced by DAO and Service Layer patterns. It effectively combines Object-Oriented Programming concepts, Java Generics, multithreading, and database integration into a cohesive and scalable solution.

From a usability perspective, Readora delivers a clean, responsive, and intuitive interface with complete functionality, ensuring that all user actions are handled reliably with proper validation, feedback, and smooth navigation.

From a design perspective, the system maintains full consistency between UML diagrams and actual implementation, reflecting accurate real-world relationships and system behavior. The use of Draw.io ensures that both the Use Case and Class Diagrams are clear, professional, and easy to understand even at a glance.

Overall, Readora is not just a working application, but a demonstration of thoughtful system design, clean architecture, and attention to detail. It is fully prepared for presentation, evaluation, and real-world extension, meeting and exceeding the expectations of a capstone-level project.
