
---

# Money Transfer API

## Overview

The Money Transfer API is a robust, scalable RESTful service for managing account balances and transferring funds between accounts. Designed to support high concurrency and integrate with external exchange rate providers, this application handles currency conversions seamlessly.

## Features

- **Account Management**
  - Create and manage user accounts with unique identifiers, balances, and currencies.
- **Fund Transfer**
  - Transfer funds between accounts with real-time currency conversion.
- **Transaction History**
  - Retrieve transaction history for specific accounts.
- **Exchange Rate Integration**
  - Integrate with external exchange rate providers to fetch and cache conversion rates.
- **Concurrency Handling**
  - Ensure thread-safe operations with robust transaction management.
- **Caching**
  - Efficient caching mechanisms to reduce unnecessary API calls and enhance performance.

## Technologies

- **Java 17**
- **Spark Java**: Lightweight web framework for creating RESTful APIs.
- **Hibernate**: ORM framework for managing database interactions.
- **H2 Database**: In-memory database for testing and development purposes.
- **Guice**: Dependency injection framework.
- **Caffeine**: High-performance caching library.
- **Gson**: Library for converting Java objects to JSON and vice versa.
- **SLF4J**: Simple Logging Facade for Java.
- **MapStruct**: Annotation-based Java bean mapper for DTO conversion.
- **AsyncHttpClient**: Asynchronous HTTP and WebSocket client library.

## Endpoints

### Account Management

- **Get Account Balance**
  - `GET /accounts/{accountId}/balance`
  - Retrieves the balance of the specified account.

- **Get Transactions for Account**
  - `GET /accounts/{accountId}/transactions`
  - Retrieves the transaction history for the specified account.

### Fund Transfer

- **Transfer Funds**
  - `POST /accounts/transfer`
  - Transfers funds between accounts, handling currency conversion if necessary.

## Setup

### Prerequisites

- Java 17
- Maven or Gradle
- IDE (e.g., IntelliJ IDEA, Eclipse)

### Running the Application

1. **Clone the repository:**
   ```sh
   git clone https://github.com/yourusername/money-transfer-api.git
   cd money-transfer-api
   ```

2. **Set up environment variables:**
   - `APPLICATION_PROPERTIES_PATH`: Path to your `application.properties` file.
   - `EXCHANGE_RATE_API_KEY`: API key for the exchange rate provider.

3. **Build and run the application:**
   ```sh
   ./gradlew run
   ```

4. **Access the API:**
   The API will be available at `http://localhost:4567`.

### Configuration

Modify the `application.properties` file to configure the API:

```properties
default.exchange.rate.api.key=your_default_api_key
exchange.rate.api.url=https://v6.exchangerate-api.com/v6
cache.expiry.minutes=60
update.frequency.minutes=5
```

## Testing

### Unit Tests

Run the unit tests using Gradle:

```sh
./gradlew test
```

## Contribution

Contributions are welcome! Please fork the repository and create a pull request with your changes.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Contact

For any questions or support, please contact [your-email@example.com].

---
