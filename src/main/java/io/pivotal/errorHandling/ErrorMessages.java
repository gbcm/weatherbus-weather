package io.pivotal.errorHandling;

import lombok.Getter;

//TODO: Consider making this a table
public enum ErrorMessages {
    UNKNOWN(ErrorPathConstants.ERROR_PATH, "Unknown error occurred"),
    MISSING_PARAM(ErrorPathConstants.ERROR_NO_PARAMS_PATH, "Query parameters not provided"),
    PARAM_OUT_OF_RANGE(ErrorPathConstants.ERROR_PARAM_OUT_OF_RANGE_PATH, "Query parameter out of range"),
    RETROFIT(ErrorPathConstants.ERROR_RETROFIT_CONFIG_PATH, "Retrofit error: An API dependency may have changed"),
    USER_NOT_FOUND(ErrorPathConstants.ERROR_USER_NOT_FOUND_PATH, "User not found"),
    USER_ALREADY_EXISTS(ErrorPathConstants.USER_ALREADY_EXISTS_PATH, "User already exists"),
    BAD_JSON(ErrorPathConstants.JSON_SYNTAX_ERROR_PATH, "Invalid JSON")
    ;
    @Getter
    private final String errorPath;
    @Getter
    private final String errorMessage;

    ErrorMessages(String errorPath, String errorMessage) {
        this.errorPath = errorPath;
        this.errorMessage = errorMessage;
    }
}
