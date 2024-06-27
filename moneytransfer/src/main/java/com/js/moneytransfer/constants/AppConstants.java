package com.js.moneytransfer.constants;

public class AppConstants {

    public static final String API_V1 = "/api/v1/accounts";
    public static final String ACCOUNT_CREATION_SUCCESS = "Account created successfully with ID: {}";
    public static final String ACCOUNT_CREATION_LOG = "Creating account for owner ID: {}";
    public static final String ACCOUNT_NOT_FOUND = "Account not found for owner ID: {}";
    public static final String INSUFFICIENT_BALANCE = "Insufficient balance in debit account for owner ID: {}";
    public static final String TRANSFER_SUCCESS = "Transfer successful from owner ID {} to owner ID {}";
    public static final String TRANSFER_LOG = "Transferring {} from owner ID {} to owner ID {}";
    public static final String EXCHANGE_RATE_ERROR = "Unable to retrieve exchange rate from {} to {}";
    public static final String LOCK_ACQUISITION_ERROR = "Failed to acquire lock for accounts {} or {}";
    public static final String UNEXPECTED_ERROR = "An unexpected error occurred: {}";
    public static final String SUCCESSFULLY_LOADED_INITIAL_ACCOUNTS = "Successfully loaded initial accounts: {}";
    public static final String ACCOUNTS_ALREADY_EXIST_IN_THE_DATABASE_SKIPPING_INITIAL_DATA_LOADING = "Accounts already exist in the database. Skipping initial data loading.";
    public static final String ERROR_OCCURRED_WHILE_LOADING_INITIAL_ACCOUNTS = "Error occurred while loading initial accounts";
    public static final String FETCHING_BALANCE_FOR_OWNER_ID = "Fetching balance for owner ID: {}";
    public static final String BALANCE_FOR_OWNER_ID = "Balance for owner ID: {} , {}";

    private AppConstants() {
        // Private constructor to prevent instantiation
    }
}
