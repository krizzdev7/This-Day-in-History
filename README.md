# This Day in History

A simple Java Swing application to explore historical facts.

## Project Overview

This application allows users to:
- Get a random historical fact for a selected date.
- List all facts for a chosen date.
- Add, edit, and delete facts.
- Search for facts by keyword and category.
- Import and export facts from/to a CSV file.
- Mark facts as favorites.

## MySQL Setup

1.  Install MySQL Server.
2.  Open a MySQL client and run the following command to create the database:
    ```sql
    CREATE DATABASE thisdayhistory CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
    ```
3.  Run the provided `schema.sql` script on the `thisdayhistory` database to create the `historical_facts` table and insert sample data.

## Configuration

1.  Open the `src/main/resources/config.properties` file.
2.  Update the database connection properties:
    - `db.host`: The host of your MySQL server (e.g., `localhost`).
    - `db.port`: The port of your MySQL server (e.g., `3306`).
    - `db.name`: The name of the database (e.g., `thisdayhistory`).
    - `db.user`: The username for your MySQL server (e.g., `root`).
    - `db.password`: The password for your MySQL server.

## How to Build and Run

### Using Maven-managed dependency

1.  Build the application using Maven:
    ```bash
    mvn clean package
    ```
2.  Run the application:
    ```bash
    java -jar target/thisdayhistory-1.0.0.jar
    ```

### Using local connector jar

1.  Place the `mysql-connector-j-X.X.X.jar` file in the `lib` directory.
2.  Build the application using Maven:
    ```bash
    mvn clean package
    ```
3.  Run the application:
    ```bash
    java -cp target/thisdayhistory-1.0.0.jar;lib/mysql-connector-j-X.X.X.jar com.thisdayhistory.MainApp
    ```

## Troubleshooting

- **ClassNotFoundException: com.mysql.cj.jdbc.Driver**: Make sure the MySQL connector jar is in the classpath.
- **Access denied for user 'root'@'localhost'**: Check your MySQL username and password in `config.properties`.
- **Port in use**: Make sure no other application is using the same port as the MySQL server.
