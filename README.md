
# Money Transfer Application

## Overview
The Money Transfer Application is a robust solution for managing accounts and transferring funds between them with currency exchange capabilities. It leverages Spring Boot, Spring Data JPA, and various other technologies to provide a scalable system.

## Features
- **Account Management**: Create and manage user accounts with different currencies.
- **Fund Transfer**: Transfer funds between accounts, with automatic currency conversion.
- **Concurrency Handling**: Supports concurrent transfers with pessimistic locking to ensure data integrity.
- **Exception Handling**: Comprehensive error handling for various exceptions like insufficient funds, account not found, and unsupported currencies.
- **Caching**: Uses Caffeine cache for caching exchange rates to improve performance.
- **Swagger API Documentation**: Interactive API documentation and testing using Swagger.
- **Audit Logging**: Hibernate Envers integration for maintaining an audit trail of account changes.

## Technologies Used
- **Java 17**
- **Spring Boot 3.3.1**
- **Spring Data JPA**
- **Hibernate Envers**
- **Caffeine Cache**
- **MapStruct**
- **H2 Database**
- **JUnit 5**
- **Mockito**
- **Jacoco for Test Coverage**
- **JMeter for Performance Testing**
- **Swagger for API Documentation**

## Getting Started

### Prerequisites
- Java 17
- Maven or Gradle

### Installation
1. **Clone the repository**:
   ```bash
   git clone https://github.com/NinJavaa/moneytransfer.git
   cd moneytransfer
   ```

2. **Switch to the `dev` branch**:
   ```bash
   git checkout dev
   ```

3. **Build the project**:
   ```bash
   ./gradlew build
   ```

4. **Run the application**:
   ```bash
   ./gradlew bootRun
   ```

### Configuration
The application can be configured using properties files. Here is an example configuration in `application-dev.properties`:
```properties
spring.profiles.active=dev
spring.datasource.url=jdbc:h2:mem:devdb;DB_CLOSE_DELAY=-1
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=create-drop
spring.h2.console.enabled=true

exchange.rate.api.url=https://v6.exchangerate-api.com/v6/{apiKey}/pair/{fromCurrency}/{toCurrency}
default.exchange.rate.api.key=your_api_key

account.initial.accounts[0].ownerId=1001
account.initial.accounts[0].currency=USD
account.initial.accounts[0].balance=1000.00
# ... add more initial accounts as needed
```

## Usage

### API Endpoints
- **Create Account**:
  - **Endpoint**: `POST /api/v1/accounts/create`
  - **Description**: Creates a new account.
  - **Request Body**:
    ```json
    {
      "ownerId": 1001,
      "currency": "USD",
      "initialBalance": 1000.00
    }
    ```

- **Transfer Funds**:
  - **Endpoint**: `POST /api/v1/accounts/transfer`
  - **Description**: Transfers funds between accounts.
  - **Request Parameters**:
    - `fromOwnerId`: The owner ID of the sender account.
    - `toOwnerId`: The owner ID of the recipient account.
    - `amount`: The amount to transfer.

- **Get Balance**:
  - **Endpoint**: `GET /api/v1/accounts/balance`
  - **Description**: Retrieves the balance of an account.
  - **Request Parameters**:
    - `ownerId`: The owner ID of the account.

### Running Tests
- **Unit Tests**:
  ```bash
  ./gradlew test
  ```

- **Generate Test Coverage Report**:
  ```bash
  ./gradlew jacocoTestReport
  ```

- **Verify Test Coverage**:
  ```bash
  ./gradlew jacocoTestCoverageVerification
  ```

### Performance Testing with JMeter
1. **Open JMeter** and create a new Test Plan.
2. **Add a Thread Group** with the desired number of users and ramp-up period.
3. **Add HTTP Requests** for the `transferFunds` and `getBalance` endpoints.
4. **Add User Defined Variables** to specify different account IDs and amounts.
5. **Add a Random Timer** to simulate real-world usage patterns.
6. **Add Assertions** to verify the expected response content.
7. **Add Listeners** to monitor and analyze the test results.

#### Example JMeter Configuration
- **Thread Group**: 20 users, ramp-up period of 5 seconds, loop count of 1.
- **HTTP Request**: `POST /api/v1/accounts/transfer`
  - Parameters:
    - `fromOwnerId=1001`
    - `toOwnerId=3003`
    - `amount=100`
- **Assertions**: Check for success message in the response.
- **Listeners**: View Results Tree, Summary Report.

### JMeter Test Result
The following screenshot shows the results of a JMeter test simulating 20 concurrent fund transfers.

![JMeter Test Result](moneytransfer concurrency test jmeter.png)

## Swagger API Documentation
Swagger provides an interactive UI to test and document the API endpoints. After running the application, you can access the Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```

## Logging
The application uses Log4j2 for logging. The configuration can be found in `log4j2.xml`. Here is an example configuration:
```xml
<Configuration status="WARN">
    <Appenders>
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout pattern="%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"/>
        </Console>
    </Appenders>
    <Loggers>
        <Root level="info">
            <AppenderRef ref="Console"/>
        </Root>
    </Loggers>
</Configuration>
```

## Future Enhancements
- **Add Support for More Currencies**: Extend the list of supported currencies and handle conversions more dynamically.
- **Improve Concurrency Handling**: Explore optimistic locking and other concurrency control mechanisms.
- **Enhance Performance Testing**: Add more comprehensive JMeter tests to simulate various load scenarios.

## Contributing
1. **Fork the repository**.
2. **Create a new branch**:
   ```bash
   git checkout -b feature/your-feature-name
   ```
3. **Commit your changes**:
   ```bash
   git commit -m "Add some feature"
   ```
4. **Push to the branch**:
   ```bash
   git push origin feature/your-feature-name
   ```
5. **Open a pull request**.

## License
This project is licensed under the MIT License.

