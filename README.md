# Drug Store Inventory Management System v2.0

A comprehensive desktop application for managing drug store inventory, built with Java Swing and MySQL database.

## Features

- **User Authentication**: Secure login system with role-based access (Manager/Staff)
- **Product Management**: Add, edit, delete, and view products with categories
- **Category Management**: Organize products into categories
- **Dashboard**: Overview of inventory statistics and analytics
- **Report Generation**: Dashboard-driven reports with category/vendor filtering and improved back navigation
- **Data Exchange**: Import/export product data in CSV and JSON formats, with preset CSV export filename and file extension selection
- **Inventory Tracking**: Monitor stock levels, prices, and product details

## Prerequisites

Before setting up the system, ensure you have the following installed:

- **Java Development Kit (JDK) 18** or higher
- **Apache NetBeans IDE 27**
- **MySQL Server 8.0** or **MariaDB 10.4** or higher
- **MySQL Connector/J 9.5.0** (included in project dependencies)

## Database Setup

1. Install and start MySQL Server
2. Create a new database named `drugstoreinventorydb`
3. Import the database schema and initial data:
   - Open MySQL command line or phpMyAdmin
   - Run the SQL script located at `database/init.sql`

**Default Database Configuration:**
- Host: localhost
- Port: 3306
- Database: drugstoreinventorydb
- Username: root
- Password: (empty)

> **Note:** If you need to change database credentials, modify the `DBManager.java` file in the `src/infra/` directory.

## Project Setup in NetBeans

1. **Download and Install NetBeans 27**
   - Download from the official Apache NetBeans website
   - Install JDK 18 if not already installed
   - Install NetBeans IDE

2. **Open the Project**
   - Launch NetBeans IDE
   - Go to `File > Open Project`
   - Navigate to the project directory (`SDI_DrugStore_InventoryManagementSystem_1.3`)
   - Select the project and click `Open Project`

3. **Configure MySQL Connector**
   - The project includes MySQL Connector/J reference
   - If the JAR file is missing, download `mysql-connector-j-9.5.0.jar`
   - Add it to the project libraries:
     - Right-click on project > `Properties`
     - Go to `Libraries` > `Compile` tab
     - Click `Add JAR/Folder` and select the MySQL connector JAR

4. **Build the Project**
   - Right-click on the project in Projects window
   - Select `Build` or press `F11`
   - Ensure the build completes without errors

## Running the Application

1. **Start MySQL Server**
   - Ensure MySQL service is running

2. **Run from NetBeans**
   - Right-click on the project
   - Select `Run` or press `F6`
   - The application will start with the Login screen

3. **Run from VS Code**
   - Install the Java Extension Pack or Java support extensions
   - Open the project folder in VS Code
   - Ensure the workspace JDK is set to JDK 18 or higher
   - Use the Run view or the `Run Java` action on `src/app/App.java`
   - Alternatively, run from the integrated terminal:
     - `cd "<project-root>"`
     - `javac -d build\classes src\app\App.java` (or use your IDE build task)
     - `java -cp build\classes app.App`
   - Or use Ant from the VS Code terminal:
     - `ant clean` to clean generated files
     - `ant build` to compile the project
     - `ant run` to start the application
   - The app launches with the Login screen

4. **Alternative: Run JAR file**
   - After building, find the JAR in `dist/` directory
   - Run with: `java -jar SDI_DrugStore_Application_1.3.jar`

## Usage Guide

### Login
- **Default Manager Account:**
  - Username: `manager`
  - Password: `Manager123`
- **Default Staff Account:**
  - Username: `staff`
  - Password: `Staff123`

### Navigation
- **Dashboard**: View inventory statistics and overview
- **Products**: Manage product inventory
- **Categories**: Manage product categories
- **Data Exchange**: Import/export data in CSV formats

### Product Management
- Add new products with name, description, price, stock, category
- Edit existing product information
- Delete products (with confirmation)
- Search and filter products

### Category Management
- Create new product categories
- Edit category names and descriptions
- Delete categories (only if no products are assigned)

### Data Exchange
- **Export**: Export product data to CSV files, with a predefined `product_inventory.csv` filename and selected CSV extension
- **Import**: Import product data from CSV files
- Supports bulk operations for inventory management

## Project Structure

```
src/
├── adapter/          # Data import/export adapters
├── app/             # Main application entry point
├── controller/      # MVC controllers
├── dto/             # Data transfer objects
├── exception/       # Custom exceptions
├── infra/           # Infrastructure (DB, utilities)
├── model/           # Data models
├── repository/      # Data access layer
├── service/         # Business logic layer
├── state/           # Application state management
├── validation/      # Input validation
└── view/            # Swing GUI components
```

## Architecture

The application follows the **Model-View-Controller (MVC)** pattern with additional layers:

- **View Layer**: Swing GUI components with .form files for NetBeans GUI Builder
- **Controller Layer**: Handles user interactions and coordinates between view and service
- **Service Layer**: Contains business logic
- **Repository Layer**: Data access abstraction
- **Model Layer**: Domain objects (Product, Category, User)
- **Infrastructure Layer**: Database connection and utilities

## Troubleshooting

### Common Issues

1. **Database Connection Error**
   - Verify MySQL server is running
   - Check database credentials in `DBManager.java`
   - Ensure database `drugstoreinventorydb` exists

2. **Build Errors**
   - Ensure JDK 18+ is installed and configured in NetBeans
   - Verify MySQL Connector/J is properly added to classpath
   - Clean and rebuild the project

3. **GUI Display Issues**
   - Ensure system has proper Java Swing support
   - Check display scaling settings

### Logs and Debugging
- Check NetBeans output window for runtime errors
- Database connection issues will be logged to console
- Use NetBeans debugger for step-through debugging

## Development

### Adding New Features
1. Follow MVC pattern
2. Add models in `model/` package
3. Create repository interfaces and implementations
4. Implement service layer logic
5. Create controllers for user interactions
6. Design views using NetBeans GUI Builder

### Code Style
- Follow Java naming conventions
- Use meaningful variable and method names
- Add JavaDoc comments for public methods
- Handle exceptions appropriately

## License

This project is developed for educational purposes.

## Support

For technical support or questions about the system:
- Check the troubleshooting section above
- Review the source code comments
- Ensure all prerequisites are properly installed

---

**Version:** 2.0
**Last Updated:** July 3, 2026
**Developed with:** Apache NetBeans IDE 27, Java 18, MySQL</content>