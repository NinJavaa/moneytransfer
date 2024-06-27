package com.js.moneytransfer.exception;

import com.js.moneytransfer.constants.AppConstants;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    void handleLockAcquisitionException_ShouldReturnConflictStatus() {
        LockAcquisitionException exception = new LockAcquisitionException("Lock acquisition error", new Exception());
        ResponseEntity<String> response = globalExceptionHandler.handleLockAcquisitionException(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Lock acquisition error", response.getBody());
    }

    @Test
    void handleAccountNotFoundException_ShouldReturnNotFoundStatus() {
        AccountNotFoundException exception = new AccountNotFoundException("Account not found");
        ResponseEntity<String> response = globalExceptionHandler.handleAccountNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Account not found", response.getBody());
    }

    @Test
    void handleInsufficientFundsException_ShouldReturnBadRequestStatus() {
        InsufficientFundsException exception = new InsufficientFundsException("Insufficient funds");
        ResponseEntity<String> response = globalExceptionHandler.handleInsufficientFundsException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Insufficient funds", response.getBody());
    }

    @Test
    void handleExchangeRateNotFoundException_ShouldReturnServiceUnavailableStatus() {
        ExchangeRateNotFoundException exception = new ExchangeRateNotFoundException("Exchange rate not found");
        ResponseEntity<String> response = globalExceptionHandler.handleExchangeRateNotFoundException(exception);

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals("Exchange rate not found", response.getBody());
    }

    @Test
    void handleException_ShouldReturnInternalServerErrorStatus() {
        Exception exception = new Exception("Unexpected error");
        ResponseEntity<String> response = globalExceptionHandler.handleException(exception);

        String expectedMessage = String.format(AppConstants.UNEXPECTED_ERROR, "Unexpected error");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(expectedMessage, response.getBody());
    }
}
