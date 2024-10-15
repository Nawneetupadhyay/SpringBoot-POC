package com.example.simpleWebApp.enums;

public enum ErrorCode {
    PRODUCT_NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500),
    BAD_REQUEST(400),
    PRODUCT_CREATION_FAILED(500),
    PRODUCT_UPDATE_FAILED(500),
    PRODUCT_DELETION_FAILED(500);

    private final int httpStatus;

    ErrorCode(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}
