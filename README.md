# Prometheus Demo

A simple RESTful API application to perform CRUD operations using Spring Boot, Kotlin, Gradle, H2 database and Java 18

This project consists of APIs to perform CRUD operations for a single Entity (Employee)

---

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

---

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

---

### Prometheus

By default, spring-boot-actuator provides production-ready features for receiving, storing and configuring metrics for 
different type of monitoring systems (Prometheus, InfluxDB, etc). 
In [application.yml](https://github.com/KhanS0L0/PrometheusDemo/blob/main/src/main/resources/config/application.yml) file you can find simple configuration of our metrics.

```yaml
management:
  metrics:
    tags:
      application: ${spring.application.name}
  server:
    port: 8087                # individual port only for metrics
    servlet:
      context-path: /
  endpoints:
    web:
      exposure:
        include: "*"          # enabling all default system metrics
      base-path: /
      path-mapping:
        metrics: metrics_names
        prometheus: metrics   # individual endpoint for Prometheus metrics
  endpoint:
    health:
      probes:
        enabled: true
      show-details: always
```

After we will run our application spring-boot will produce default system metrics by itself

---

### Custom metrics

In this application we only use @Timed annotation which provide three different basic metrics: counter, gauge, summary.
```kotlin
    // gets all employees
    @Timed(
        value = "get.all.employees.metrics",
        description = "Time taken to return employee list",
    )
    @GetMapping("/employees")
    fun getAllEmployees() : ResponseEntity<List<Employee>> =
            ResponseEntity.status(HttpStatus.OK).body(employeeService.getAllEmployees())
```

To configure this annotation we should define "custom" bean that will enable AspectJ-proxy-mechanism and allow us to use it in places we want. 
Otherwise, we will face the problem like with @Transactional annotation (limitation of Spring AOP).
https://stackoverflow.com/questions/3423972/spring-transaction-method-call-by-the-method-within-the-same-class-does-not-wo

After we will invoke these methods we can find our metrics on http://localhost:8087/metrics

```
# HELP get_all_employees_metrics_seconds_max Time taken to return employee list
# TYPE get_all_employees_metrics_seconds_max gauge
get_all_employees_metrics_seconds_max{application="PrometheusDemoApplication",class="com.example.khansolo.web.EmployeeController",exception="none",method="getAllEmployees",} 0.0
# HELP get_all_employees_metrics_seconds Time taken to return employee list
# TYPE get_all_employees_metrics_seconds summary
get_all_employees_metrics_seconds_count{application="PrometheusDemoApplication",class="com.example.khansolo.web.EmployeeController",exception="none",method="getAllEmployees",} 4.0
get_all_employees_metrics_seconds_sum{application="PrometheusDemoApplication",class="com.example.khansolo.web.EmployeeController",exception="none",method="getAllEmployees",} 0.009091706
```

* Counter - (surprise-surprise motherf*cker) this metrics just count how many times method was invoked. Have "_count" - postfix
* Gauge - represent single numerical value. In our case method's time spent. Have "_max" - postfix. 
* Summary - samples observations (usually things like request durations and response sizes). Have "_sum" - postfix.

Out of the box, all these metrics are not so useful, but with Prometheus we can use an aggregation type functions and get statistics
for several days, this will allow us to "observe" our services/applications and prevent unpleasant incidents