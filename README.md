# FitCare — Fitness Goal & Roadmap App

A full-stack fitness tracking application
built with Java Spring Boot and Bootstrap.

## Tech Stack

**Backend**
- Java 24
- Spring Boot 4.0.6
- Spring Security + JWT
- Spring Data JPA
- MySQL Database

**Frontend**
- HTML5 + CSS3
- Bootstrap 5.3
- Vanilla JavaScript

## Features

- User Registration & Login with JWT Auth
- Fitness Goal Selection (7 goals)
- Personalized Roadmap with Phases
- BMI Calculator
- Progress Tracking
- Data Seeder for automatic DB population

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/auth/register | Register user |
| POST | /api/auth/login | Login + JWT |
| GET | /api/users/me | Get profile |
| GET | /api/users/bmi | BMI calculator |
| GET | /api/goals/all | All goals |
| POST | /api/goals/set | Set my goal |
| GET | /api/goals/my | My active goal |
| GET | /api/roadmap/my | My roadmap |

## Setup Instructions

### Prerequisites
- Java 24
- MySQL 8.0+
- Maven 3.8+

### Database Setup
```sql
CREATE DATABASE fitcaredb;
```

### Configuration
`src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=****
```

### Run
```bash
mvn spring-boot:run
```

App runs on `http://localhost:8081`

## Project Structure
src/main/java/com/fitcare/
├── controller/    # REST Controllers
├── service/       # Business Logic
├── repository/    # Data Access Layer
├── model/         # JPA Entities
├── dto/           # Data Transfer Objects
├── security/      # JWT Security
├── config/        # Configuration
└── exception/     # Global Exception Handler


## Author
Ranjeet Mahto — [LinkedIn](https://www.linkedin.com/in/ranjeet-mahto11)