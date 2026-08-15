# Finexa 💳

[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)](https://jwt.io/)

**Finexa** is a secure, backend-focused banking and fintech REST API application built with **Java** and **Spring Boot**, designed to demonstrate enterprise-grade backend architecture, transactional integrity, and clean code practices.

Finexa provides robust APIs for user authentication, account management, financial transactions with balance guard checks, and automated transactional email notifications.

---

## 📑 Table of Contents

- [✨ Features](#-features)
  - [Authentication & User Management](#authentication--user-management)
  - [Account Management](#account-management)
  - [Transaction Engine](#transaction-engine)
  - [Email Notification System](#email-notification-system)
- [🛠 Tech Stack](#-tech-stack)
- [🏛 Architecture](#-architecture)
- [📁 Project Structure](#-project-structure)
- [🔄 Core Workflows](#-core-workflows)
  - [1. Authentication Flow](#1-authentication-flow)
  - [2. Transaction Flow](#2-transaction-flow)
  - [3. Financial Precision](#3-financial-precision)
  - [4. DTO-Based API Design](#4-dto-based-api-design)
- [🔌 API Reference](#-api-reference)
- [🚀 Getting Started](#-getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation & Setup](#installation--setup)
  - [Environment Variables](#environment-variables)
  - [Running the Application](#running-the-application)
- [🧪 API Testing](#-api-testing)
- [🛡 Security](#-security)
- [🗺 Roadmap](#-roadmap)
- [👨‍💻 Author](#-author)
- [📄 License](#-license)

---

## ✨ Features

### Authentication & User Management
- 🔐 User registration and secure login
- 🎟️ JWT-based stateless authentication
- 🔒 Protected REST endpoints with Spring Security
- 🔑 Password management & self-service reset workflows
- 📬 Automated password update confirmation alerts
- ✅ Request validation via Jakarta Bean Validation
- 🚫 Centralized Global Exception Handling

### Account Management
- 🏦 Dynamic bank account creation & user mapping
- 💰 Real-time account balance tracking & currency support
- 📊 Account type & status lifecycle management
- 📜 Granular transaction history per account

### Transaction Engine
- 💵 Credit and debit processing
- 🛡️ **Balance Guard:** Automatic prevention of overdraft / insufficient balance
- 💰 Exact financial precision using Java `BigDecimal`
- 🕐 Auditable transaction timestamps & status tracking

### Email Notification System
Finexa integrates **Thymeleaf HTML templates** to deliver styled transactional emails for:
- 👋 Welcome onboarding
- 🏦 New account creation alerts
- 💵 Credit & 💸 Debit notifications
- 🔐 Password reset and security change confirmations

---

## 🛠 Tech Stack

| Layer | Technology | Purpose |
| :--- | :--- | :--- |
| **Language** | Java 17+ | Core programming language |
| **Framework** | Spring Boot | Enterprise application framework |
| **Web** | Spring Web | RESTful API development |
| **Security** | Spring Security + JWT | Authentication, authorization & token parsing |
| **Persistence** | Spring Data JPA | Data access abstraction |
| **ORM** | Hibernate | Object-relational mapping |
| **Database** | PostgreSQL | Relational database storage |
| **Validation** | Jakarta Bean Validation | DTO input validation |
| **Templates** | Thymeleaf | HTML email rendering |
| **Utilities** | Lombok | Boilerplate reduction |
| **Build Tool** | Maven | Dependency & build management |

---

## 🏛 Architecture

Finexa adheres to standard **Layered Architecture** principles, enforcing a strict separation of concerns:
Finexa adheres to standard **Layered Architecture** principles, enforcing a strict separation of concerns:

┌───────────────────────────────────────────────────────────┐
│                          Client                           │
│                 (Web / Mobile / Postman)                  │
└─────────────────────────────┬─────────────────────────────┘
│ REST / JSON
▼
┌───────────────────────────────────────────────────────────┐
│                     Controller Layer                      │
│               REST Endpoints + DTO Validation             │
└─────────────────────────────┬─────────────────────────────┘
│
▼
┌───────────────────────────────────────────────────────────┐
│                      Service Layer                        │
│                 Business Logic & Rules                    │
└───────────────┬───────────────────────────┬───────────────┘
│                           │
▼                           ▼
┌───────────────────────────┐   ┌───────────────────────────┐
│     Repository Layer      │   │    Supporting Services    │
│     (Spring Data JPA)     │   │     (Security / Mail)     │
└───────────────┬───────────┘   └───────────────────────────┘
│
▼
┌───────────────────────────┐
│        PostgreSQL         │
│         Database          │
└───────────────────────────┘


---

## 📁 Project Structure

```text
Finexa/
├── src/
│   └── main/
│       ├── java/com/finexa/finexa/
│       │   ├── account/
│       │   │   ├── controller/
│       │   │   ├── dtos/
│       │   │   ├── entity/
│       │   │   ├── repo/
│       │   │   └── services/
│       │   ├── auth_users/
│       │   │   ├── controller/
│       │   │   ├── dtos/
│       │   │   ├── entity/
│       │   │   ├── repo/
│       │   │   └── services/
│       │   ├── transaction/
│       │   │   ├── controller/
│       │   │   ├── dtos/
│       │   │   ├── entity/
│       │   │   ├── repo/
│       │   │   └── services/
│       │   ├── enums/
│       │   ├── exceptions/
│       │   └── res/
│       └── resources/
│           ├── templates/email/
│           │   ├── welcome.html
│           │   ├── account-created.html
│           │   ├── credit-alert.html
│           │   ├── debit-alert.html
│           │   ├── password-change.html
│           │   ├── password-reset.html
│           │   └── password-update-confirmation.html
│           └── application.properties
├── pom.xml
├── mvnw
└── README.md
🔄 Core Workflows
1. Authentication Flow
Plaintext
[ Client ] ──( Credentials )──▶ [ AuthController ] ──▶ [ UserService ] ──▶ [ Database ]
                                        │
[ Client ] ◀──( Returns JWT )───────────┘
    │
    └──( Authorization: Bearer <Token> )──▶ [ Protected Endpoints ]
2. Transaction Flow & Balance Guard
Plaintext
Client Request
      │
      ▼
Transaction Controller ──▶ Validate Request DTO
      │
      ▼
Transaction Service    ──▶ Fetch Account ──▶ Validate Balance (Debit check)
      │                                                │
      ├─────────────────────── Inefficient Balance? ───┴──▶ Throws InsufficientBalanceException
      │
      ├──▶ Update Balance
      ├──▶ Persist Transaction in PostgreSQL
      └──▶ Dispatch Async Email Notification
3. Financial Precision
Financial data avoids binary floating-point rounding issues by using BigDecimal:

Java
// Avoid double/float precision artifacts (e.g., 0.1 + 0.2 = 0.30000000000000004)
private BigDecimal amount;
4. DTO-Based API Design
JPA entities are strictly isolated from the presentation layer via Data Transfer Objects (DTOs) to ensure encapsulation and prevent unintended data exposure.

🔌 API Reference
🔐 Authentication
Method	Endpoint	Description	Auth Required
POST	/api/auth/register	Register a new user	❌
POST	/api/auth/login	Authenticate and obtain JWT	❌
POST	/api/auth/password-reset	Request password reset email	❌
🏦 Accounts
Method	Endpoint	Description	Auth Required
POST	/api/accounts	Create a new bank account	✅
GET	/api/accounts/{id}	Retrieve account details	✅
GET	/api/accounts/user/{userId}	Retrieve all accounts for a user	✅
💸 Transactions
Method	Endpoint	Description	Auth Required
POST	/api/transactions	Process credit or debit transaction	✅
GET	/api/transactions/{id}	Get transaction details	✅
GET	/api/transactions/account/{id}	Get account transaction history	✅
🚀 Getting Started
Prerequisites
Java 17+ (java -version)

Maven 3.8+ (mvn -version)

PostgreSQL 14+ (psql --version)

Git

Installation & Setup
Clone the repository:

Bash
git clone [https://github.com/Dexturn1/Finexa.git](https://github.com/Dexturn1/Finexa.git)
cd Finexa
Create the Database:

SQL
CREATE DATABASE finexa;
Environment Variables
Configure your sensitive credentials in src/main/resources/application.properties or set them as environment variables:

Properties
spring.datasource.url=jdbc:postgresql://localhost:5432/finexa
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD:postgres}

jwt.secret=${JWT_SECRET}

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}
⚠️ Warning: Never commit production credentials, secrets, or mail passwords directly to source control.

Running the Application
Using the Maven Wrapper:

Bash
# macOS/Linux
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
The application will start on http://localhost:8080.

🧪 API Testing
1. Register a User
HTTP
POST /api/auth/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "SecurePassword123"
}
2. Login & Retrieve JWT
HTTP
POST /api/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "SecurePassword123"
}
3. Access Protected Endpoint
HTTP
GET /api/accounts/1
Authorization: Bearer <YOUR_JWT_TOKEN>
🛡 Security
🔐 Stateless authentication with JWT & Spring Security

🛡️ Strict DTO validation to prevent mass-assignment vulnerabilities

🔑 BCrypt password hashing

🚫 Centralized error handling preventing stack-trace leaks

🔒 Environment-variable-based secret management

🗺 Roadmap
[ ] Swagger / OpenAPI 3 interactive documentation

[ ] Redis caching for account balances & sessions

[ ] Refresh token rotation mechanism

[ ] Idempotency-Key support for payment requests

[ ] Docker & Docker Compose containerization

[ ] GitHub Actions CI/CD pipeline

[ ] Unit & integration test suites (JUnit 5 + Testcontainers)

👨‍💻 Author
Prabhat Kapkoti

Java & Backend Developer

GitHub: @Dexturn1

📄 License
This project is open-source and intended for educational and portfolio purposes.
