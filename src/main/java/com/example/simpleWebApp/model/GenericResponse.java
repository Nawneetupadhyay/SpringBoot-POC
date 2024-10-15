package com.example.simpleWebApp.model;

import com.example.simpleWebApp.enums.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;

//to discard non-null fields while sending the data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericResponse<T> {
    private T data;
    private String errorMessage;
    private ErrorCode errorCode;

    public GenericResponse(T data) {
        this.data = data;
    }

    public GenericResponse(String errorMessage, ErrorCode errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
