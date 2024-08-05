package com.example.withdb.BussinessLogic;

public record OperationValidation(String errorMessage, boolean success) {
    @Override
    public boolean success() {
        return success;
    }
    @Override
    public String errorMessage() {
        return errorMessage;
    }
}
