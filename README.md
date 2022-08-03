# Prometheus Demo

A simple RESTful API application to perform CRUD operations using Spring Boot, Kotlin, Gradle, H2 database and Java 18

This project consists of APIs to perform CRUD operations for a single Entity (Employee)

### List of APIs

**Create	- POST**
* http://localhost:8080/employees

**Read	- GET**
* http://localhost:8080/employees
* http://localhost:8080/employees/{id}

**Update	- PUT**
* http://localhost:8080/employees/{id}

**Delete	- DELETE**
* http://localhost:8080/employees/{id}


This project was created using [https://start.spring.io/](https://start.spring.io/) as a start with following options
* Project – Gradle Project
* Language – Kotlin
* Spring Boot - 2.4.2
* Packaging – war
* Java – 11
* Dependencies
    -	Spring Web
    -	Spring Data JPA
    -   Spring AOP
    -   Spring Actuator
    -   Micrometer Registry Prometheus
    -	H2 Database
    -	OpenFeign

### Features
* Application runs on Undertow web server
* REST controller named EmployeeController that exposes CRUD operations for Employee Entity.  APIs mentioned in above table are available
* Service layer named EmployeeService that performs data validation on the following fields
    -	Firstname – checks if it has only alphabets
    -	lastname – checks if it has only alphabets
    -	email – checks if it is a valid email address
* Repository named EmployeeRepository has a query method (findOneById) which retrieves one employee based on id
* Consume a public RESTful API [https://mailboxlayer.com](https://mailboxlayer.com) using Spring Cloud OpenFeign to validate email address 
* Application provide default metrics for Prometheus
* REST controller provide @Timed - metrics for Prometheus (Counter, Gauge, Summary)