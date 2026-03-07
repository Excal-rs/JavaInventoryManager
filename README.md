# Java Inventory Management System (IMS)

A desktop-based Inventory Management System built with Java and JavaFX. This application allows users to manage inventory items, track specific item instances, and store custom data fields in a local database.

## Features

* **User Authentication**: Secure Sign Up and Log In functionality for individual user accounts.
* **Dashboard Overview**: A central dashboard displaying the total inventory value and total item quantity.
* **Item Management**: Add, edit, view, and delete inventory items.
* **Instance Tracking**: Toggle the ability to track individual instances of an item, including specific notes and locations for each instance.
* **Custom Fields**: Add up to two custom data fields (Title and Value) per item to store extra information.
* **Search Functionality**: Quickly search for items by name directly from the dashboard.

## Technologies Used

* **Java**: Core application logic.
* **JavaFX**: User interface design and controllers.
* **SQLite**: Local database storage for users, items, instances, and custom fields.
* **Maven**: Project build and dependency management.

## Getting Started

### Prerequisites

* Java Development Kit (JDK) 21 or higher.

### Installation & Running

1.  Clone the repository.
2.  Navigate to the project root directory.
3.  Run the application using the included Maven wrapper:
    * **On Windows**:
        ```bash
        mvnw.cmd clean javafx:run
        ```
    * **On Linux/Mac**:
        ```bash
        ./mvnw clean javafx:run
        ```

## Project Structure

* **Controllers**: Contains the Java code handling user interactions and UI updates.
* **Classes**: Contains the data models and validation logic.
* **FXML**: Defines the layout and structure of the application's screens (Dashboard, Login, Item management, etc.).
* **SQLdb**: Contains the local SQLite database file (`IMS_database`).
* **styles**: Contains CSS files for styling the JavaFX components.
