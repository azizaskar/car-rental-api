# Car Rental Management System

## Project Overview

This is a **Car Rental Management API** built with Java, demonstrating Object-Oriented Programming principles and JDBC database connectivity. The system manages cars, customers, and rental transactions through a command-line interface.

**Entities:**
- **Car** - rental vehicles with pricing and availability status
- **Customer** - clients with driver license information
- **Rental** - represents a car rental transaction (composition of Car + Customer)

---

## OOP Design Features

### 1. Abstraction
- **BaseEntity** - abstract class containing common fields (`id`, `name`) and abstract methods (`getDetails()`, `getType()`)
- All entities extend BaseEntity and implement abstract methods

### 2. Inheritance & Polymorphism
- `Car extends BaseEntity`
- `Customer extends BaseEntity`
- Polymorphic behavior: `describe()` method calls overridden `getType()` and `getDetails()`

### 3. Interfaces
- **Validatable** - enforces validation rules
  - Implemented by: `Car`, `Customer`, `Rental`
- **PricedItem** - defines pricing calculation contract
  - Implemented by: `Rental` (calculates total price based on days and car's daily rate)

### 4. Composition & Aggregation
- **Rental** class contains references to `Car` and `Customer` objects
- Demonstrates "has-a" relationship

### 5. Encapsulation
- All fields are private with getters/setters
- Validation logic in setters (e.g., `dailyPrice > 0`, `endDate >= startDate`)

---

## Architecture (Layered Design)

```
controller/       CLI interface (Main.java)
    ↓
service/          Business logic + validation
    ↓
repository/       CRUD operations (JDBC)
    ↓
PostgreSQL DB     Data persistence
```

**Separation of Concerns:**
- `Main` → user interaction only
- `Service` → business rules (e.g., only AVAILABLE cars can be rented)
- `Repository` → SQL queries using PreparedStatement (no raw SQL injection risk)

---

## Database Schema

**Tables:**

### `customer`
| Column         | Type         | Constraints       |
|----------------|--------------|-------------------|
| id             | SERIAL       | PRIMARY KEY       |
| name           | VARCHAR(100) | NOT NULL          |
| driver_license | VARCHAR(50)  | NOT NULL, UNIQUE  |
| phone          | VARCHAR(30)  |                   |

### `car`
| Column        | Type         | Constraints            |
|---------------|--------------|------------------------|
| id            | SERIAL       | PRIMARY KEY            |
| name          | VARCHAR(100) | NOT NULL               |
| license_plate | VARCHAR(20)  | NOT NULL, UNIQUE       |
| daily_price   | NUMERIC(10,2)| NOT NULL, CHECK > 0    |
| status        | VARCHAR(20)  | NOT NULL               |

### `rental`
| Column      | Type         | Constraints          |
|-------------|--------------|----------------------|
| id          | SERIAL       | PRIMARY KEY          |
| car_id      | INTEGER      | FK → car(id)         |
| customer_id | INTEGER      | FK → customer(id)    |
| start_date  | DATE         | NOT NULL             |
| end_date    | DATE         | NOT NULL             |
| total_price | NUMERIC(10,2)| NOT NULL, CHECK >= 0 |

**Relationships:**
- `rental.car_id` → `car.id` (Many-to-One)
- `rental.customer_id` → `customer.id` (Many-to-One)

---

## Custom Exceptions

1. **InvalidInputException** - thrown when input validation fails (e.g., empty name, negative price)
2. **DuplicateResourceException** - thrown when trying to create duplicate records (e.g., same license plate)
3. **ResourceNotFoundException** - thrown when queried entity doesn't exist (e.g., car with id=999)
4. **DatabaseOperationException** - wraps SQL exceptions during CRUD operations

---

## Technologies Used

- **Language:** Java 11+
- **Database:** PostgreSQL 16
- **JDBC Driver:** `org.postgresql:postgresql`
- **IDE:** IntelliJ IDEA
- **Database Tool:** pgAdmin 4

---

## Setup Instructions

### 1. Database Setup

Create PostgreSQL database:
```sql
CREATE DATABASE car_rental;
```

Run `resources/schema.sql` in pgAdmin Query Tool to create tables and insert sample data.

### 2. Configure Database Connection

Edit `src/utils/DatabaseConnection.java`:
```java
private static final String URL = "jdbc:postgresql://localhost:5432/car_rental";
private static final String USER = "postgres";
private static final String PASSWORD = "your_password";
```

### 3. Run the Application

1. Open project in IntelliJ IDEA
2. Build project (ensure PostgreSQL JDBC driver is in classpath)
3. Run `controller/Main.java`

---

## CLI Menu Options

```
1. List cars          - Display all cars with status
2. Add car            - Create new car (requires: name, plate, price, status)
3. List customers     - Show all registered customers
4. Add customer       - Register new customer (requires: name, driver license, phone)
5. List rentals       - Display all rental transactions
6. Create rental      - Rent a car (requires: carId, customerId, dates)
0. Exit               - Close application
```

---

## Sample Usage

**Adding a new car:**
```
Choose: 2
Car name: BMW X5
License plate: BMW-123
Daily price: 200
Status: AVAILABLE
```

**Creating a rental:**
```
Choose: 6
Car id: 1
Customer id: 2
Start date: 2026-01-20
End date: 2026-01-23
→ Total price calculated: 3 days × $50 = $150
```

---

## Project Structure

```
car-rental-api/
├── src/
│   ├── controller/
│   │   └── Main.java
│   ├── service/
│   │   ├── CarService.java
│   │   ├── CustomerService.java
│   │   └── RentalService.java
│   ├── repository/
│   │   ├── CarRepository.java
│   │   ├── CustomerRepository.java
│   │   └── RentalRepository.java
│   ├── model/
│   │   ├── BaseEntity.java
│   │   ├── Car.java
│   │   ├── Customer.java
│   │   ├── Rental.java
│   │   ├── Validatable.java
│   │   └── PricedItem.java
│   ├── exception/
│   │   ├── InvalidInputException.java
│   │   ├── DuplicateResourceException.java
│   │   ├── ResourceNotFoundException.java
│   │   └── DatabaseOperationException.java
│   └── utils/
│       └── DatabaseConnection.java
└── resources/
    └── schema.sql
```

---

## Author

**Student:** Askar Aziz  
**Course:** Object-Oriented Programming (Java)  
**Assignment:** Assignment 3 - Car Rental API  
**Date:** January 2026

---

## License

This project is for educational purposes only.