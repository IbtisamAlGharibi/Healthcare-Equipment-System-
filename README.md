# Healthcare Laboratory Equipment Reservation System

A full-stack web application for managing laboratory equipment reservations, maintenance, and laboratory resources in healthcare institutions.

---

## Project Overview

The **Healthcare Laboratory Equipment Reservation System** provides a centralized platform for hospitals and healthcare laboratories to manage specialized medical equipment efficiently.

The system enables administrators, laboratory staff, and maintenance technicians to coordinate equipment reservations, maintenance schedules, and laboratory resources while preventing scheduling conflicts and improving equipment utilization.

---

## Features

### Administrator
- Manage laboratories
- Manage laboratory equipment
- Manage maintenance technicians
- Approve or reject reservation requests
- View reports and statistics
- Track equipment status

### Laboratory Staff
- View available equipment
- Create reservation requests
- Cancel reservations
- View reservation history

### Maintenance Technician
- View maintenance requests
- Update maintenance progress
- Mark maintenance as completed
- Track assigned maintenance jobs

---

# System Architecture

The project follows a layered Spring Boot architecture.

```
Client (HTML/CSS/JavaScript)
            │
            ▼
     REST Controllers
            │
            ▼
      Service Layer
(Business Logic & Validation)
            │
            ▼
      Repository Layer
      (Spring Data JPA)
            │
            ▼
          MySQL
```

## Backend Technologies

- Java
- Spring Boot
- Spring Security
- JWT Authentication
- Spring Data JPA
- Hibernate
- MySQL
- Maven

## Frontend Technologies

- HTML5
- CSS3
- JavaScript (ES6)

## Development Tools

- IntelliJ IDEA
- VS Code
- MySQL Workbench
- Docker
- Git
- GitHub
- Postman

---

# Project Structure

```
src
 ├── controller
 ├── service
 ├── repository
 ├── entity
 ├── dto
 ├── security
 ├── exception
 ├── configuration
 └── utility
```

---

# Business Rules

- Equipment cannot be reserved if it is:
  - Reserved
  - Under Maintenance

- Reservation requests require administrator approval.

- Equipment under maintenance cannot be reserved.

- Maintenance technicians can only manage assigned maintenance tasks.

- Reservation history is maintained for auditing purposes.

---

# Database

The application uses **MySQL** for persistent storage.

Main entities include:

- Users
- Laboratory
- Equipment
- Reservation
- Maintenance
- Maintenance Technician

---

# Environment Variables

Create an `application.properties` (or use environment variables) with the following configuration:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/HealthcareEquipmentSystem
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.secret=YOUR_SECRET_KEY
jwt.expiration=86400000
```

---

# Setup Instructions

## Prerequisites

- Java 17+
- Maven
- MySQL
- Git
- Docker (Optional)

---

## 1. Clone the Repository

```bash
git clone https://github.com/<your-username>/HealthcareEquipmentSystem.git
```

---

## 2. Navigate to the Project

```bash
cd HealthcareEquipmentSystem
```

---

## 3. Create the Database

```sql
CREATE DATABASE HealthcareEquipmentSystem;
```

---

## 4. Configure Database

Update the database credentials inside:

```
src/main/resources/application.properties
```

---

## 5. Build the Project

```bash
mvn clean install
```

---

## 6. Run the Application

```bash
mvn spring-boot:run
```

or

```bash
java -jar target/HealthcareEquipmentSystem.jar
```

---

## 7. Access the Application

Backend API

```
http://localhost:8080
```

Frontend

Open:

```
index.html
```

---

# API Endpoints

## Authentication

| Method | Endpoint |
|----------|---------------------------|
| POST | `/auth/login` |
| POST | `/auth/register` |

---

## Laboratories

| Method | Endpoint |
|----------|--------------------------|
| GET | `/laboratories` |
| GET | `/laboratories/{id}` |
| POST | `/laboratories` |
| PUT | `/laboratories/{id}` |
| DELETE | `/laboratories/{id}` |

---

## Equipment

| Method | Endpoint |
|----------|-----------------------|
| GET | `/equipment` |
| GET | `/equipment/{id}` |
| POST | `/equipment` |
| PUT | `/equipment/{id}` |
| DELETE | `/equipment/{id}` |

---

## Reservations

| Method | Endpoint |
|----------|-----------------------------|
| GET | `/reservations` |
| GET | `/reservations/{id}` |
| POST | `/reservations` |
| PUT | `/reservations/{id}` |
| DELETE | `/reservations/{id}` |
| PUT | `/reservations/{id}/approve` |
| PUT | `/reservations/{id}/cancel` |

---

## Maintenance

| Method | Endpoint |
|----------|----------------------------|
| GET | `/maintenance` |
| GET | `/maintenance/{id}` |
| POST | `/maintenance` |
| PUT | `/maintenance/{id}` |
| PUT | `/maintenance/{id}/complete` |

---

## Technicians

| Method | Endpoint |
|----------|----------------------------|
| GET | `/technicians` |
| GET | `/technicians/{id}` |
| POST | `/technicians` |
| PUT | `/technicians/{id}` |
| DELETE | `/technicians/{id}` |

---

## Reports

| Method | Endpoint |
|----------|--------------------------------------------|
| GET | `/reports/available-equipment` |
| GET | `/reports/reserved-equipment` |
| GET | `/reports/maintenance-equipment` |
| GET | `/reports/equipment-per-laboratory` |
| GET | `/reports/reservations-per-laboratory` |
| GET | `/reports/staff-most-reservations` |
| GET | `/reports/equipment-repaired-this-month` |
| GET | `/reports/top-technician` |

---

# Security

- JWT Authentication
- Spring Security
- Role-based Authorization
- Password Encryption using BCrypt

---

# Future Improvements

- Email notifications
- Equipment QR Code integration
- Calendar scheduling
- Dashboard analytics
- Audit logging
- File upload for maintenance reports

---

# Team Members

| Name 
|------
| **Wejdan Salim Al Subhi** 
| Ibtisam Hamdan Mohammed Al Gharibi 
| Maathier Ahmed Mahfoodh Al Nabhani 

---

# License

This project was developed as part of a full-stack software engineering academic project.
