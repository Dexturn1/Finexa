Finexa

Modern Banking & Fintech Backend built with Spring Boot

Finexa is a backend-focused banking and fintech application built with Java and Spring Boot. It provides a secure REST API for user authentication, bank account management, financial transactions, and automated email notifications.

The project is designed with a layered architecture and follows common backend development practices such as DTO-based API communication, service/repository separation, validation, centralized exception handling, JWT-based authentication, and transactional database operations.

⸻

Features

Authentication & User Management

* User registration
* Secure user authentication
* JWT-based authentication
* Password management
* Password reset workflow
* Password update confirmation
* Email-based user notifications
* Request validation
* Secure access to protected APIs

Account Management

* Create bank accounts
* Retrieve account information
* Account status management
* Account type management
* Currency support
* Account balance management
* User-account relationship management
* Account transaction history

Transactions

Finexa supports core banking transaction workflows.

* Credit transactions
* Debit transactions
* Transaction status management
* Transaction type management
* Balance validation
* Insufficient balance protection
* Transaction timestamps
* Transaction history
* Secure account-to-account transaction processing

Financial operations are handled using BigDecimal to avoid the precision problems associated with floating-point arithmetic.

Email Notifications

Finexa provides automated email notifications for important account and security events.

Available email templates include:

* Welcome email
* Account creation notification
* Credit alert
* Debit alert
* Password change notification
* Password reset notification
* Password update confirmation

The email templates are built using Thymeleaf and are designed to provide consistent transactional communication.

⸻

Tech Stack

Technology	Purpose
Java	Backend programming language
Spring Boot	Application framework
Spring Web	REST API development
Spring Data JPA	Database persistence
Hibernate	ORM
Spring Security	Application security
JWT	Authentication & authorization
PostgreSQL	Relational database
Thymeleaf	Email templates
Jakarta Validation	Request validation
Lombok	Boilerplate reduction
Maven	Dependency management
REST API	Client-server communication

⸻

Architecture

Finexa follows a layered backend architecture:

                    ┌──────────────────────┐
                    │       Client         │
                    │ Web / Mobile / API   │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │      Controller      │
                    │     REST Endpoints   │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │       Service        │
                    │ Business Logic       │
                    └──────────┬───────────┘
                               │
                  ┌────────────┴────────────┐
                  ▼                         ▼
        ┌──────────────────┐      ┌──────────────────┐
        │   Repository     │      │ External Services│
        │   Data Access    │      │ Email / Security │
        └────────┬─────────┘      └──────────────────┘
                 │
                 ▼
        ┌──────────────────┐
        │    PostgreSQL    │
        │     Database     │
        └──────────────────┘

The application separates responsibilities between controllers, services, repositories, entities, DTOs, and supporting components.

⸻

Project Structure

The project is organized by domain rather than putting everything into one giant folder, because apparently software becomes easier to maintain when every class isn’t dumped into a single package.

src/
└── main/
    ├── java/
    │   └── com/
    │       └── finexa/
    │           └── finexa/
    │               ├── account/
    │               │   ├── controller/
    │               │   ├── dtos/
    │               │   ├── entity/
    │               │   ├── repo/
    │               │   └── services/
    │               │
    │               ├── auth_users/
    │               │   ├── controller/
    │               │   ├── dtos/
    │               │   ├── entity/
    │               │   ├── repo/
    │               │   └── services/
    │               │
    │               ├── transaction/
    │               │   ├── controller/
    │               │   ├── dtos/
    │               │   ├── entity/
    │               │   ├── repo/
    │               │   └── services/
    │               │
    │               ├── enums/
    │               ├── exceptions/
    │               ├── res/
    │               └── ...
    │
    └── resources/
        ├── templates/
        │   └── email/
        │       ├── welcome.html
        │       ├── account-created.html
        │       ├── credit-alert.html
        │       ├── debit-alert.html
        │       ├── password-change.html
        │       ├── password-reset.html
        │       └── password-update-confirmation.html
        │
        └── application.properties

⸻

Authentication Flow

Finexa uses JWT-based authentication to secure protected endpoints.

User
 │
 │ Register
 ▼
Auth Controller
 │
 ▼
User Service
 │
 ▼
User Repository
 │
 ▼
Database
User
 │
 │ Login
 ▼
Authentication
 │
 ▼
Credentials Validation
 │
 ▼
JWT Generation
 │
 ▼
Client
 │
 │ Authorization: Bearer <token>
 ▼
JWT Authentication Filter
 │
 ▼
Protected Controller

The JWT allows authenticated users to access protected resources without relying on server-side session storage.

⸻

Transaction Flow

A typical transaction follows this flow:

Client
   │
   ▼
Transaction Controller
   │
   ▼
Transaction Service
   │
   ├── Validate Request
   │
   ├── Find Account
   │
   ├── Check Account Status
   │
   ├── Validate Balance
   │
   ├── Update Account Balance
   │
   ├── Create Transaction
   │
   ├── Persist Transaction
   │
   └── Send Notification
   │
   ▼
PostgreSQL

For debit transactions, Finexa validates that the account has sufficient funds before processing the transaction.

This prevents invalid operations such as:

Available Balance: ₹5,000
Debit Request:     ₹8,000
Result:
InsufficientBalanceException

⸻

Financial Precision

Financial values are represented using Java’s BigDecimal instead of double or float.

private BigDecimal amount;

This is important for financial applications because floating-point arithmetic can introduce precision errors.

For example, using floating-point numbers for money can produce results such as:

0.1 + 0.2 = 0.30000000000000004

Banking software is generally not the place to let floating-point mathematics freestyle.

⸻

Transaction Types & Status

Finexa uses enums to represent transaction-related states.

TransactionType

Examples can include:

CREDIT
DEBIT

Transactions also maintain a status through:

TransactionStatus

This provides a controlled set of valid transaction states rather than relying on arbitrary strings.

⸻

DTO-Based API Design

Finexa uses Data Transfer Objects to separate API request/response models from persistence entities.

Example:

Client
   │
   ▼
Request DTO
   │
   ▼
Controller
   │
   ▼
Service
   │
   ▼
Entity
   │
   ▼
Repository
   │
   ▼
Database

This helps prevent exposing JPA entities directly through the API and provides a cleaner boundary between the API and persistence layers.

⸻

Validation & Error Handling

Finexa uses request validation to prevent invalid data from entering the application.

Examples include:

* Email format validation
* Required field validation
* Input validation
* Transaction amount validation
* Authentication validation
* Account validation
* Balance validation

Custom exceptions are used for domain-specific failures, including insufficient account balance.

A centralized response structure is also used to provide consistent API responses.

Example:

{
  "message": "Insufficient balance"
}

⸻

Email Notification System

Finexa integrates transactional email notifications for important events.

Account Events

Account Created
      │
      ▼
Email Service
      │
      ▼
account-created.html
      │
      ▼
User Email

Transaction Events

Credit Transaction
       │
       ▼
Credit Alert Email
Debit Transaction
       │
       ▼
Debit Alert Email

The email templates use Thymeleaf for dynamic content such as:

* User name
* Account information
* Transaction amount
* Transaction type
* Transaction date
* Transaction status
* Account balance

⸻

Database

Finexa uses a relational database to maintain structured banking data.

Core domain relationships include:

User
 │
 └── Account
       │
       └── Transaction

A user can have accounts, and accounts maintain their associated transaction records.

JPA/Hibernate is used to map Java entities to relational database tables.

⸻

API Endpoints

The API is organized around major application domains.

Authentication

POST   /api/auth/register
POST   /api/auth/login
...

Accounts

POST   /api/accounts
GET    /api/accounts/{id}
...

Transactions

POST   /api/transactions
GET    /api/transactions/{id}
...

Endpoint paths may vary depending on the current controller mappings in the project.

⸻

Getting Started

Prerequisites

Make sure the following are installed:

* Java 17+
* Maven
* PostgreSQL
* Git

Verify Java:

java -version

Verify Maven:

mvn -version

⸻

Clone the Repository

git clone https://github.com/your-username/finexa.git
cd finexa

⸻

Database Configuration

Create a PostgreSQL database:

CREATE DATABASE finexa;

Configure your database connection in:

src/main/resources/application.properties

Example:

spring.datasource.url=jdbc:postgresql://localhost:5432/finexa
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

Never commit real database credentials, JWT secrets, or email credentials to GitHub.

Use environment variables or a secrets management solution for production deployments.

⸻

Environment Variables

Sensitive configuration should be provided through environment variables.

Example:

DB_USERNAME
DB_PASSWORD
JWT_SECRET
MAIL_USERNAME
MAIL_PASSWORD

Configure these according to your local environment.

⸻

Running the Application

Using Maven:

./mvnw spring-boot:run

Or:

mvn spring-boot:run

The application will start on the configured Spring Boot port.

By default:

http://localhost:8080

⸻

API Testing

You can test the API using tools such as:

* Postman
* Insomnia
* cURL
* Swagger/OpenAPI, if configured

Example registration request:

POST /api/auth/register
Content-Type: application/json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "SecurePassword123"
}

Example login request:

POST /api/auth/login
Content-Type: application/json
{
  "email": "john@example.com",
  "password": "SecurePassword123"
}

After successful authentication, the returned JWT can be used to access protected endpoints.

Authorization: Bearer <JWT_TOKEN>

⸻

Security Considerations

Finexa applies several backend security practices:

* JWT-based authentication
* Protected API endpoints
* Password handling through Spring Security mechanisms
* Input validation
* Authentication checks
* Authorization checks
* Secure exception handling
* Environment-based secret configuration

For a production banking system, additional controls would be required, including stronger auditing, rate limiting, fraud detection, encryption strategies, secure secret management, monitoring, and comprehensive security testing.

This project focuses on demonstrating backend engineering and fintech application architecture rather than claiming to be an actual production banking platform.

⸻

Future Improvements

Planned improvements include:

* Swagger/OpenAPI documentation
* Role-based authorization
* Refresh token mechanism
* Redis caching
* Docker containerization
* CI/CD pipeline
* AWS deployment
* Transaction idempotency
* Audit logging
* Rate limiting
* Improved test coverage
* Integration testing
* Docker Compose for local development
* Kubernetes deployment
* Monitoring and observability
* Frontend dashboard

⸻

What I Learned

Building Finexa helped strengthen practical backend development skills including:

* Designing REST APIs
* Spring Boot application architecture
* Spring Security and JWT authentication
* Database relationships with JPA/Hibernate
* DTO-based API design
* Service and repository patterns
* Financial transaction processing
* Exception handling
* Request validation
* Email notification systems
* PostgreSQL integration
* Secure configuration management
* Writing maintainable backend code

⸻

Project Goals

The primary goal of Finexa is to build a realistic backend system that demonstrates how a banking application can be designed using modern Java and Spring Boot technologies.

The project focuses on:

Security
   +
Clean Architecture
   +
Database Design
   +
Financial Transactions
   +
REST APIs
   +
Real-world Backend Engineering

⸻

Author

Prabhat Kapkoti

Java Backend Developer

Focused on:

Java
Spring Boot
REST APIs
PostgreSQL
Spring Security
Docker
AWS
Backend Engineering

⸻

License

This project is intended for educational and portfolio purposes.
